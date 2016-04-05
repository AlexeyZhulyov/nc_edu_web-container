package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.web.servlet.RequestWrapper;
import nc.sumy.edu.webcontainer.web.servlet.ResponseWrapper;
import nc.sumy.edu.webcontainer.web.servlet.ServletConfigImpl;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.jsp.HttpJspPage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static nc.sumy.edu.webcontainer.common.ClassUtil.*;

/**
 * Class for processing jsps.
 */
public class JspHandlerImpl implements JspHandler {

    private static final ConcurrentMap<File, HttpJspPage> INSTANCES = new ConcurrentHashMap<>();
    private String outputDir;
    private final JspC jspc = new JspC();

    /**
     * Create a new (with compiling, loading and initiating) or take an existing instance of jsp class,
     * invoke method service and return response.
     */
    @Override
    public HttpResponse processJSP(HttpRequest request, File file) {

        HttpJspPage jspPage = null;
        PrintWriter logWriter = null;
        ResponseWrapper responseWrapper = new ResponseWrapper(new HttpResponse(200));
        ServletConfig servletConfig = null;

        if (INSTANCES.containsKey(file)) {
            jspPage = INSTANCES.get(file);
            servletConfig = jspPage.getServletConfig();
            if (((ServletConfigImpl) jspPage.getServletConfig()).getDate().getTime() < file.lastModified()) {
                destroy(file);
                jspPage = null;
            }
        }
        if (jspPage == null) {
            String filePath = file.getAbsolutePath();
            String pathUnpackWar = filePath.substring(0, filePath.indexOf(File.separator, filePath.lastIndexOf("www") + 4) + 1);
            outputDir = pathUnpackWar + "WEB-INF" + File.separator + "classes";
            String lib = pathUnpackWar + "WEB-INF" + File.separator + "lib";
            ClassUtil.addFilesFromDirToSysClassPath(lib);
            ClassUtil.addFileToSystemClassPath(outputDir);


            execute(pathUnpackWar);
            Class<HttpJspPage> klass = find(file);

            jspPage = newInstance(klass);
            try {
                servletConfig = new ServletConfigImpl(logWriter, new File(pathUnpackWar).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new WebURLException(pathUnpackWar, e);
            }
            try {
                jspPage.init(servletConfig);
            } catch (ServletException e) {
                throw new JspInitException(filePath, e);
            }
            INSTANCES.put(file, jspPage);
        }
        RequestWrapper requestWrapper = new RequestWrapper(request, servletConfig);

        try {
            jspPage._jspService(requestWrapper, responseWrapper);
        } catch (ServletException | IOException e) {
            throw new JspServiceException(file.getPath(), e);
        }
        return (HttpResponse) responseWrapper.getResponse();
    }

    public void destroy(File file) {
        INSTANCES.remove(file).jspDestroy();
    }

    private void execute(String dir) {

        jspc.setOutputDir(outputDir);
        jspc.setCompile(true);
        jspc.setPackage("executed");  //"executed"
        try {
            jspc.setArgs(new String[]{"-webapp", dir});
            jspc.execute();
        } catch (JasperException e) {
            throw new JspExecutionException(dir, e);

        }
    }

    private Class<HttpJspPage> find(File file) {
        ClassLoader classLoader;
        Class klass;
        URL klassToLoad = null;
        try {
            klassToLoad = new File(outputDir).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new WebURLException(outputDir, e);
        }
        classLoader = new URLClassLoader(new URL[]{klassToLoad});
        String className = "executed.jsp." + file.getName().replace('.', '_');
        try {
            klass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new JspClassNotFoundException(className, e);
        }
        return (Class<HttpJspPage>) klass;
    }
}

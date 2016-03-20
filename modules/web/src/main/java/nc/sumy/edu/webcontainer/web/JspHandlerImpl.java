package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpResponse;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.HttpJspPage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static nc.sumy.edu.webcontainer.common.ClassUtil.*;

public class JspHandlerImpl implements JspHandler {

    private final Map<File, HttpJspPage> instances = new HashMap<>();
    private static final String OUTPUT_DIR = "../www/default/jspTemp"; //"../www/default/jspTemp"
    private final JspC jspc = new JspC();

    @Override
    public HttpServletResponse processJSP(HttpServletRequest request, File file) {

        HttpJspPage jspPage;

        if(instances.containsKey(file))
            jspPage = instances.get(file);
        else {
            execute(file.getParent());
            Class<HttpJspPage> klass = find(file);

            jspPage = newInstance(klass);
            jspPage.jspInit();
            instances.put(file,jspPage);
        }

        ResponseWrapper response = new ResponseWrapper(new HttpResponse(200));

        try {
            jspPage._jspService(request,response);
        } catch (ServletException e) {
            throw new WebException("Servlet exception with _jspService",e);
        } catch (IOException e) {
            throw new WebException("IO exception with _jspService",e);
        }
        return response;
    }

    private void execute(String dir) {
        jspc.setOutputDir(OUTPUT_DIR);
        jspc.setCompile(true);
        jspc.setPackage("executed");
        try {
            jspc.setArgs(new String[]{"-webapp", dir});
            jspc.execute();
        } catch (JasperException e) {
            throw new WebException("Cannot execute jsp",e);

        }
    }

    private Class<HttpJspPage> find (File file){
        ClassLoader classLoader;
        Class klass;
        try {
            classLoader = new URLClassLoader(new URL[]{new File(OUTPUT_DIR).toURI().toURL()});
        } catch (MalformedURLException e) {
            throw new WebException("Malformed url of output directory for class loader",e);
        }
        try {
            klass = classLoader.loadClass("executed." + file.getName().replace('.','_'));
        } catch (ClassNotFoundException e) {
            throw new WebException("Class not found",e);
        }
        return (Class<HttpJspPage>) klass;
    }
}

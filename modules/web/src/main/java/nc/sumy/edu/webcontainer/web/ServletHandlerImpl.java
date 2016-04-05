package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.web.servlet.RequestWrapper;
import nc.sumy.edu.webcontainer.web.servlet.ResponseWrapper;
import nc.sumy.edu.webcontainer.web.servlet.ServletConfigImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static nc.sumy.edu.webcontainer.common.ClassUtil.newInstance;

/**
 * Class for processing servlets.
 */
public class ServletHandlerImpl implements ServletHandler {

    private static final ConcurrentMap<String, HttpServlet> INSTANCES = new ConcurrentHashMap<>();

    /**
     * Create a new (with initiating) or take an existing instance of servlet class,
     * invoke method service and return response.
     */
    public HttpResponse processServlet(HttpRequest request, Class servletClass) {

        HttpServlet servlet;
        PrintWriter logWriter = null;
        ResponseWrapper responseWrapper = new ResponseWrapper(new HttpResponse(200));
        ServletConfig servletConfig = null;

        String className = servletClass.getCanonicalName();
        //Class<HttpServlet> servletClass = servletClass;

        if (INSTANCES.containsKey(className)) {
            servlet = INSTANCES.get(className);
            servletConfig = servlet.getServletConfig();
        }
        else {
            servlet = (HttpServlet) newInstance(servletClass);

            String klassPath = getKlassPath(servletClass);
            String libPath = getLibPath(klassPath);
            String configPath = getConfigPath(klassPath);

            URL configUrl = getConfigUrl(configPath);

            ClassUtil.addFilesFromDirToSysClassPath(libPath);

            servletConfig = new ServletConfigImpl(logWriter, configUrl);
            servletInitialization(servlet, servletConfig);
            INSTANCES.put(className, servlet);
        }

        RequestWrapper requestWrapper = new RequestWrapper(request, servletConfig);

        try {
            servlet.service(requestWrapper, responseWrapper);
        } catch (ServletException | IOException e) {
            throw new ServletServiceException(className, e);
        }
        return (HttpResponse) responseWrapper.getResponse();
    }

    private URL getConfigUrl(String configPath) {
        try {
            return (configPath == null ? null : new File(configPath).toURI().toURL());
        } catch (MalformedURLException e) {
            throw new WebURLException(configPath, e);
        }
    }

    private String getConfigPath(String klassPath) {
        return (klassPath.contains("WEB-INF") ? klassPath.substring(0, klassPath.lastIndexOf("/WEB-INF")) : null);
        // (klassPath.contains("WEB-INF") ? klassPath.substring(0, klassPath.lastIndexOf("/WEB-INF")) : null)
    }

    private String getLibPath(String klassPath) {
        return (klassPath.contains("classes") ? klassPath.substring(0, klassPath.lastIndexOf("classes")) + "lib/" : null);
    }

    private String getKlassPath(Class klass) {
        return klass.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    private void servletInitialization(Servlet servlet, ServletConfig servletConfig) {
        try {
            servlet.init(servletConfig);
        } catch (ServletException e) {
            throw new ServletInitException(servlet.getClass().getName(), e);
        }
    }

    public void destroy(Class klass) {
        INSTANCES.remove(klass.getCanonicalName()).destroy();
    }
}
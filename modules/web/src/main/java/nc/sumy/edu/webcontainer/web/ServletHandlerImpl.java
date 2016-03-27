package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;

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

public class ServletHandlerImpl implements ServletHandler {

    private static final ConcurrentMap<String, HttpServlet> INSTANCES = new ConcurrentHashMap<>();


    public HttpResponse processServlet(HttpRequest request, Class klass) {

        HttpServlet servlet;
        PrintWriter logWriter = null;
        ResponseWrapper responseWrapper = new ResponseWrapper(new HttpResponse(200));
        ServletConfig servletConfig = null;

        String className = klass.getCanonicalName();
        Class<HttpServlet> servletClass = klass;

        if (INSTANCES.containsKey(className)) {
            servlet = INSTANCES.get(className);
            servletConfig = servlet.getServletConfig();
        }
        else {
            servlet = newInstance(servletClass);

            String klassPath = getKlassPath(klass);
            String libPath = getLibPath(klassPath);
            String configPath = getConfigPath(klassPath);

            URL configUrl = getConfigUrl(configPath);

            ClassUtil.addFilesFromDirToSysClassPath(libPath);

            servletConfig = new ServletConfigImpl(logWriter, configUrl);
            try {
                servlet.init(servletConfig);
                INSTANCES.put(className, servlet);
            } catch (ServletException e) {
                throw new WebException("Cannot do init()", e);
            }
        }

        RequestWrapper requestWrapper = new RequestWrapper(request, servletConfig);

        try {
            servlet.service(requestWrapper, responseWrapper);
        } catch (ServletException | IOException e) {
            throw new WebException("Cannot do service()", e);
        }
        return (HttpResponse) responseWrapper.getResponse();
    }

    private URL getConfigUrl(String configPath) {
        try {
            return (configPath == null ? null : new File(configPath).toURI().toURL());
        } catch (MalformedURLException e) {
            throw new WebException("MalformedURLException for config", e);
        }
    }

    private String getConfigPath(String klassPath) {
        return (klassPath.contains("WEB-INF") ? klassPath.substring(0, klassPath.lastIndexOf("/WEB-INF")) : null);
    }

    private String getLibPath(String klassPath) {
        return (klassPath.contains("classes") ? klassPath.substring(0, klassPath.lastIndexOf("classes")) + "lib/" : null);
    }

    private String getKlassPath(Class klass) {
        return klass.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public void destroy(Class klass) {
        INSTANCES.remove(klass.getCanonicalName()).destroy();
    }
}
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

    private static final ConcurrentMap<String, HttpServlet> instances = new ConcurrentHashMap<>();

    public HttpResponse processServlet(HttpRequest request, Class klass) {

        HttpServlet servlet;
        PrintWriter logWriter = null;
        ResponseWrapper responseWrapper = new ResponseWrapper(new HttpResponse(200));
        ServletConfig servletConfig = null;

        String className = klass.getCanonicalName();
        Class<HttpServlet> servletClass = klass;

        if (instances.containsKey(className))
            servlet = instances.get(className);
        else {
            servlet = newInstance(servletClass);

            String klassPath = klass.getProtectionDomain().getCodeSource().getLocation().getPath();
            String libPath = (klassPath.contains("classes") ? klassPath.substring(0, klassPath.lastIndexOf("classes")) + "lib/" : null);
            String configPath = (klassPath.contains("WEB-INF") ? klassPath.substring(0, klassPath.lastIndexOf("/WEB-INF")) : null);
            URL configUrl = null;
            try {
                configUrl = (configPath == null ? null : new File(configPath).toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ClassUtil.addFilesFromDirToSysClassPath(libPath);

            servletConfig = new ServletConfigImpl(logWriter, configUrl);
            try {
                servlet.init(servletConfig);
                instances.put(className, servlet);
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

    public void destroy(Class klass) {
        instances.remove(klass.getCanonicalName()).destroy();
    }
}
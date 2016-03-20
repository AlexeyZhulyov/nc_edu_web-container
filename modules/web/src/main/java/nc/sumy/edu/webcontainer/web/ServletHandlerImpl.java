package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nc.sumy.edu.webcontainer.common.ClassUtil.newInstance;

public class ServletHandlerImpl implements ServletHandler {

    private final Map<String, HttpServlet> instances = new HashMap<>();

    public ServletResponse processServlet(ServletRequest request, Class klass) {

        HttpServlet servlet = null;

        String className = klass.getCanonicalName();
        Class<HttpServlet> servletClass = klass;

        if (instances.containsKey(className))
            servlet = instances.get(className);
        else {
            servlet = newInstance(servletClass);
            try {
                servlet.init();
                instances.put(className, servlet);
            } catch (ServletException e) {
                throw new WebException("Cannot do init()", e);
            }
        }

        ResponseWrapper response = new ResponseWrapper(new HttpResponse(200));

        try {
            servlet.service(request, response);
        } catch (ServletException e) {
            throw new WebException("Cannot do service()", e);
        } catch (IOException e) {
            throw new WebException("Cannot read servlet?", e);
        }
        return response;
    }
}

package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServletHandlerImpl implements ServletHandler {

    private final Map<String, HttpServlet> instances = new HashMap<>();

    public ServletResponse processServlet(ServletRequest request, Class klass) {

        HttpServlet servlet;

        String className = klass.getCanonicalName();

        if (instances.containsKey(className))
            servlet = instances.get(className);
        else {
            try {
                servlet = (HttpServlet) klass.newInstance();
                servlet.init();
                instances.put(className, servlet);
            } catch (InstantiationException e) {
                throw new WebException("Cannot create instance", e);
            } catch (IllegalAccessException e) {
                throw new WebException("No access", e);
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

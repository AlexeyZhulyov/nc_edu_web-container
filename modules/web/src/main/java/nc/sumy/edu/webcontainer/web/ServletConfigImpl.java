package nc.sumy.edu.webcontainer.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

@SuppressWarnings("PMD")
public class ServletConfigImpl implements ServletConfig {
    private ServletContext servletContext = new ServletContextImpl();
    @Override
    public String getServletName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new UnsupportedOperationException();
    }
}

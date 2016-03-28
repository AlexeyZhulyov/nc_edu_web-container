package nc.sumy.edu.webcontainer.web.servlet;

import org.apache.jasper.servlet.JspCServletContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

@SuppressWarnings("PMD")
public class ServletConfigImpl implements ServletConfig {
    private ServletContext servletContext;
    private final Date date = new Date();

    public ServletConfigImpl(PrintWriter aLogWriter, URL aResourcesBaseURL) {
        servletContext = new JspCServletContext(aLogWriter, aResourcesBaseURL);
    }

    public Date getDate(){
        return date;
    }

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

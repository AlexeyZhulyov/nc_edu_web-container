package nc.sumy.edu.webcontainer.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;

public class WebContainerImpl implements WebContainer {

    @Override
    public String processStatic(File file) {
        WebHandler webHandler = new WebHandlerImpl();
        return webHandler.process(file);
    }

    @Override
    public ServletResponse processServlet(ServletRequest request, Class klass) {
        ServletHandler servletHandler = new ServletHandlerImpl();
        return servletHandler.processServlet(request, klass);
    }

    @Override
    public ServletResponse processJSP(ServletRequest request, File file) {
        return null;
    }
}

package nc.sumy.edu.webcontainer.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ServletHandler {
    ServletResponse processServlet(ServletRequest request, Class klass);
}

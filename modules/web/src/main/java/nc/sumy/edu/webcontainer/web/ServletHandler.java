package nc.sumy.edu.webcontainer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletHandler {
    HttpServletResponse processServlet(HttpServletRequest request, Class klass);
}

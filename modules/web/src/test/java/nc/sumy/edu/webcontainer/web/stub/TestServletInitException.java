package nc.sumy.edu.webcontainer.web.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class TestServletInitException extends HttpServlet {

    @Override
    public void init() throws ServletException {
        throw new ServletException();
    }
}
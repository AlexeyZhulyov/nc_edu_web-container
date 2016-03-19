package nc.sumy.edu.webcontainer.web.stub;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {

//    @Override
//    public void init(ServletConfig config) throws ServletException {
//    }

    private int number;

    @Override
    public void init() throws ServletException {
        number = 1;
        super.init();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello Servlet</h1>");
        out.println("<body>Test servlet #" + number + ".</body>");
        number++;
    }

//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public String getServletInfo() {
//        return null;
//    }
//
//    @Override
//    public ServletConfig getServletConfig() {
//        return null;
//    }
}

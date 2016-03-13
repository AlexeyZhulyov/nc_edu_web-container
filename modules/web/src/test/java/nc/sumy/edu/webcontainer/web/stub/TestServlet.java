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

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<h1>Hello Servlet</h1>");
        out.println("<body>Test servlet.</body>");
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

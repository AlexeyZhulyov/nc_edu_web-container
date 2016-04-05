package executed.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n    <title>Demo Project</title>\r\n    <meta   charset = \"utf-8\">\r\n    <meta   name    = \"viewport\"\r\n            content = \"width=device-width, initial-scale=1\">\r\n    <link   rel     = \"stylesheet\"\r\n            href    = \"css/bootstrap.min.css\">\r\n    <script src     = \"js/jquery-1.11.1.min.js\"></script>\r\n    <script src     = \"js/bootstrap.min.js\"></script>\r\n</head>\r\n<body>\r\n\r\n    <div class=\"container\">\r\n        <div class=\"jumbotron\">\r\n            <h1>Welcome to the Demo Project</h1>\r\n            <p>This is simple example of Java web application</p>\r\n        </div>\r\n\r\n        <div class=\"row\">\r\n\r\n            <div class=\"col-sm-4\">\r\n                <h3>Servlets</h3>\r\n                <p>A Java servlet is a Java programming language program that extends the capabilities of a server. Although servlets can respond to any types of requests, they most commonly implement applications hosted on Web servers.</p>\r\n                <a href=\"https://en.wikipedia.org/wiki/Java_servlet\">\r\n");
      out.write("                    <i class=\"fa fa-external-link\">More</i>\r\n                </a>\r\n            </div>\r\n\r\n            <div class=\"col-sm-4\">\r\n                <h3>JSP</h3>\r\n                <p>JavaServer Pages (JSP) is a technology that helps software developers create dynamically generated web pages based on HTML, XML, or other document types. Released in 1999 by Sun Microsystems, JSP is similar to PHP, but it uses the Java programming language.</p>\r\n                <a href=\"https://en.wikipedia.org/wiki/JavaServer_Pages\">\r\n                    <i class=\"fa fa-external-link\">More</i>\r\n                </a>\r\n            </div>\r\n\r\n            <div class=\"col-sm-4\">\r\n                <h3>Let's try</h3>\r\n                <a href=\"Blog?action=toCommentsPage\" >\r\n                    <button type=\"button\" class=\"btn btn-success\">Push Me</button>\r\n                </a>\r\n            </div>\r\n        </div>\r\n    </div>\r\n\r\n</body>\r\n</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

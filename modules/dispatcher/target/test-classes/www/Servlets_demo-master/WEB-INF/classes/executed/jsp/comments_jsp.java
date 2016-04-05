package executed.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import sumy.javacourse.webdemo.model.DBStub;
import sumy.javacourse.webdemo.controller.Main;
import sumy.javacourse.webdemo.model.Comment;

public final class comments_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


  // Methods example... It's deprecated approach too.
  int getParameterAsInteger(HttpSession session, String key){
    Object o = session.getAttribute(key);
    if (o != null) {
      return Integer.valueOf( o.toString() );
    }
    return 0;
  }

  float calculatePercent(int value, float total) {
    return value != 0? value/total*100 : value;
  }

  String cutFractionalDigits(float value){
    return String.format("%.0f", value);
  }

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

      out.write("\r\n\r\n\r\n");

  // Simple usage of HttpSession. Please do not use it in real applications/labs.
  int agreeAmount     = getParameterAsInteger( session, Main.AGREE     );
  int disagreeAmount  = getParameterAsInteger( session, Main.DISAGREE  );
  int tentativeAmount = getParameterAsInteger( session, Main.TENTATIVE );

  float total = agreeAmount + disagreeAmount + tentativeAmount;

  float agreePercent      = calculatePercent( agreeAmount,     total );
  float disagreePercent   = calculatePercent( disagreeAmount,  total );
  float tentativePercent  = calculatePercent( tentativeAmount, total );

      out.write("\r\n\r\n");
      out.write("\r\n\r\n\r\n\r\n<!--\r\n  Warning: this is obsolete technology.\r\n  Current approach implemented for example of pure Servlet/JSP technologies.\r\n-->\r\n<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n  <title>Demo Project</title>\r\n  <meta   charset = \"utf-8\">\r\n  <meta   name    = \"viewport\"\r\n          content = \"width=device-width, initial-scale=1\">\r\n  <link   rel     = \"stylesheet\"\r\n          href    = \"css/bootstrap.min.css\">\r\n  <script src     = \"js/jquery-1.11.1.min.js\"></script>\r\n</head>\r\n<body>\r\n\r\n<div class=\"container\">\r\n  <div class=\"jumbotron\">\r\n    <h2>Are the Servlet & JSP simple technologies?</h2>\r\n\r\n    <div    class = \"progress\">\r\n      <div  class = \"progress-bar progress-bar-success\"\r\n            role  = \"progressbar\"\r\n            style = \"width: ");
      out.print( agreePercent );
      out.write("%\">\r\n        Agree\r\n      </div>\r\n\r\n      <div  class = \"progress-bar progress-bar-danger\"\r\n            role  = \"progressbar\"\r\n            style = \"width:  ");
      out.print( disagreePercent );
      out.write("%\">\r\n        Disagree\r\n      </div>\r\n\r\n      <div  class = \"progress-bar progress-bar-warning\"\r\n            role  = \"progressbar\"\r\n            style = \"width: ");
      out.print( tentativePercent );
      out.write("%\">\r\n        Tentative\r\n      </div>\r\n    </div>\r\n\r\n    <table class=\"table\">\r\n      <thead>\r\n        <tr>\r\n          <th style=\"width: 30px\">Vote</th>\r\n          <th>Percent</th>\r\n        </tr>\r\n      </thead>\r\n\r\n      <tbody>\r\n        <tr>\r\n          <td>Agree</td>\r\n          <td>");
      out.print( cutFractionalDigits( agreePercent ) );
      out.write("%</td>\r\n        </tr>\r\n        <tr>\r\n          <td>Disagree</td>\r\n          <td>");
      out.print( cutFractionalDigits( disagreePercent ) );
      out.write("%</td>\r\n        </tr>\r\n        <tr>\r\n          <td>Tentative</td>\r\n          <td>");
      out.print( cutFractionalDigits( tentativePercent) );
      out.write("%</td>\r\n        </tr>\r\n        <tr>\r\n          <th>Total</th>\r\n          <th>");
      out.print( cutFractionalDigits( total ) );
      out.write("</th>\r\n        </tr>\r\n      </tbody>\r\n    </table>\r\n\r\n    <form role=\"form\" action=\"Blog\">\r\n\r\n      <div class=\"radio\">\r\n        <label>\r\n          <input    type      = \"radio\"\r\n                    name      = \"voteType\"\r\n                    value     = \"agree\"\r\n                    checked   = \"checked\">Agree</label>\r\n      </div>\r\n\r\n      <div class=\"radio\">\r\n        <label>\r\n          <input    type      = \"radio\"\r\n                    name      = \"voteType\"\r\n                    value     = \"disagree\">Disagree</label>\r\n      </div>\r\n\r\n      <div class=\"radio\">\r\n        <label>\r\n          <input    type      = \"radio\"\r\n                    name      = \"voteType\"\r\n                    value     = \"tentative\">Tentative</label>\r\n      </div>\r\n\r\n      <input        type      = \"hidden\"\r\n                    name      = \"action\"\r\n                    value     = \"saveVote\">\r\n\r\n      <button       type      = \"submit\"\r\n                    class     = \"btn btn-success\">Vote</button>\r\n    </form>\r\n  </div>\r\n\r\n  <div class=\"row\">\r\n");
      out.write("    <div            class     = \"col-sm-3 col-md-6 col-lg-4\">\r\n      <form         role      = \"form\"\r\n                    action    = \"Blog\">\r\n\r\n        <div        class     = \"form-group\">\r\n          <label    for       = \"author\">Author:</label>\r\n          <input    type      = \"text\"\r\n                    class     = \"form-control\"\r\n                    id        = \"author\"\r\n                    name      = \"author\"\r\n                    required  = \"true\">\r\n        </div>\r\n\r\n        <div        class     = \"form-group\">\r\n          <label    for       = \"email\">Email:</label>\r\n          <input    type      = \"text\"\r\n                    class     = \"form-control\"\r\n                    id        = \"email\"\r\n                    name      = \"email\"\r\n                    required  = \"true\"\r\n                    pattern   = \"[^@]+@[^@]+\\.[a-zA-Z]{2,6}\"\r\n                    title     = \"xxx@xxx.xx\">\r\n        </div>\r\n\r\n        <div        class     = \"form-group\">\r\n          <label    for       = \"comment\">Comment:</label>\r\n");
      out.write("          <textarea class     = \"form-control\"\r\n                    rows      = \"5\"\r\n                    id        = \"comment\"\r\n                    name      = \"comment\"\r\n                    required  = \"required\"\r\n                    maxlength = \"3000\"></textarea>\r\n        </div>\r\n\r\n        <input      type      = \"hidden\"\r\n                    name      = \"action\"\r\n                    value     = \"saveComment\">\r\n\r\n        <button     type      = \"submit\"\r\n                    class     = \"btn btn-success\">Send</button>\r\n      </form>\r\n    </div>\r\n  </div>\r\n\r\n  <div class=\"row\">\r\n    <div class=\"col-sm-12\">\r\n      ");

        for(Comment comment : DBStub.comments()) {
      
      out.write("\r\n          <h3>");
      out.print(comment.getAuthor());
      out.write("</h3>\r\n          <p>");
      out.print(comment.getText() );
      out.write("</p>\r\n          <br/>\r\n      ");

        }
      
      out.write("\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n</body>\r\n</html>");
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

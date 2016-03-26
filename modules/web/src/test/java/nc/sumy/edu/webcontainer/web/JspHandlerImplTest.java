package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class JspHandlerImplTest {

    private final JspHandler jspHandler = new JspHandlerImpl();
    private HttpRequest request;
    File jspTest = new File(JspHandlerImplTest.class.getResource("/test.jsp").getPath());
    private static final String BLANK = "\\s";
    //File jspIndex = new File(JspHandlerImplTest.class.getResource("/index.jsp").getPath());
    //File jspComments = new File(JspHandlerImplTest.class.getResource("/comments.jsp").getPath());
//    File jspIndex = new File(new File(".").getParentFile() + "/modules/temp/webapp/jsp/index.jsp");
//    File jspComments = new File("../modules/temp/webapp/jsp/comments.jsp");
//    File jspIndex = new File(JspHandlerImplTest.class.getResource("/web").getPath());
//    File jspComments = new File(JspHandlerImplTest.class.getResource("/comments.jsp").getPath());

    @Before
    public void setUp() {
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(requestStr, "", "");
    }

//    @Test
//    public void process1() {
//        int number = 1;
//        String expected = "\n<html>\n" +
//                "<h1>Hello JSP</h1>\n" +
//                "<body>\n" +
//                "<body>Test JSP #%s.</body>\n" +
//                "</body>\n" +
//                "</html>";
//        byte[] body = jspHandler.processJSP(request,jspTest).getBody();
//        String actual = new String(body);
//        assertEquals(format(expected,number), actual.replace("\r", ""));
//        number++;
//    }

//    @Test
//    public void process2() throws URISyntaxException {
//        File file = new File("im_there");
//        System.out.println(file.getAbsolutePath());
//        //System.out.println(JspHandlerImplTest.class.getResource("/index.jsp"));
//        File jspIndex = new File("../www/Servlets_demo/jsp/index.jsp");
//        System.out.println(jspIndex.getAbsolutePath() + " " + jspIndex.exists());
//        String expected = ClassUtil.fileToString(new File(JspHandlerImpl.class.getResource("/TestIndex.html").toURI()));
//        byte[] body = jspHandler.processJSP(request,jspIndex).getBody();
//        String actual = new String(body);
//        assertEquals(expected.replaceAll(BLANK, ""), actual.replaceAll(BLANK, ""));
//    }

//    @Test
//    public void process3() throws URISyntaxException {
//        File jspComments = new File("../www/Servlets_demo/jsp/comments.jsp");
//        System.out.println(jspComments.getAbsolutePath() + " " + jspComments.exists());
//        String expected = ClassUtil.fileToString(new File(JspHandlerImpl.class.getResource("/TestComments.html").toURI()));
//        byte[] body = jspHandler.processJSP(request,jspComments).getBody();
//        String actual = new String(body);
//        assertEquals(expected.replaceAll(BLANK, ""), actual.replaceAll(BLANK, ""));
//    }
}
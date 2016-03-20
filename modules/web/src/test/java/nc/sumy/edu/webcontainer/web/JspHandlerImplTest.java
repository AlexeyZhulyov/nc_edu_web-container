package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static java.lang.String.format;
import static org.junit.Assert.*;

public class JspHandlerImplTest {

    private final JspHandler jspHandler = new JspHandlerImpl();
    private HttpServletRequest servletRequest;
    File jspTest = new File(JspHandlerImplTest.class.getResource("/test.jsp").getPath());

    @Before
    public void setUp() {
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        servletRequest = new RequestWrapper(new HttpRequest(requestStr, "", ""));
    }

    @Test
    public void process1() {
        int number = 1;
        String expected = "<h1>Hello Servlet</h1>\n<body>Test servlet #%s.</body>\n";
        byte[] body = ((ResponseWrapper) jspHandler.processJSP(servletRequest,jspTest)).getResponse().getBody();
        String actual = new String(body);
        assertEquals(format(expected,number), actual.replace("\r", ""));
        number++;
        body = ((ResponseWrapper) jspHandler.processJSP(servletRequest,jspTest)).getResponse().getBody();
        actual = new String(body);
        assertEquals(format(expected,number), actual.replace("\r", ""));
    }
}
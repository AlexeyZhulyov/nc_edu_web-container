package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.web.stub.*;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletRequest;

import static org.junit.Assert.*;

public class ServletHandlerImplTest {
//    private String result;
    private ServletHandler servletHandler;
    ServletRequest servletRequest;
    int number = 1;
    private final static String EXPECT_EXCEPTION = "Expected an CgiException to be thrown";

    @Before
    public void setUp() throws Exception {
        //result = "<h1>Hello Servlet</h1>\n<body>Test servlet #" + number + ".</body>\n";
        servletHandler = new ServletHandlerImpl();
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        servletRequest = new RequestWrapper(new HttpRequest(requestStr, "", ""));
    }

    @Test
    public void process1() {
        assertEquals("<h1>Hello Servlet</h1>\n<body>Test servlet #" + number + ".</body>\n",
                new String(((ResponseWrapper) servletHandler.processServlet(servletRequest, TestServlet.class)).getResponse().getBody()).replace("\r", ""));
        number++;

        assertEquals("<h1>Hello Servlet</h1>\n<body>Test servlet #" + number + ".</body>\n",
                new String(((ResponseWrapper) servletHandler.processServlet(servletRequest, TestServlet.class)).getResponse().getBody()).replace("\r", ""));
    }

    @Test
    public void processExceptionMessage1() {
        try {
            servletHandler.processServlet(servletRequest, AbstractTestServlet.class);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals("Cannot create instance", e.getMessage());
        }
    }

    @Test
    public void processExceptionMessage2() {
        try {
            servletHandler.processServlet(servletRequest, TestServletWithPrivateConstructor.class);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals("No access", e.getMessage());
        }
    }

    @Test
    public void processExceptionMessage3() {
        try {
            servletHandler.processServlet(servletRequest, TestServletInitException.class);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals("Cannot do init()", e.getMessage());
        }
    }

    @Test
    public void processExceptionMessage4() {
        try {
            servletHandler.processServlet(servletRequest, TestServletServiceException.class);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals("Cannot do service()", e.getMessage());
        }
    }

    @Test
    public void processExceptionMessage5() {
        try {
            servletHandler.processServlet(servletRequest, TestServletIOException.class);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals("Cannot read servlet?", e.getMessage());
        }
    }
}
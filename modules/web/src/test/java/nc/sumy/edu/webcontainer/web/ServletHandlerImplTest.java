package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.web.stub.*;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import static org.junit.Assert.assertEquals;

public class ServletHandlerImplTest {
    private ServletHandler servletHandler;
    private HttpRequest request;

    @Before
    public void setUp() {
        servletHandler = new ServletHandlerImpl();
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1\r\n" +
                "Host" + ": foo.com\r\n" +
                "Accept" + ": text/html\r\n" +
                "Range-Unit: 3388 | 1024\r\n" +
                "Cookie: JSESSION=123456789";
        request = new HttpRequest(requestStr, "", "");
    }

    @Test
    public void processServletState() throws ClassNotFoundException, MalformedURLException {
        String expected = "<h1>Hello Servlet</h1>\n<body>Test servlet #1.</body>\n";
        String actual = new String(servletHandler.processServlet(request, TestServlet.class).getBody());
        assertEquals(expected, actual.replace("\r", ""));

        expected = "<h1>Hello Servlet</h1>\n<body>Test servlet #2.</body>\n";
        actual = new String(servletHandler.processServlet(request, TestServlet.class).getBody());
        assertEquals(expected, actual.replace("\r", ""));
    }

    @Test
    public void processServletRequestResponse() throws ClassNotFoundException {
        String expected = "<h1>Test Servlet</h1>\n" + "<body>\n" +
                "<h1>ServletRequest:</h1>\n" + "<p>RemoteHost: </p>\n" +
                "<p>ContextPath: null</p>\n" + "<p>Parameter Accept: </p>\n" +
                "<p>Session: nc.sumy.edu.webcontainer.web.servlet.HttpSessionImpl</p>\n" +
                "<h1>ServletResponse:</h1>\n" + "<p>ContentType: text/html</p>\n" +
                "\n" + "</body>\n";
        String actual = new String(servletHandler.
                processServlet(request, TestServletRequestResponse.class).getBody());
        assertEquals(expected, actual.replace("\r", ""));
    }

    @Test
    public void destroyServlet() throws ClassNotFoundException {
        ((ServletHandlerImpl) servletHandler).destroy(TestServlet.class);
    }

    @Test(expected = ServletInitException.class)
    public void processInitException() {
        servletHandler.processServlet(request, TestServletInitException.class);
    }

    @Test(expected = ServletServiceException.class)
    public void processServiceException() {
        servletHandler.processServlet(request, TestServletServiceException.class);
    }

}
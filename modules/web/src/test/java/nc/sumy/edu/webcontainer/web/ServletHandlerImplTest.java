package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.common.InstanceNotCreatedException;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.web.stub.*;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static java.lang.String.format;

public class ServletHandlerImplTest {
    private ServletHandler servletHandler;
    private HttpServletRequest servletRequest;

    @Before
    public void setUp() {
        servletHandler = new ServletHandlerImpl();
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
        byte[] body = ((ResponseWrapper) servletHandler.processServlet(servletRequest,TestServlet.class)).getResponse().getBody();
        String actual = new String(body);
        assertEquals(format(expected,number), actual.replace("\r", ""));
        number++;
        body = ((ResponseWrapper) servletHandler.processServlet(servletRequest,TestServlet.class)).getResponse().getBody();
        actual = new String(body);
        assertEquals(format(expected,number), actual.replace("\r", ""));
    }

    @Test(expected = InstanceNotCreatedException.class)
    public void processExceptionMessage1() {
        servletHandler.processServlet(servletRequest, AbstractTestServlet.class);
    }

    @Test(expected = InstanceNotCreatedException.class)
    public void processExceptionMessage2() {
        servletHandler.processServlet(servletRequest, TestServletWithPrivateConstructor.class);
    }

    @Test(expected = WebException.class)
    public void processExceptionMessage3() {
        servletHandler.processServlet(servletRequest, TestServletInitException.class);
    }

    @Test(expected = WebException.class)
    public void processExceptionMessage4() {
        servletHandler.processServlet(servletRequest, TestServletServiceException.class);
    }

    @Test(expected = WebException.class)
    public void processExceptionMessage5() {
        servletHandler.processServlet(servletRequest, TestServletIOException.class);
    }
}
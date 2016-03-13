package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.web.stub.TestServlet;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletRequest;

import static org.junit.Assert.*;

public class ServletHandlerImplTest {
    private String result;
    private ServletHandler servletHandler;
    ServletRequest servletRequest;

    @Before
    public void setUp() throws Exception {
        result = "<h1>Hello Servlet</h1>\n<body>Test servlet.</body>\n";
        servletHandler = new ServletHandlerImpl();
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        servletRequest = new RequestWrapper(new HttpRequest(requestStr,"",""));
    }

    @Test
    public void process() {
        assertEquals(result, new String(((ResponseWrapper)servletHandler.processServlet(servletRequest, TestServlet.class)).getResponse().getBody()).replace("\r",""));
    }
}
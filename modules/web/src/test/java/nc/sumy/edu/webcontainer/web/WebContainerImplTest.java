package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.web.stub.TestServlet;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletRequest;
import java.io.File;
import java.net.URISyntaxException;

import static java.lang.String.format;
import static org.junit.Assert.*;

public class WebContainerImplTest {
    WebContainer webContainer = null;

    @Before
    public void setUp() throws Exception {
        webContainer = new WebContainerImpl();
    }

    @Test
    public void testProcessStatic() throws Exception {
        File testPage = null;
        try {
            testPage = new File(WebHandlerImplTest.class.getResource("/TestHtml.html").toURI());
        } catch (URISyntaxException e) {
            throw new WebException(format("Cannot parse URI: %s", testPage.getName()), e);
        }
        String processResult = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<title>Page Title</title>\n" +
                "<body>\n" +
                "\n" +
                "<h1>This is a heading</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        assertEquals(processResult, webContainer.processStatic(testPage).replace("\r", ""));
    }

    @Test
    public void testProcessServlet() throws Exception {
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\n" +
                "Host" + ": foo.com" + "\n" +
                "Accept" + ": text/html" + "\n" +
                "Range-Unit: 3388 | 1024";
        ServletRequest servletRequest = new RequestWrapper(new HttpRequest(requestStr, "", ""));
        assertEquals("<h1>Hello Servlet</h1>\n<body>Test servlet #" + 1 + ".</body>\n",
                new String(((ResponseWrapper) webContainer.processServlet(servletRequest, TestServlet.class)).getResponse().getBody()).replace("\r", ""));
    }

    @Test
    public void testProcessJSP() throws Exception {
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\n" +
                "Host" + ": foo.com" + "\n" +
                "Accept" + ": text/html" + "\n" +
                "Range-Unit: 3388 | 1024";
        ServletRequest servletRequest = new RequestWrapper(new HttpRequest(requestStr, "", ""));
        assertEquals(null, webContainer.processJSP(servletRequest, new File("")));
    }
}
package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.*;
import static java.lang.String.*;

public class JspHandlerImplTest {

    private HttpRequest request;

    @Before
    public void setUp() {
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1\r\n" +
                "Host" + ": foo.com\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding: gzip, deflate, sdch\n" +
                "Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4,uk;q=0.2\n" +
                "Upgrade-Insecure-Requests: \n" +
                "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36" +
                "Cookie: JSESSION=123456789";
        request = new HttpRequest(requestStr, "", "");
    }

    @Test
    public void processServletRedirectToJsp() throws ClassNotFoundException {
        ServletHandler servletHandler = new ServletHandlerImpl();
        ClassLoader classLoader = new URLClassLoader(new URL[]{getClass().getResource("/www/Servlets_demo-master/WEB-INF/classes/")});
        Class testServlet = Class.forName("sumy.javacourse.webdemo.controller.Main", false, classLoader);
        String actual = new String(servletHandler.
                processServlet(request, testServlet).getBody());
        assertEquals("", actual);
    }

    @Test
    public void processJsp() throws URISyntaxException {
        JspHandler jspHandler = new JspHandlerImpl();
        File jspPage = new File(getClass().getResource("/www/Servlets_demo-master/jsp/test.jsp").toURI());
        String expected = "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Test JSP</title>\n" +
                "</head>\n" +
                "<h1>Hello JSP</h1>\n" +
                "<body>\n" +
                "Test JSP #%s.\n" +
                "</body>\n" +
                "</html>\n" +
                "\n";
        String actual = new String(jspHandler.
                processJSP(request, jspPage).getBody());
        assertEquals(format(expected, 1), actual.replace("\r", ""));
        actual = new String(jspHandler.
                processJSP(request, jspPage).getBody());
        assertEquals(format(expected, 2), actual.replace("\r", ""));
    }
}
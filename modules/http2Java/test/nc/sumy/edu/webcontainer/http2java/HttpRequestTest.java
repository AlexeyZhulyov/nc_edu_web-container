package nc.sumy.edu.webcontainer.http2java;

import nc.sumy.edu.webcontainer.http2Java.HttpMethod;
import nc.sumy.edu.webcontainer.http2Java.HttpRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
    private HttpRequest request;
    private String str;
    private static final String methodGet = "GET ";
    private static final String methodPost = "POST ";
    private static final String httpVersion = "HTTP/1.1";
    private static final String host = "Host";
    private static final String nonexistentVar = "qqq";
    private static final String userAgent = "User-Agent";
    private static final String accept = "Accept";
    private static final String connection = "Connection";
    private static final String close  = "close";
    private static final String connectionClose = "Connection: close";

    // Check valid http get request #1: with relative URI and without parameters
    @Test
    public void getRequest1() {
        str = methodGet + "/JavaPower.gif " + httpVersion + "\r\n" +
                host + ": www.devresource.org\r\n" +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURN(), "/JavaPower.gif");
        assertEquals(request.getHeader("Range-Unit"), "3388 | 1024");
        assertEquals(request.getHeader(nonexistentVar), "");
        assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #2: with relative URI and without parameters (more sophisticated)
    @Test
    public void getRequest2() {
        str = methodGet + "/wiki/page.html " + httpVersion + "\r\n" +
                host + ": ru.wikipedia.org\r\n" +
                userAgent + ": Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\r\n" +
                accept + ": text/html\r\n" +
                connectionClose;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURN(), "/wiki/page.html");
        assertEquals(request.getHeader(host), "ru.wikipedia.org");
        assertEquals(request.getHeader(userAgent),
                "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5");
        assertEquals(request.getHeader(accept), "text/html");
        assertEquals(request.getHeader(connection), close);
        assertEquals(request.getHeader(nonexistentVar), "");
        assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #3: with relative URI and with parameters
    @Test
    public void getRequest3() {
        str = methodGet + "/someservlet.jsp?param1=foo&param2=bar " + httpVersion + "\r\n" +
                host + ": foo.com\r\n" +
                userAgent + ": Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) " +
                "Gecko/2008050509 Google Chrome/3.0b5\r\n" +
                accept + ": text/jsp\r\n" +
                connectionClose;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURN(), "/someservlet.jsp");
        assertEquals(request.getHeader(host), "foo.com");
        assertEquals(request.getHeader(userAgent),
                "Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Google Chrome/3.0b5");
        assertEquals(request.getHeader(accept), "text/jsp");
        assertEquals(request.getHeader(connection), "close");
        assertEquals(request.getHeader(nonexistentVar), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter("param2"), "bar");
        assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #3: with absolute URI and with parameters
    @Test
    public void getRequest4() {
        str = methodGet + "http://foo.com/someservlet.jsp?param1=foo " + httpVersion + "\r\n" +
                accept + ": text/jsp\r\n" +
                connectionClose;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURN(), "/someservlet.jsp");
        assertEquals(request.getHeader(host), "foo.com");
        assertEquals(request.getHeader(accept), "text/jsp");
        assertEquals(request.getHeader(connection), close);
        assertEquals(request.getHeader(nonexistentVar), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http post request: with absolute URI and with parameters
    @Test
    public void postRequest() {
        str = methodPost + "http://www.site.ru/news.html " + httpVersion + "\r\n" +
                host + ": www.site.ru\r\n" +
                "Referer: http://www.site.ru/index.html\r\n" +
                "Cookie: income=1\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-Length: 35\r\n" +
                "login=Petya%20Vasechkin&password=qq\r\n";

        request = new HttpRequest(str);
        assertEquals(request.getMethod(), HttpMethod.POST);
        assertEquals(request.getURN(), "/news.html");
        assertEquals(request.getHeader(host), "www.site.ru");
        assertEquals(request.getHeader("Referer"), "http://www.site.ru/index.html");
        assertEquals(request.getHeader("Cookie"), "income=1");
        assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
        assertEquals(request.getHeader("Content-Length"), "35");
        assertEquals(request.getHeader(nonexistentVar), "");

        assertEquals(request.getParameter("login"), "Petya%20Vasechkin");
        assertEquals(request.getParameter("password"), "qq");
        assertEquals(request.getParameter(nonexistentVar), "");
    }
}

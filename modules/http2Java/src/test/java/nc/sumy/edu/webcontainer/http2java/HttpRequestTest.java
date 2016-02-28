package nc.sumy.edu.webcontainer.http2java;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {
    private HttpRequest request;
    private String str;
    private static final String ENDL = "\r\n";
    private static final String METHOD_GET = "GET ";
    private static final String METHOD_POST = "POST ";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String HOST = "Host";
    private static final String NONEXISTENT_VAR = "qqq";
    private static final String USER_AGENT = "User-Agent";
    private static final String ACCEPT = "Accept";
    private static final String CONNECTION = "Connection";
    private static final String CLOSE_STR = "close";
    private static final String CONNECTION_CLOSE = "Connection: close";

    // Check valid http get request #1: with relative URI and without parameters
    @Test
    public void getRequest1() {
        str = METHOD_GET + "/JavaPower.gif " + HTTP_VERSION + ENDL +
                HOST + ": www.devresource.org" + ENDL +
                ACCEPT + ": text/html" + ENDL +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getUrn(), "/JavaPower.gif");
        assertEquals(request.getHeader(ACCEPT), "text/html");
        assertEquals(request.getHeader("Range-Unit"), "3388 | 1024");
        assertEquals(request.getHeader(NONEXISTENT_VAR), "");
        assertEquals(request.getParameter(NONEXISTENT_VAR), "");
    }

    // Check valid http get request #2: with relative URI and without parameters (more sophisticated)
    @Test
    public void getRequest2() {
        str = METHOD_GET + "/wiki/page.html " + HTTP_VERSION + ENDL +
                HOST + ": ru.wikipedia.org" + ENDL +
                USER_AGENT + ": Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5" + ENDL +
                ACCEPT + ": text/html" + ENDL +
                CONNECTION_CLOSE;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getUrn(), "/wiki/page.html");
        assertEquals(request.getHeader(HOST), "ru.wikipedia.org");
        assertEquals(request.getHeader(USER_AGENT),
                "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5");
        assertEquals(request.getHeader(ACCEPT), "text/html");
        assertEquals(request.getHeader(CONNECTION), CLOSE_STR);
        assertEquals(request.getHeader(NONEXISTENT_VAR), "");
        assertEquals(request.getParameter(NONEXISTENT_VAR), "");
    }

    // Check valid http get request #3: with relative URI and with parameters
    @Test
    public void getRequest3() {
        str = METHOD_GET + "/someservlet.jsp?param1=foo&param2=bar " + HTTP_VERSION + ENDL +
                HOST + ": foo.com" + ENDL +
                USER_AGENT + ": Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) " +
                "Gecko/2008050509 Google Chrome/3.0b5" +  ENDL +
                ACCEPT + ": text/jsp" + ENDL +
                CONNECTION_CLOSE;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getUrn(), "/someservlet.jsp");
        assertEquals(request.getHeader(HOST), "foo.com");
        assertEquals(request.getHeader(USER_AGENT),
                "Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Google Chrome/3.0b5");
        assertEquals(request.getHeader(ACCEPT), "text/jsp");
        assertEquals(request.getHeader(CONNECTION), "close");
        assertEquals(request.getHeader(NONEXISTENT_VAR), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter("param2"), "bar");
        assertEquals(request.getParameter(NONEXISTENT_VAR), "");
    }

    // Check valid http get request #3: with absolute URI and with parameters
    @Test
    public void getRequest4() {
        str = METHOD_GET + "http://foo.com/someservlet.jsp?param1=foo " + HTTP_VERSION + ENDL +
                ACCEPT + ": text/jsp" + ENDL +
                CONNECTION_CLOSE;
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getUrn(), "/someservlet.jsp");
        assertEquals(request.getHeader(HOST), "foo.com");
        assertEquals(request.getHeader(ACCEPT), "text/jsp");
        assertEquals(request.getHeader(CONNECTION), CLOSE_STR);
        assertEquals(request.getHeader(NONEXISTENT_VAR), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter(NONEXISTENT_VAR), "");
    }

    // Check valid http post request: with absolute URI and with parameters
    @Test
    public void postRequest() {
        str = METHOD_POST + "http://www.site.ru/news.html " + HTTP_VERSION + ENDL +
                HOST + ": www.site.ru" + ENDL +
                "Referer: http://www.site.ru/index.html" + ENDL +
                "Cookie: income=1" +  ENDL +
                "Content-Type: application/x-www-form-urlencoded" + ENDL +
                "Content-Length: 35" + ENDL +
                "login=Petya%20Vasechkin&password=qq";

        request = new HttpRequest(str);
        assertEquals(request.getMethod(), HttpMethod.POST);
        assertEquals(request.getUrn(), "/news.html");
        assertEquals(request.getHeader(HOST), "www.site.ru");
        assertEquals(request.getHeader("Referer"), "http://www.site.ru/index.html");
        assertEquals(request.getHeader("Cookie"), "income=1");
        assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
        assertEquals(request.getHeader("Content-Length"), "35");
        assertEquals(request.getHeader(NONEXISTENT_VAR), "");

        assertEquals(request.getParameter("login"), "Petya%20Vasechkin");
        assertEquals(request.getParameter("password"), "qq");
        assertEquals(request.getParameter(NONEXISTENT_VAR), "");
    }
}

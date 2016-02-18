import org.junit.Assert;
import org.junit.Test;

public class HttpRequestTest extends Assert{
    private HttpRequest request;
    private String str;

    // Check valid http get request #1: with relative URI and without parameters
    @Test
    public void testGetRequest1() {
        str = "GET /JavaPower.gif HTTP/1.1\n" +
                "Host: www.devresource.org\n" +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURI(), "/JavaPower.gif");
        assertEquals(request.getHeader("Range-Unit"), "3388 | 1024");
        assertEquals(request.getHeader("qqq"), "");
        assertEquals(request.getParameter("qq"), "");
    }

    // Check valid http get request #2: with relative URI and without parameters (more sophisticated)
    @Test
    public void testGetRequest2() {
        str = "GET /wiki/page.html HTTP/1.1\n" +
                "Host: ru.wikipedia.org\n" +
                "User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
                "Accept: text/html\n" +
                "Connection: close";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURI(), "/wiki/page.html");
        assertEquals(request.getHeader("Host"), "ru.wikipedia.org");
        assertEquals(request.getHeader("User-Agent"),
                "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5");
        assertEquals(request.getHeader("Accept"), "text/html");
        assertEquals(request.getHeader("Connection"), "close");
        assertEquals(request.getHeader("qqq"), "");
        assertEquals(request.getParameter("qq"), "");
    }

    // Check valid http get request #3: with relative URI and with parameters
    @Test
    public void testGetRequest3() {
        str = "GET /someservlet.jsp?param1=foo&param2=bar HTTP/1.1\n" +
                "Host: foo.com\n" +
                "User-Agent: Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) " +
                "Gecko/2008050509 Google Chrome/3.0b5\n" +
                "Accept: text/jsp\n" +
                "Connection: close";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURI(), "/someservlet.jsp");
        assertEquals(request.getHeader("Host"), "foo.com");
        assertEquals(request.getHeader("User-Agent"),
                "Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Google Chrome/3.0b5");
        assertEquals(request.getHeader("Accept"), "text/jsp");
        assertEquals(request.getHeader("Connection"), "close");
        assertEquals(request.getHeader("qqq"), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter("param2"), "bar");
        assertEquals(request.getParameter("qq"), "");
    }

    // Check valid http get request #3: with absolute URI and with parameters
    @Test
    public void testGetRequest4() {
        str = "GET http://foo.com/someservlet.jsp?param1=foo HTTP/1.1\n" +
                "Accept: text/jsp\n" +
                "Connection: close";
        request = new HttpRequest(str);

        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getURI(), "/someservlet.jsp");
        assertEquals(request.getHeader("Host"), "foo.com");
        assertEquals(request.getHeader("Accept"), "text/jsp");
        assertEquals(request.getHeader("Connection"), "close");
        assertEquals(request.getHeader("qqq"), "");

        assertEquals(request.getParameter("param1"), "foo");
        assertEquals(request.getParameter("qq"), "");
    }

    // Check valid http post request: with absolute URI and with parameters
    @Test
    public void testPostRequest() {
        str = "POST http://www.site.ru/news.html HTTP/1.0\n" +
                "Host: www.site.ru\n" +
                "Referer: http://www.site.ru/index.html\n" +
                "Cookie: income=1\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 35\n" +
                "login=Petya%20Vasechkin&password=qq\n";

        request = new HttpRequest(str);
        assertEquals(request.getMethod(), HttpMethod.POST);
        assertEquals(request.getURI(), "/news.html");
        assertEquals(request.getHeader("Host"), "www.site.ru");
        assertEquals(request.getHeader("Referer"), "http://www.site.ru/index.html");
        assertEquals(request.getHeader("Cookie"), "income=1");
        assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
        assertEquals(request.getHeader("Content-Length"), "35");
        assertEquals(request.getHeader("qqq"), "");

        assertEquals(request.getParameter("login"), "Petya%20Vasechkin");
        assertEquals(request.getParameter("password"), "qq");
        assertEquals(request.getParameter("qq"), "");
    }
}
package nc.sumy.edu.webcontainer.http2java;

import nc.sumy.edu.webcontainer.http2Java.HttpMethod;
import nc.sumy.edu.webcontainer.http2Java.HttpRequest;
import org.junit.Assert;
import org.junit.Test;

public class HttpRequestTest {
    private HttpRequest request;
    private String str;
    private String methodGet = "GET ";
    private String methodPost = "POST ";
    private String httpVersion = "HTTP/1.1";
    private String host = "Host";
    private String nonexistentVar = "qqq";
    private String userAgent = "User-Agent";
    private String accept = "Accept";
    private String connection = "Connection";
    private String close  = "close";
    private String connectionClose = "Connection: close";

    // Check valid http get request #1: with relative URI and without parameters
    @Test
    public void testGetRequest1() {
        str = methodGet + "/JavaPower.gif " + httpVersion + "\n" +
                host + ": www.devresource.org\n" +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(str);

        Assert.assertEquals(request.getMethod(), HttpMethod.GET);
        Assert.assertEquals(request.getURN(), "/JavaPower.gif");
        Assert.assertEquals(request.getHeader("Range-Unit"), "3388 | 1024");
        Assert.assertEquals(request.getHeader(nonexistentVar), "");
        Assert.assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #2: with relative URI and without parameters (more sophisticated)
    @Test
    public void testGetRequest2() {
        str = methodGet + "/wiki/page.html " + httpVersion + "\n" +
                host + ": ru.wikipedia.org\n" +
                userAgent + ": Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
                accept + ": text/html\n" +
                connectionClose;
        request = new HttpRequest(str);

        Assert.assertEquals(request.getMethod(), HttpMethod.GET);
        Assert.assertEquals(request.getURN(), "/wiki/page.html");
        Assert.assertEquals(request.getHeader(host), "ru.wikipedia.org");
        Assert.assertEquals(request.getHeader(userAgent),
                "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5");
        Assert.assertEquals(request.getHeader(accept), "text/html");
        Assert.assertEquals(request.getHeader(connection), close);
        Assert.assertEquals(request.getHeader(nonexistentVar), "");
        Assert.assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #3: with relative URI and with parameters
    @Test
    public void testGetRequest3() {
        str = methodGet + "/someservlet.jsp?param1=foo&param2=bar " + httpVersion + "\n" +
                host + ": foo.com\n" +
                userAgent + ": Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) " +
                "Gecko/2008050509 Google Chrome/3.0b5\n" +
                accept + ": text/jsp\n" +
                connectionClose;
        request = new HttpRequest(str);

        Assert.assertEquals(request.getMethod(), HttpMethod.GET);
        Assert.assertEquals(request.getURN(), "/someservlet.jsp");
        Assert.assertEquals(request.getHeader(host), "foo.com");
        Assert.assertEquals(request.getHeader(userAgent),
                "Google Chrome/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Google Chrome/3.0b5");
        Assert.assertEquals(request.getHeader(accept), "text/jsp");
        Assert.assertEquals(request.getHeader(connection), "close");
        Assert. assertEquals(request.getHeader(nonexistentVar), "");

        Assert.assertEquals(request.getParameter("param1"), "foo");
        Assert.assertEquals(request.getParameter("param2"), "bar");
        Assert.assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http get request #3: with absolute URI and with parameters
    @Test
    public void testGetRequest4() {
        str = methodGet + "http://foo.com/someservlet.jsp?param1=foo " + httpVersion + "\n" +
                accept + ": text/jsp\n" +
                connectionClose;
        request = new HttpRequest(str);

        Assert.assertEquals(request.getMethod(), HttpMethod.GET);
        Assert.assertEquals(request.getURN(), "/someservlet.jsp");
        Assert.assertEquals(request.getHeader(host), "foo.com");
        Assert.assertEquals(request.getHeader(accept), "text/jsp");
        Assert.assertEquals(request.getHeader(connection), close);
        Assert.assertEquals(request.getHeader(nonexistentVar), "");

        Assert.assertEquals(request.getParameter("param1"), "foo");
        Assert.assertEquals(request.getParameter(nonexistentVar), "");
    }

    // Check valid http post request: with absolute URI and with parameters
    @Test
    public void testPostRequest() {
        str = methodPost + "http://www.site.ru/news.html " + httpVersion + "\n" +
                host + ": www.site.ru\n" +
                "Referer: http://www.site.ru/index.html\n" +
                "Cookie: income=1\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: 35\n" +
                "login=Petya%20Vasechkin&password=qq\n";

        request = new HttpRequest(str);
        Assert.assertEquals(request.getMethod(), HttpMethod.POST);
        Assert.assertEquals(request.getURN(), "/news.html");
        Assert.assertEquals(request.getHeader(host), "www.site.ru");
        Assert.assertEquals(request.getHeader("Referer"), "http://www.site.ru/index.html");
        Assert.assertEquals(request.getHeader("Cookie"), "income=1");
        Assert.assertEquals(request.getHeader("Content-Type"), "application/x-www-form-urlencoded");
        Assert.assertEquals(request.getHeader("Content-Length"), "35");
        Assert.assertEquals(request.getHeader(nonexistentVar), "");

        Assert.assertEquals(request.getParameter("login"), "Petya%20Vasechkin");
        Assert.assertEquals(request.getParameter("password"), "qq");
        Assert.assertEquals(request.getParameter(nonexistentVar), "");
    }
}

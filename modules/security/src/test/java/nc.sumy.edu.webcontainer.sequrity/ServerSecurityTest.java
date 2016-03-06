package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSecurityTest {
    HttpRequest request;

    /*-----------  ORDER - ALLOW is first  -----------*/

    // test with config that has files includes
    @Test
    public void securityTest1() {
        request = new HttpRequest("GET src/test/resources/test-site1/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "51.81.47.51", "nc.com");
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = new HttpRequest("GET src/test/resources/test-site1/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "93.49.37.56", "nc.com");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);
    }

    // test with config that has not files includes
    @Test
    public void securityTest2() {
        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                        "Accept: text/html\r\n", "217.20.147.94", "nc.com");
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "217.20.147.785", "maxim.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "51.81.47.51", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "51.81.47.51", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "11.11.111.66", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "11.22.22.66", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "33.33.33.99", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "33.33.33.4", "qqq.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = new HttpRequest("GET src/test/resources/test-site2/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "217.20.147.94", "nc.com");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);
    }

    /*-----------  ORDER - DENY is first  -----------*/

    // test with config that has files includes
    /*@Test
    public void securityTest3() {
        request = new HttpRequest("GET src/test/resources/test-site3/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "51.81.47.51", "nc.com");
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = new HttpRequest("GET src/test/resources/test-site3/index.html HTTP/1.1\r\n" +
                "Accept: text/html\r\n", "93.49.37.56", "nc.com");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);
    }*/

}

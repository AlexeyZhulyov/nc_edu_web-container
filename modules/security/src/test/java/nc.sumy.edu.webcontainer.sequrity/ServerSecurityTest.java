package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSecurityTest {
    HttpRequest request;

    @Test
    public void securityTest1() {
        request = new HttpRequest("GET src/test/resources/test-site1/index.html HTTP/1.1\r\n" +
                        "Accept: text/html\r\n", "217.20.147.94", "nc.com");
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);
    }
}

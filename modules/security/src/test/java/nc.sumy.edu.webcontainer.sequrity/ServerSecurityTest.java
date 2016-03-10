package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSecurityTest {
    private HttpRequest request;
    private static final String NONEXISTENT_IP = "qqq.pc";
    private static final String NC_COM = "nc.com";

    @SuppressWarnings("PMD")
    private static final String IP_VAR1 = "51.81.47.51";
    @SuppressWarnings("PMD")
    private static final String IP_VAR2 = "217.20.147.94";

    @SuppressWarnings("PMD")
    private HttpRequest makeRequest(int number, String ipString, String hostString) {
        String requestString = "GET src/test/resources/test-site"+ Integer.toString(number) +
                "/index.html HTTP/1.1\r\nAccept: text/html\r\n";
        return new HttpRequest(requestString, ipString, hostString);
    }

    /*-----------  ORDER - ALLOW is first  -----------*/

    // test with config that has files includes
    @SuppressWarnings("PMD")
    @Test
    public void securityTest1() {
        request = makeRequest(1, IP_VAR1, NC_COM);
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(1, "93.49.37.56", NC_COM);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);
    }

    // test with config that has not files includes
    @SuppressWarnings("PMD")
    @Test
    public void securityTest2() {
        request = makeRequest(2, IP_VAR2, NC_COM);
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(2, "217.20.147.785", "maxim.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(2, IP_VAR1, NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(2, IP_VAR1, NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(2, "11.11.111.66", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(2, "11.22.22.66", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(2, "33.33.33.99", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(2, "33.33.33.4", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(2, IP_VAR2, NC_COM);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);
    }

    /*-----------  ORDER - DENY is first  -----------*/

    // test with config that has files includes
    @SuppressWarnings("PMD")
    @Test
    public void securityTest3() {
        request = makeRequest(3, IP_VAR1, NC_COM);
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(3, "93.49.37.56", NC_COM);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);
    }

    // test with config that has not files includes
    @SuppressWarnings("PMD")
    @Test
    public void securityTest4() {
        request = makeRequest(4, IP_VAR2, NC_COM);
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(4, "217.20.147.785", "maxim.pc");
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(4, IP_VAR1, NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(4, IP_VAR1, NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(4, "11.11.111.66", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(4, "11.22.22.66", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(4, "33.33.33.99", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);

        request = makeRequest(4, "33.33.33.4", NONEXISTENT_IP);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);

        request = makeRequest(4, IP_VAR2, NC_COM);
        serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), true);
    }

    // test with "all" directive in "allow"-section
    @SuppressWarnings("PMD")
    @Test
    public void securityTest5() {
        request = makeRequest(5, IP_VAR1, NC_COM);
        ServerSecurity serverSecurity = new ServerSecurity(request);
        assertEquals(serverSecurity.isAllow(), false);
    }
}

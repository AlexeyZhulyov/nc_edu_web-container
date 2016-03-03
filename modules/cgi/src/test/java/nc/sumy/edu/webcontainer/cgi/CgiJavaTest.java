package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CgiJavaTest {
    CgiJava cgiJava = null;
    nc.sumy.edu.webcontainer.cgi.Test testClass = null;
    Request request = null;
    String processResult = null;
    final static String CLASS_NAME = "Test";
    final static String EXPECT_EXCEPTION = "Expected an CgiException to be thrown";

    @Before
    public void setUp() {
        testClass = new nc.sumy.edu.webcontainer.cgi.Test();
        cgiJava = new CgiJava();
        cgiJava.setEnvironmentVariable("REQUEST_METHOD", "POST");
        cgiJava.setEnvironmentVariable("SCRIPT_NAME", CLASS_NAME);

        String queryString = "login=Petya%20Vasechkin&password=qq";
        String requestStr = "GET " + "/" + CLASS_NAME + ".cgi?" + queryString + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";

        request = new HttpRequest(requestStr);

        processResult = "Content-type: text/html\n\n" +
                "<html>\n" + "<head>\n" + "<title>\n" + "Hello There " + "Petya Vasechkin" + "!" +
                "\n" + "</title>\n" + "</head>\n" + "<body>\n" + "<h1 align=center>Hello There " + "Petya Vasechkin" +
                "!</h1>" + "</body>\n</html>\n";
    }

    @Test
    public void testSetEnvironmentVariable() {
        assertEquals("POST", System.getProperty("REQUEST_METHOD"));
        assertEquals(CLASS_NAME, System.getProperty("SCRIPT_NAME"));
    }

    @Test
    public void testProcess() {
        assertEquals(processResult, cgiJava.process(CLASS_NAME, request.getParameters()));
    }

    @Test
    public void testSearchClass() {
        assertEquals(testClass.getClass(), cgiJava.searchClass("Test"));
    }

    @Test(expected = CgiException.class)
    public void testSearchClassException() {
        cgiJava.searchClass("Absent");
    }

    @Test
    public void testSearchClassExceptionMessage() {
        try {
            cgiJava.searchClass("Absent");
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals("Class \"Absent\" not found", e.getMessage());
        }
    }

    @Test
    public void testInvokeGenerateMethodExceptionMessage1() {
        try {
            cgiJava.process("TestWithoutGenerate", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals("Method \"generate\" not found", e.getMessage());
        }
    }

    @Test
    public void testInvokeGenerateMethodExceptionMessage2() {
        try {
            cgiJava.process("TestWithPrivateConstructor", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals("No access", e.getMessage());
        }
    }

    @Test
    public void testInvokeGenerateMethodExceptionMessage3() {
        try {
            cgiJava.process("AbstractTest", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals("Cannot create new instance", e.getMessage());
        }
    }
}
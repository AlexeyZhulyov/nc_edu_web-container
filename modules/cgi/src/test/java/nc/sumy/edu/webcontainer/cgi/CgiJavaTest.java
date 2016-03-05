package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CgiJavaTest {
    private CgiJava cgiJava = null;
    private nc.sumy.edu.webcontainer.cgi.Test testClass = null;
    private Request request = null;
    private String processResult = null;
    private final static String CLASS_NAME_TEST = "Test";
    private final static String EXPECT_EXCEPTION = "Expected an CgiException to be thrown";

    @Before
    public void setUp() {
        testClass = new nc.sumy.edu.webcontainer.cgi.Test();
        cgiJava = new CgiJava();
        cgiJava.setEnvironmentVariable("REQUEST_METHOD", "POST");
        cgiJava.setEnvironmentVariable("SCRIPT_NAME", CLASS_NAME_TEST);

        String queryString = "login=Petya%20Vasechkin&password=qq";
        String requestStr = "GET " + "/" + CLASS_NAME_TEST + ".cgi?" + queryString + " HTTP/1.1" + "\r\n" +
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
    public void setEnvironmentVariable() {
        assertEquals("POST", System.getProperty("REQUEST_METHOD"));
        assertEquals(CLASS_NAME_TEST, System.getProperty("SCRIPT_NAME"));
    }

    @Test
    public void process() {
        assertEquals(processResult, cgiJava.process(CLASS_NAME_TEST, request.getParameters()));
    }

    @Test
    public void searchClass() {
        assertEquals(testClass.getClass(), cgiJava.searchClass("Test"));
    }

    @Test(expected = CgiException.class)
    public void searchClassException() {
        cgiJava.searchClass("Absent");
    }

    @Test
    public void searchClassExceptionMessage() {
        try {
            cgiJava.searchClass("Absent");
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals(String.format(CgiException.MESSAGE_CLASS_NOT_FOUND, "Absent"), e.getMessage());
        }
    }

    @Test
    public void invokeGenerateMethodExceptionMessage1() {
        try {
            cgiJava.process("TestWithoutGenerate", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals(String.format(CgiException.MESSAGE_CANNOT_INVOKE_METHOD,"generate"), e.getMessage());
        }
    }

    @Test
    public void invokeGenerateMethodExceptionMessage2() {
        try {
            cgiJava.process("TestWithPrivateConstructor", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals(String.format(CgiException.MESSAGE_CANNOT_INVOKE_METHOD,"generate"), e.getMessage());
        }
    }

    @Test
    public void invokeGenerateMethodExceptionMessage3() {
        try {
            cgiJava.process("AbstractTest", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals(CgiException.MESSAGE_CANNOT_CREATE_INSTANCE, e.getMessage());
        }
    }
}
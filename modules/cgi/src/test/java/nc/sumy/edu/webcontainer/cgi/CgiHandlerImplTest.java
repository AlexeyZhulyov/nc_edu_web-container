package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;

import static java.lang.String.*;
import static nc.sumy.edu.webcontainer.cgi.CgiException.*;
import static org.junit.Assert.*;

public class CgiHandlerImplTest {
    private CgiHandlerImpl cgiHandlerImpl = null;
    private static final String HOST = "nc.pc";
    private static final String IP_ADDRESS = "";
    private final static String CLASS_NAME_TEST = "Test";
    private final static String EXPECT_EXCEPTION = "Expected an CgiException to be thrown";

    @Before
    public void setUp() {
        cgiHandlerImpl = new CgiHandlerImpl();
    }

    @Test
    public void process() {
        String queryString = "login=Petya%20Vasechkin&password=qq";
        String requestStr = "GET " + "/" + CLASS_NAME_TEST + ".cgi?" + queryString + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        Request request = new HttpRequest(requestStr, IP_ADDRESS, HOST);
        String processResult = "Content-type: text/html\n\n" +
                "<html>\n" + "<head>\n" + "<title>\n" + "Hello There " + "Petya Vasechkin" + "!" +
                "\n" + "</title>\n" + "</head>\n" + "<body>\n" + "<h1 align=center>Hello There " + "Petya Vasechkin" +
                "!</h1>" + "</body>\n</html>\n";
        assertEquals(processResult, cgiHandlerImpl.process(CLASS_NAME_TEST, request.getParameters()));
    }

    @Test
    public void find() {
        CgiAction testClass = new nc.sumy.edu.webcontainer.cgi.stub.Test();
        assertEquals(testClass.getClass(), cgiHandlerImpl.find("Test"));
    }

    @Test(expected = CgiException.class)
    public void findException() {
        cgiHandlerImpl.find("Absent");
    }

    @Test
    public void findExceptionMessage() {
        try {
            cgiHandlerImpl.find("Absent");
            fail(EXPECT_EXCEPTION);
        } catch (CgiClassNotFoundException e) {
            assertEquals(format(CLASS_NOT_FOUND, "Absent"), e.getMessage());
        }
    }

    @Test
    public void runExceptionMessage1() {
        try {
            cgiHandlerImpl.process("TestWithoutRun", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiInvalidClassException e) {
            assertEquals(format(INVALID_CLASS, "TestWithoutRun"), e.getMessage());
        }
    }

    @Test
    public void runExceptionMessage2() {
        try {
            cgiHandlerImpl.process("TestWithPrivateConstructor", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiCannotInvokeMethodException e) {
            assertEquals(format(CANNOT_INVOKE_METHOD, "run"), e.getMessage());
        }
    }

    @Test
    public void runExceptionMessage3() {
        try {
            cgiHandlerImpl.process("AbstractTest", new HashMap<>());
            fail(EXPECT_EXCEPTION);
        } catch (CgiException e) {
            assertEquals(CANNOT_CREATE_INSTANCE, e.getMessage());
        }
    }
}
package nc.sumy.edu.webcontainer.cgi;

import org.junit.Rule;
import org.junit.rules.ExpectedException;


public class CgiExceptionTest {
    String message = "Some message";

    @Rule
    public ExpectedException cgiException = ExpectedException.none();

    @org.junit.Test
    public void testExpectedException1() {
        cgiException.expect(CgiException.class);
        throw new CgiException();
    }

    @org.junit.Test
    public void testExpectedException2() {
        cgiException.expect(CgiException.class);
        cgiException.expectMessage(message);
        throw new CgiException(message);
    }

    @org.junit.Test
    public void testExpectedException3() {
        cgiException.expect(CgiException.class);
        cgiException.expectMessage(message);
        throw new CgiException(message, new IllegalArgumentException());
    }

    @org.junit.Test
    public void testExpectedException4() {
        cgiException.expect(CgiException.class);
        throw new CgiException(new IllegalArgumentException());
    }
}
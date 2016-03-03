package nc.sumy.edu.webcontainer.cgi;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class CgiExceptionTest {
    final static String MESSAGE = "Some message";

    @Rule
    public ExpectedException cgiException = ExpectedException.none();

    @Test
    public void testExpectedException1() {
        cgiException.expect(CgiException.class);
        throw new CgiException();
    }

    @Test
    public void testExpectedException2() {
        cgiException.expect(CgiException.class);
        cgiException.expectMessage(MESSAGE);
        throw new CgiException(MESSAGE);
    }

    @Test
    public void testExpectedException3() {
        cgiException.expect(CgiException.class);
        cgiException.expectMessage(MESSAGE);
        throw new CgiException(MESSAGE, new IllegalArgumentException());
    }

    @Test
    public void testExpectedException4() {
        cgiException.expect(CgiException.class);
        throw new CgiException(new IllegalArgumentException());
    }
}
package nc.sumy.edu.webcontainer.http;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParameterEncodingExceptionTest {
    public static final String EXCEPTION_TEXT = "qqq";
    @Rule
    public ExpectedException encodingException = ExpectedException.none();

    @Test
    public void exceptionTest1() {
        encodingException.expect(ParameterEncodingException.class);
        throw new ParameterEncodingException();
    }

    @Test
    public void exceptionTest2() {
        encodingException.expect(ParameterEncodingException.class);
        encodingException.expectMessage(EXCEPTION_TEXT);
        throw new ParameterEncodingException(EXCEPTION_TEXT);
    }

    @Test
    public void exceptionTest3() {
        encodingException.expect(ParameterEncodingException.class);
        encodingException.expectMessage(EXCEPTION_TEXT);
        throw new ParameterEncodingException(EXCEPTION_TEXT, new IllegalArgumentException());
    }

    @Test
    public void exceptionTest4() {
        encodingException.expect(ParameterEncodingException.class);
        throw new ParameterEncodingException(new IllegalArgumentException());
    }
}

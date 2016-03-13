package nc.sumy.edu.webcontainer.cgi;

import static java.lang.String.format;

public class CgiCannotInvokeMethodException extends CgiException {
    public CgiCannotInvokeMethodException(String methodName, Throwable cause) {
        super(format(CANNOT_INVOKE_METHOD, methodName), cause);
    }
}

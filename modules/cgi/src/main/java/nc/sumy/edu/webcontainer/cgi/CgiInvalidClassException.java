package nc.sumy.edu.webcontainer.cgi;

import static java.lang.String.format;

public class CgiInvalidClassException extends CgiException {
    public CgiInvalidClassException(String className) {
        super(format(INVALID_CLASS, className));
    }
}

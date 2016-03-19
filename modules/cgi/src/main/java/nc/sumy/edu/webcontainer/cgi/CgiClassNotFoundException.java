package nc.sumy.edu.webcontainer.cgi;

import static java.lang.String.format;

public class CgiClassNotFoundException extends CgiException  {
    public CgiClassNotFoundException(String className) {
        super(format(CLASS_NOT_FOUND, className));
    }
}

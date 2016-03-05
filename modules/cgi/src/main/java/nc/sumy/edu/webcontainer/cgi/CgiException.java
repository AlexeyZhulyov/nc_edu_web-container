package nc.sumy.edu.webcontainer.cgi;

public class CgiException extends RuntimeException {

    public static final String CLASS_NOT_FOUND = "Class \"%s\" not found";
    public static final String CANNOT_INVOKE_METHOD = "Cannot invoke method \"%s\"";
    public static final String CANNOT_CREATE_INSTANCE = "Cannot create new instance";

    public CgiException(String message) {
        super(message);
    }

    public CgiException(String message, Throwable cause) {
        super(message, cause);
    }
}

package nc.sumy.edu.webcontainer.web;

public class WebException extends RuntimeException {

    public static final String CANNOT_FIND_READ_FILE = "Cannot find or read file \"%s\"";

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }
}

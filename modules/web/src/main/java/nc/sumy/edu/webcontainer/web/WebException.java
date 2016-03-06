package nc.sumy.edu.webcontainer.web;

public class WebException extends RuntimeException {

    public static final String CANNOT_FIND_FILE = "Cannot find file \"%s\"";
    public static final String CANNOT_READ_FILE = "Cannot read file \"%s\"";

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }
}

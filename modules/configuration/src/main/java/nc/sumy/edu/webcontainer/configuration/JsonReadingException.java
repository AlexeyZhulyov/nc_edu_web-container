package nc.sumy.edu.webcontainer.configuration;

/**
 * Exception that is thrown during work with server configuration
 */
public class JsonReadingException extends RuntimeException {
    public JsonReadingException(String message, Exception initException) {
        super(message, initException);
    }

    public JsonReadingException(String message) {
        super(message);
    }
}

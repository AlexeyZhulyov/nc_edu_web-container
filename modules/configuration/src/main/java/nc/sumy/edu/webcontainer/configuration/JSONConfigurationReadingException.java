package nc.sumy.edu.webcontainer.configuration;

public class JSONConfigurationReadingException extends RuntimeException {
    public JSONConfigurationReadingException(String message, Exception initException) {
        super(message, initException);
    }

    public JSONConfigurationReadingException(String message) {
        super(message);
    }
}

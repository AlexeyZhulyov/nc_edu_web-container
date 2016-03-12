package nc.sumy.edu.webcontainer.configuration;

public class ServerConfigurationJsonReadingException extends RuntimeException {
    public ServerConfigurationJsonReadingException(String message, Exception initException) {
        super(message, initException);
    }

    public ServerConfigurationJsonReadingException(String message) {
        super(message);
    }
}

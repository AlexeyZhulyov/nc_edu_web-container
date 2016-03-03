package nc.sumy.edu.webcontainer.http;

public class ParameterEncodingException extends RuntimeException {

    public ParameterEncodingException() {
        super();
    }

    public ParameterEncodingException(String message) {
        super(message);
    }

    public ParameterEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterEncodingException(Throwable cause) {
        super(cause);
    }
}

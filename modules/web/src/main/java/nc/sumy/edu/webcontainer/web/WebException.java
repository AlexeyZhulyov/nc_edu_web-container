package nc.sumy.edu.webcontainer.web;

public class WebException extends RuntimeException {

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }
}

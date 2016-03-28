package nc.sumy.edu.webcontainer.web;

/**
* General exception for web module.
*/
public class WebException extends RuntimeException {

    public WebException(String message, Throwable cause) {
        super(message, cause);
    }
}

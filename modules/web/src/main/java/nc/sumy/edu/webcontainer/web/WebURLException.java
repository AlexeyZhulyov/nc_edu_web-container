package nc.sumy.edu.webcontainer.web;

public class WebURLException extends WebException{
    public WebURLException(String file, Throwable cause) {
        super("Malformed url with file: " + file, cause);
    }
}

package nc.sumy.edu.webcontainer.web;

public class ServletServiceException extends WebException {
    public ServletServiceException(String className, Throwable cause) {
        super("Cannot do method Service in servlet:" + className, cause);
    }
}

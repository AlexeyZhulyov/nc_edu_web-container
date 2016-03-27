package nc.sumy.edu.webcontainer.web;

public class ServletInitException extends WebException{
    public ServletInitException(String className, Throwable cause) {
        super("Cannot do init() : " + className, cause);
    }
}

package nc.sumy.edu.webcontainer.web;

public class JspClassNotFoundException extends WebException {
    public JspClassNotFoundException(String className, Throwable cause) {
        super("Class jsp not found: " + className, cause);
    }
}

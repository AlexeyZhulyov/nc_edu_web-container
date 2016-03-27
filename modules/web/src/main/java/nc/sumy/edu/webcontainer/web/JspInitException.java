package nc.sumy.edu.webcontainer.web;

public class JspInitException extends WebException {
    public JspInitException(String jspName, Throwable cause) {
        super("Cannot do method init in jsp:" + jspName, cause);
    }
}

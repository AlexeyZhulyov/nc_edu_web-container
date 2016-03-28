package nc.sumy.edu.webcontainer.web;

public class JspServiceException extends WebException {
    public JspServiceException(String jspName, Throwable cause) {
        super("Cannot do method _jspService in jsp:" + jspName, cause);
    }
}

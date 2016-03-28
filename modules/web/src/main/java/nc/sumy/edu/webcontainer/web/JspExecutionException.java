package nc.sumy.edu.webcontainer.web;

public class JspExecutionException extends WebException {
    public JspExecutionException(String dir, Throwable cause) {
        super("Cannot execute jsp from: " + dir, cause);
    }
}

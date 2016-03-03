package nc.sumy.edu.webcontainer.cgi;

public class CgiException extends RuntimeException {

    public CgiException(String message) {
        super(message);
    }

    public CgiException(String message, Throwable cause) {
        super(message, cause);
    }
}

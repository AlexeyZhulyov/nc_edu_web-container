package nc.sumy.edu.webcontainer.server;

/**
 * Class represents exception that is thrown in Server class when server fails
 */
public class ServerFailException extends RuntimeException{

    public ServerFailException(String message) {
        super(message);
    }

    public ServerFailException(String message, Throwable cause) {
        super(message, cause);
    }
}

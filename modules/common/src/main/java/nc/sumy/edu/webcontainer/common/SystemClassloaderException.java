package nc.sumy.edu.webcontainer.common;

public class SystemClassloaderException extends RuntimeException {

    public SystemClassloaderException(Throwable cause) {
        super("Error, could not add URL to system classloader", cause);
    }
}

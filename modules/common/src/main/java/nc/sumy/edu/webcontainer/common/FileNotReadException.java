package nc.sumy.edu.webcontainer.common;

public class FileNotReadException extends RuntimeException {
    public FileNotReadException(String fileName, Throwable cause) {
        super("Unable to read the file " + fileName, cause);
    }
}

package nc.sumy.edu.webcontainer.common;

import java.io.File;

public class FileNotReadException extends RuntimeException {
    public FileNotReadException(File file, Throwable cause) {
        super("Unable to read the file " + file, cause);
    }
}

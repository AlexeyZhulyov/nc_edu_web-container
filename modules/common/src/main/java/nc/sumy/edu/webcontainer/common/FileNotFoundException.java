package nc.sumy.edu.webcontainer.common;

import static java.lang.String.format;

public class FileNotFoundException extends RuntimeException {

    private static final String MESSAGE = "\"Unable to find the file: %s";

    public FileNotFoundException(String fileName) {
        super(format(MESSAGE, fileName));
    }
}
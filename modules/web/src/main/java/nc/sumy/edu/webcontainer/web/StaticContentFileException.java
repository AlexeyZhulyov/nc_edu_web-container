package nc.sumy.edu.webcontainer.web;

import static java.lang.String.format;

public class StaticContentFileException extends WebException {

    public static final String CANNOT_FIND_READ_FILE = "Cannot find or read file \"%s\"";

    public StaticContentFileException(String fileName, Throwable cause) {
        super(format(CANNOT_FIND_READ_FILE, fileName), cause);
    }
}

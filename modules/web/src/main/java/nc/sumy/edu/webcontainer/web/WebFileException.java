package nc.sumy.edu.webcontainer.web;

import static java.lang.String.format;

public class WebFileException extends WebException {
    public WebFileException(String fileName, Throwable cause) {
        super(format(CANNOT_FIND_READ_FILE, fileName), cause);
    }
}

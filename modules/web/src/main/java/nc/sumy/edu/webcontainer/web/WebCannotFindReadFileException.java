package nc.sumy.edu.webcontainer.web;

import static java.lang.String.format;

public class WebCannotFindReadFileException extends WebException {
    public WebCannotFindReadFileException(String fileName, Throwable cause) {
        super(format(CANNOT_FIND_READ_FILE, fileName), cause);
    }
}

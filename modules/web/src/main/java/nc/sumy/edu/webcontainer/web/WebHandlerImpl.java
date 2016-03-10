package nc.sumy.edu.webcontainer.web;

import org.apache.commons.io.FileUtils;

import java.io.*;

import static java.lang.String.format;
import static nc.sumy.edu.webcontainer.web.WebException.*;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        try {
            return FileUtils.readFileToString(page);
        } catch (IOException e) {
            throw new WebException(format(CANNOT_FIND_READ_FILE, page.getName()), e);
        }
    }
}

package nc.sumy.edu.webcontainer.web;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        try {
            return FileUtils.readFileToString(page);
        } catch (IOException e) {
            throw new WebCannotFindReadFileException(page.getName(), e);
        }
    }
}

package nc.sumy.edu.webcontainer.web;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class StaticContentHandlerImpl implements StaticContentHandler {

    public byte[] process(File page) {
        try {
           return FileUtils.readFileToByteArray(page);
        } catch (IOException e) {
            throw new StaticContentFileException(page.getAbsolutePath(), e);
        }
    }
}

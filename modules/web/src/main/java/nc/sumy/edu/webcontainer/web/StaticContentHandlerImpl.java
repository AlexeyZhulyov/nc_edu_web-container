package nc.sumy.edu.webcontainer.web;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
* Provides an implementation of the StaticContentHandler interface.
* Contain one method for read file and return byte array.
*/
public class StaticContentHandlerImpl implements StaticContentHandler {

    public byte[] process(File page) {
        try {
           return FileUtils.readFileToByteArray(page);
        } catch (IOException e) {
            throw new StaticContentFileException(page.getAbsolutePath(), e);
        }
    }
}

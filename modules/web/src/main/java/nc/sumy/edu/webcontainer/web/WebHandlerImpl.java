package nc.sumy.edu.webcontainer.web;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static nc.sumy.edu.webcontainer.common.ClassUtil.fileToString;

public class WebHandlerImpl implements WebHandler {

    public byte[] process(File page) {
        try {
           return FileUtils.readFileToByteArray(page);
        } catch (IOException e) {
            throw new WebException("file was not read: " + page.getAbsolutePath(), e);
        }
    }
}

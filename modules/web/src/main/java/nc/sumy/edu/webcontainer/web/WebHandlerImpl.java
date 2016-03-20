package nc.sumy.edu.webcontainer.web;

import java.io.File;

import static nc.sumy.edu.webcontainer.common.ClassUtil.fileToString;

public class WebHandlerImpl implements WebHandler {

    public String process(File page) {
        return fileToString(page);
    }
}

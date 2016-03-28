package nc.sumy.edu.webcontainer.web;

import java.io.File;

public interface StaticContentHandler {
    byte[] process(File page);
}

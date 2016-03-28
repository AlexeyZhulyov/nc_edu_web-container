package nc.sumy.edu.webcontainer.web;

import java.io.File;

/**
* Contain one method for read file and return byte array.
*/
public interface StaticContentHandler {
    byte[] process(File page);
}

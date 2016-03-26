package nc.sumy.edu.webcontainer.web;

import java.io.File;

public interface WebHandler {
    byte[] process(File page);
}

package nc.sumy.edu.webcontainer.sequrity.interfaces;

import java.util.Set;

public interface Rules {

    String order();

    Set<String> allow();

    Set<String> deny();

    Set<AccessFile> getFiles();

}
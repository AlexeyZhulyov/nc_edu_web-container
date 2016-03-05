package nc.sumy.edu.webcontainer.sequrity.interfaces;

import java.util.Set;

public interface AccessFile {

    String order();

    String getName();

    Set<String> allow();

    Set<String> deny();

}

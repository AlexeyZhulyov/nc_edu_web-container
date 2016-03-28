package nc.sumy.edu.webcontainer.configuration.security;

import java.util.Set;

/**
 * Interface that provides methods for working with rules for directory
 */
public interface Rules {
    
    Set<ServerAccessFile> getFiles();

}
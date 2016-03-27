package nc.sumy.edu.webcontainer.configuration;

import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import java.io.File;

/**
 * Interface that provides methods for working with files security configuration
 */

public interface AccessRulesRepository {

    /**
     * Returns AccessRules stored in file at certain filePath
     * @param filepath contains information about configuration file location
     * @return AccessRules object that was parsed
     */
    AccessRules getAccessRules(String filepath);

    /**
     * Returns AccessRules stored in certain file
     * @param accessRulesFile contains information about configuration file
     * @return AccessRules object that was parsed
     */
    AccessRules getAccessRules(File accessRulesFile);
}

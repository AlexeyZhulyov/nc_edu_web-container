package nc.sumy.edu.webcontainer.configuration;

import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import java.io.File;

public interface AccessRulesConfiguration {

    AccessRules getAccessRules(String filepath);

    AccessRules getAccessRules(File accessRulesFile);
}

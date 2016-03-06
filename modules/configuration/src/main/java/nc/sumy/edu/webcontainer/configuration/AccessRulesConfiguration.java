package nc.sumy.edu.webcontainer.configuration;

import java.io.File;

public interface AccessRulesConfiguration {

    public AccessRules getAccessRules(String filepath);

    public AccessRules getAccessRules(File accessRulesFile);
}

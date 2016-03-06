package nc.sumy.edu.webcontainer.configuration;

import nc.sumy.edu.webcontainer.sequrity.AccessRules;

import java.io.File;

public interface AccessRulesConfiguration {

    public AccessRules getAccessRules(String filepath);

    public AccessRules getAccessRules(File accessRulesFile);
}
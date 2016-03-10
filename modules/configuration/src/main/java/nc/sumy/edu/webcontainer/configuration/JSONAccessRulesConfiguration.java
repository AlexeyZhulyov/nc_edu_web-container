package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import nc.sumy.edu.webcontainer.configuration.security.AccessFile;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import nc.sumy.edu.webcontainer.configuration.security.ServerAccessFile;

import java.io.*;
import java.lang.reflect.Type;

public class JSONAccessRulesConfiguration implements AccessRulesConfiguration {

    @Override
    public AccessRules getAccessRules(String filePath) {
        return getAccessRules(new File(filePath));
    }

    @Override
    public AccessRules getAccessRules(File accessRulesFile) {
        InputStream fileReadStream = JSONAccessRulesConfiguration.class
                .getResourceAsStream("/" + accessRulesFile.getName());
        if(fileReadStream == null) {
            return null;
        }
        BufferedReader bufferedReaderFromFile = new BufferedReader(new InputStreamReader(fileReadStream));
        return new GsonBuilder()
                .create()
                .fromJson(bufferedReaderFromFile, AccessRules.class);
    }
}

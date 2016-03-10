package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import nc.sumy.edu.webcontainer.configuration.security.AccessFile;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import nc.sumy.edu.webcontainer.configuration.security.ServerAccessFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

public class JSONAccessRulesConfiguration implements AccessRulesConfiguration {

    class ServerAccessFileInstanceCreator implements InstanceCreator<AccessFile> {
        @Override
        public AccessFile createInstance(Type type) {
            return new ServerAccessFile();
        }
    }


    @Override
    public AccessRules getAccessRules(String filePath) {
        return getAccessRules(new File(filePath));
    }

    @Override
    public AccessRules getAccessRules(File accessRulesFile) {
        try {
            BufferedReader bufferedReaderFromFile = new BufferedReader(new FileReader(accessRulesFile));
            return new GsonBuilder().registerTypeAdapter(AccessFile.class, new ServerAccessFileInstanceCreator())
                    .create()
                    .fromJson(bufferedReaderFromFile, AccessRules.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}

package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONAccessRulesConfiguration implements AccessRulesConfiguration {


    @Override
    public AccessRules getAccessRules(String filePath) {
        return getAccessRules(new File(filePath));
    }

    @Override
    public AccessRules getAccessRules(File accessRulesFile) {
        try {
            BufferedReader bufferedReaderFromFile = new BufferedReader(new FileReader(accessRulesFile));
            return new Gson().fromJson(bufferedReaderFromFile, AccessRules.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}

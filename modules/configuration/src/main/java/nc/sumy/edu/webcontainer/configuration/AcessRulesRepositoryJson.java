package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.GsonBuilder;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import java.io.*;

public class AcessRulesRepositoryJson implements AccessRulesRepository {

    @Override
    public AccessRules getAccessRules(String filePath) {
        return getAccessRules(new File(filePath));
    }

    @Override
    public AccessRules getAccessRules(File accessRulesFile) {
        InputStream fileReadStream = AcessRulesRepositoryJson.class
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

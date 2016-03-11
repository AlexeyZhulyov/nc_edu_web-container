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
        try(BufferedReader bufferedReaderFromFile =
                    new BufferedReader(new InputStreamReader(new FileInputStream(accessRulesFile))))
        {
            return new GsonBuilder()
                    .create()
                    .fromJson(bufferedReaderFromFile, AccessRules.class);
        } catch (IOException e) {
            return null;
        }

    }
}

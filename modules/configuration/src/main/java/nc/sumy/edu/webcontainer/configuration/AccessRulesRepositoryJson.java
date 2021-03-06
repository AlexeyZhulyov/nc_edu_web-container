package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.GsonBuilder;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Class that parses configuration of files security.
 * This implementation of AccessRulesRepository works with JSON files.
 * @author Lukianykhin O.V.
 */
public class AccessRulesRepositoryJson implements AccessRulesRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AccessRulesRepositoryJson.class);

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
            if (accessRulesFile == null) {
                LOG.debug("Can not read null file", e);
            }
            else {
                LOG.debug("Can not read file " + accessRulesFile.getName()+ " properly", e);
            }
            return null;
        }

    }
}

package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.JsonSyntaxException;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class JSONAccessRulesConfigurationTest {
    JSONAccessRulesConfiguration testInstance = new JSONAccessRulesConfiguration();


    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        AccessRules testAccessRules = testInstance.getAccessRules(new File("src/test/resources/" +
                "validAccessRulesConfiguration.json"));
        assertEquals("Order must be", "allow", testAccessRules.getOrder());
    }

    @Test
    public void invalidFileNameConfigurationTest()  {
        AccessRules testAccessRules = testInstance.getAccessRules("notexistingConfigurationFile.json");
        assertEquals("Not existing file must return null AccessRules", null, testAccessRules);
    }

    @Test(expected = JsonSyntaxException.class)
    public void invalidFileFormatConfigurationTest() throws IOException {
        testInstance.getAccessRules(new File("src/test/resources/invalidFormatConfigurationFile.json"));
    }
}

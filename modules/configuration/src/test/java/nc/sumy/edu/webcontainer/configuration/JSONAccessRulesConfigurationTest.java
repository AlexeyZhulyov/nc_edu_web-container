package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.JsonSyntaxException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JSONAccessRulesConfigurationTest {
    JSONAccessRulesConfiguration testInstance = new JSONAccessRulesConfiguration();


    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        AccessRules testAccessRules = testInstance.getAccessRules(new File("src/test/resources/" +
                "validAccessRulesConfiguration.json"));
        Assert.assertEquals("Order must be", "allow", testAccessRules.getOrder());
    }

    @Test
    public void invalidFileNameConfigurationTest()  {
        AccessRules testAccessRules = testInstance.getAccessRules("notexistingConfigurationFile.json");
        Assert.assertEquals("Not existing file must return null AccessRules", null, testAccessRules);
    }

    @Test(expected = JsonSyntaxException.class)
    public void invalidFileFormatConfigurationTest() throws IOException {
        testInstance.getAccessRules(new File("src/test/resources/invalidFormatConfigurationFile.json"));
    }
}

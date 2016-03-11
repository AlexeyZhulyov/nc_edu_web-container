package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.JsonSyntaxException;
import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import org.junit.Test;
import java.io.File;

import static org.junit.Assert.assertEquals;


public class AcessRulesRepositoryJsonTest {
    AcessRulesRepositoryJson testInstance = new AcessRulesRepositoryJson();


    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        AccessRules testAccessRules = testInstance.getAccessRules(new File("src/test/resources/" +
                "validAccessRulesConfiguration.json"));
        assertEquals("Order must be", "allow", testAccessRules.getOrder());
    }

    @Test
    public void invalidFileNameAccessConfigurationTest()  {
        AccessRules testAccessRules = testInstance.getAccessRules("notexistingConfigurationFile.json");
        assertEquals("Not existing file must return null AccessRules", null, testAccessRules);
    }

    @Test(expected = JsonSyntaxException.class)
    public void invalidFileFormatAccessConfigurationTest() {
        testInstance.getAccessRules(new File("src/test/resources/invalidFormatConfigurationFile.json"));
    }

    @Test
    public void invalidFileNameAsStringParameterAccessConfigurationTest() {
        AccessRules testAccessRules = testInstance.getAccessRules(new File("src/test/resources/fileDoesNotExist.json"));
        assertEquals("Not existing file must return null AccessRules", null, testAccessRules);
    }

}

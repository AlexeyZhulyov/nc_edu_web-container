package nc.sumy.edu.webcontainer.configuration;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;

public class JSONConfigurationTest {

    @Test
    public void noParametersConfigurationTest() {
        Configuration config = new JSONConfiguration();
        Assert.assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidStringParametersConfigurationTest() throws IOException {
        new JSONConfiguration("port:8090}");
    }

    @Test
    public void validStringParametersConfigurationTest() {
        Configuration config = null;
        try {
            config = new JSONConfiguration("{port:8090}");
        } catch (JSONConfigurationReadingException e) {
            Assert.fail("Valid string has to be read");
        }
        Assert.assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        Configuration config;
        try {
            config = new JSONConfiguration(new File("src/test/resources/validTestConfiguration.json"));
            Assert.assertEquals("Port must be 8890", 8890, config.getPort());
        } catch (JSONConfigurationReadingException e) {
            Assert.fail("Existing file has to be read: " + e.getMessage());
        }
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidFileNameConfigurationTest() throws IOException {
        new JSONConfiguration(new File("notexistingConfigurationFile.json"));
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidFileFornatConfigurationTest() throws IOException {
        new JSONConfiguration(new File("src/test/resources/invalidFormatConfigurationFile.json"));
    }

    @Test
    public void setConfigurationTest() {
        Configuration config;
        try {
            config = new JSONConfiguration(new File("src/test/resources/validTestConfiguration.json"));
            config.setPort(100);
            Assert.assertEquals("Port must be 100", 100, config.getPort());
        } catch (JSONConfigurationReadingException e) {
            Assert.fail("Existing file has to be read: " + e.getMessage());
        }

    }
}

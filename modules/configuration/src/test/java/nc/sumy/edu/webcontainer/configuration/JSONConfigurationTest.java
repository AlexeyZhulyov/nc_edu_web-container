package nc.sumy.edu.webcontainer.configuration;

import org.junit.Test;
import java.io.File;

import static org.junit.Assert.assertEquals;

public class JSONConfigurationTest {

    @Test
    public void noParametersConfigurationTest() {
        Configuration config = new JSONConfiguration();
        assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidStringParametersConfigurationTest()  {
        new JSONConfiguration("port:8090}");
    }

    @Test
    public void validStringParametersConfigurationTest() {
        Configuration config = new JSONConfiguration("{port:8090}");
        assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        Configuration config = new JSONConfiguration(new File("validTestConfiguration.json"));
        assertEquals("Port must be 8890", 8890, config.getPort());
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidFileNameConfigurationTest()  {
        new JSONConfiguration(new File("notexistingConfigurationFile.json"));
    }

    @Test(expected = JSONConfigurationReadingException.class)
    public void invalidFileFormatConfigurationTest()  {
        new JSONConfiguration(new File("invalidFormatConfigurationFile.json"));
    }

    @Test
    public void setConfigurationTest() {
        Configuration config = new JSONConfiguration(new File("validTestConfiguration.json"));
        config.setPort(100);
        assertEquals("Port must be 100", 100, config.getPort());
    }
}

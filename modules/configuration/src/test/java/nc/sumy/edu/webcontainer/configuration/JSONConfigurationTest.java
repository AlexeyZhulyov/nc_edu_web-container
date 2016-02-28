package nc.sumy.edu.webcontainer.configuration;

import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class JSONConfigurationTest {

    @Test
    public void noParametersConfigurationTest() {
        Configuration config = new JSONConfiguration();
        Assert.assertEquals("Port must be 8010", 8010, config.getPort());
    }

    @Test(expected = ParseException.class)
    public void invalidStringParametersConfigurationTest() throws ParseException {
        Configuration config = new JSONConfiguration("port:8090}");
    }

    @Test
    public void validStringParametersConfigurationTest() {
        Configuration config = null;
        try {
            config = new JSONConfiguration("{port:8090}");
        } catch (ParseException e) {
            Assert.fail("Valid string has to be read");
        }
        Assert.assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        Configuration config;
        try {
            config = new JSONConfiguration(new File("validTestConfiguration.json"));
            Assert.assertEquals("Port must be 8890", 8890, config.getPort());
        } catch (FileNotFoundException e) {
            Assert.fail("Existing file has to be read");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFileNameConfigurationTest() throws FileNotFoundException {
        Configuration config = new JSONConfiguration(new File("notexistingConfigurationFile.json"));
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidFileFornatConfigurationTest() throws FileNotFoundException {
        Configuration config = new JSONConfiguration(new File("invalidFormatConfigurationFile.json"));
    }

    @Test
    public void setConfigurationTest() {
        Configuration config;
        try {
            config = new JSONConfiguration(new File("validTestConfiguration.json"));
            config.setPort(100);
            Assert.assertEquals("Port must be 100", 100, config.getPort());
        } catch (FileNotFoundException e) {
            Assert.fail("Existing file has to be read");
        }

    }
}

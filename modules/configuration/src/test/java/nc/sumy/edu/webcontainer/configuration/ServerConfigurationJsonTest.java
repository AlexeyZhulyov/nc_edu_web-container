package nc.sumy.edu.webcontainer.configuration;

import org.junit.Test;
import java.io.File;

import static org.junit.Assert.assertEquals;

public class ServerConfigurationJsonTest {

    @Test
    public void noParametersConfigurationTest() {
        ServerConfiguration config = new ServerConfigurationJson();
        assertEquals("Port must be 8090", 8090, config.getPort());
    }

    @Test(expected = ServerConfigurationJsonReadingException.class)
    public void invalidStringFileNameParameterConfigurationTest()  {
        new ServerConfigurationJson("notexistingConfigurationFile.json");
    }

    @Test
    public void validStringParametersConfigurationTest() {
        ServerConfiguration config = new ServerConfigurationJson("validTestConfiguration.json");
        assertEquals("Port must be 8890", 8890, config.getPort());
    }

    @Test
    public void validFileParametersConfigurationStartAndGetTest() {
        ServerConfiguration config = new ServerConfigurationJson(new File("src/test/resources/validTestConfiguration.json"));
        assertEquals("Port must be 8890", 8890, config.getPort());
    }

    @Test(expected = ServerConfigurationJsonReadingException.class)
    public void invalidFileNameConfigurationTest()  {
        new ServerConfigurationJson(new File("notexistingConfigurationFile.json"));
    }

    @Test(expected = ServerConfigurationJsonReadingException.class)
    public void invalidFileFormatConfigurationTest()  {
        new ServerConfigurationJson(new File("src/test/resources/invalidFormatConfigurationFile.json"));
    }

    @Test
    public void setConfigurationTest() {
        ServerConfiguration config = new ServerConfigurationJson(new File("src/test/resources/validTestConfiguration.json"));
        config.setPort(100);
        assertEquals("Port must be 100", 100, config.getPort());
    }
}

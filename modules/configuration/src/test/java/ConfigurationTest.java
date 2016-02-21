
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;

public class ConfigurationTest extends Assert {
    Configuration config;

    @Test
    public void noParametersConfigurationTest() {
        config = new Configuration();
        assertEquals("Port must be 8010", 8010, config.getPort());
    }

    @Test(expected = FileNotFoundException.class)
    public void invalidParametersConfigurationTest() throws FileNotFoundException {
        config = new Configuration("invalid_file_name.json");
    }

    @Test
    public void validParametersConfigurationStartAndGetTest() {
        try {
            config = new Configuration("src\\test\\resources\\validTestConfiguration.json");
        } catch (FileNotFoundException e) {
            fail("File had to be found");
            e.printStackTrace();
        }
        assertEquals("Port must be 8890", 8890, config.getPort());
    }

    @Test
    public void setConfigurationTest() {
        try {
            config = new Configuration("src\\test\\resources\\validTestConfiguration.json");
        } catch (FileNotFoundException e) {
            fail("File had to be found");
            e.printStackTrace();
        }
        config.setPort(100);
        assertEquals("Port must be 100", 100, config.getPort());
    }
}

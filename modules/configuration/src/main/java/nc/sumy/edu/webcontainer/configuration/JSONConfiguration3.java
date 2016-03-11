package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.*;


public class JSONConfiguration3 implements Configuration {
    private ConfigurationProperties configurationProperties;

    class ConfigurationProperties{
        private int port = 8090;

        public void setPort(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }
    }

    public JSONConfiguration3(File configurationFile) {
        try {
            InputStream configurationFileInputStream = new FileInputStream(configurationFile);
            setPropertiesFromInputStream(configurationFileInputStream);
        } catch (FileNotFoundException e) {
            throw new JSONConfigurationReadingException("File was not found");
        }
    }

    public JSONConfiguration3(String configurationFileName) {
        InputStream configurationFileInputStream = JSONConfiguration.class.getResourceAsStream("/" + configurationFileName);
        if (configurationFileInputStream == null) {
            throw new JSONConfigurationReadingException("File was not found");
        }
        setPropertiesFromInputStream(configurationFileInputStream);
    }

    public JSONConfiguration3() {
        this.configurationProperties = new ConfigurationProperties();
    }


    private void setPropertiesFromInputStream(InputStream propertiesInputStream) {
        try{
            BufferedReader bufferedConfigurationReader =
                    new BufferedReader(new InputStreamReader(propertiesInputStream));
            this.configurationProperties = new Gson().fromJson(bufferedConfigurationReader,
                    ConfigurationProperties.class);
        }
        catch (JsonSyntaxException e) {
            throw new JSONConfigurationReadingException("Configuration string has inappropriate format", e);
        } catch (JsonIOException e) {
            throw new JSONConfigurationReadingException("Configuration filewas not read properly", e);
        }
    }

    public int getPort() {
        return configurationProperties.getPort();
    }

    public void setPort(int port) {
        this.configurationProperties.setPort(port);
    }
}


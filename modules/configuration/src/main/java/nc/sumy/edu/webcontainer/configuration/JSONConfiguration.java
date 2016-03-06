package nc.sumy.edu.webcontainer.configuration;

import java.io.*;

import com.google.gson.*;


public class JSONConfiguration implements Configuration {
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

    public JSONConfiguration(File configurationFile) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configurationFile));
            StringBuilder builder = new StringBuilder();
            String appended;
            while((appended = bufferedReader.readLine()) != null) {
                builder.append(appended);
            }
            setPropertiesFromString(new String(builder));
        } catch (IOException e) {
            throw new JSONConfigurationReadingException("File was not read", e);
        }
    }

    public JSONConfiguration(String configurationString) {
        setPropertiesFromString(configurationString);
    }

    public JSONConfiguration() {
        this.configurationProperties = new ConfigurationProperties();
    }


    private void setPropertiesFromString(String propertiesString) {
        try{
            this.configurationProperties = new Gson().fromJson(propertiesString, ConfigurationProperties.class);
        }
        catch (JsonSyntaxException e) {
            throw new JSONConfigurationReadingException("String has inappropriate format", e);
        }
    }

    public int getPort() {
        return configurationProperties.getPort();
    }

    public void setPort(int port) {
        this.configurationProperties.setPort(port);
    }
}

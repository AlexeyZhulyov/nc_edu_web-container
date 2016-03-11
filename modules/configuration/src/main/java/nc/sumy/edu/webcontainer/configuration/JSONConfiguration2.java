package nc.sumy.edu.webcontainer.configuration;

import java.io.*;
import java.nio.charset.Charset;

import com.google.gson.*;


public class JSONConfiguration2 implements Configuration {
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

    public JSONConfiguration2(File configurationFile) {
        try{
            InputStream inputFromFile = new FileInputStream(configurationFile);
            String stringFile = readConfigurationString(inputFromFile);
            setPropertiesFromString(stringFile);
        } catch (IOException e) {
            throw new JSONConfigurationReadingException("File was not read", e);
        }
    }


    public JSONConfiguration2(String configurationFileName) {
        InputStream inputStream = JSONConfiguration.class.getResourceAsStream("/" + configurationFileName);
        String stringFile = readConfigurationString(inputStream);
        setPropertiesFromString(stringFile);
    }

    public JSONConfiguration2() {
        this.configurationProperties = new ConfigurationProperties();
    }

    private String readConfigurationString(InputStream inputFromFile) {
        try{
            if (inputFromFile == null) {
                throw new JSONConfigurationReadingException("File was not found");
            }
            byte[] bytes = new byte[inputFromFile.available()];
            if(inputFromFile.read(bytes) == -1) {
                throw new JSONConfigurationReadingException("File was not found");
            }
            return new String(bytes, Charset.defaultCharset());
        } catch (IOException e) {
            throw new JSONConfigurationReadingException("File was not found");
        }
    }

    private void setPropertiesFromString(String propertiesString) {
        try{
            this.configurationProperties = new Gson().fromJson(propertiesString, ConfigurationProperties.class);
        }
        catch (JsonSyntaxException e) {
            throw new JSONConfigurationReadingException("Configuration string has inappropriate format", e);
        }
    }

    public int getPort() {
        return configurationProperties.getPort();
    }

    public void setPort(int port) {
        this.configurationProperties.setPort(port);
    }
}


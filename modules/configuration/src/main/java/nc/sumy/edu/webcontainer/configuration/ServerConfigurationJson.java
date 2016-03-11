package nc.sumy.edu.webcontainer.configuration;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;


public class ServerConfigurationJson implements ServerConfiguration {
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

    public ServerConfigurationJson(File configurationFile) {
        try{
            byte[] bytesOfFile = Files.readAllBytes(Paths.get(configurationFile.getPath()));
            String stringFile = new String(bytesOfFile, Charset.defaultCharset());
            setPropertiesFromString(stringFile);
        } catch (IOException e) {
            throw new ServerConfigurationJsonReadingException("File was not read", e);
        }
    }

    public ServerConfigurationJson(String configurationFileName) {
        try{
            InputStream inputStream = ServerConfigurationJson.class.getResourceAsStream("/" + configurationFileName);
            if (inputStream == null) {
                throw new ServerConfigurationJsonReadingException("File was not found");
            }
            byte[] bytes = new byte[inputStream.available()];
            if(inputStream.read(bytes) == -1) {
                throw new ServerConfigurationJsonReadingException("File was not found");
            }
            String stringFile = new String(bytes, Charset.defaultCharset());
            setPropertiesFromString(stringFile);
        } catch (IOException e) {
            throw new ServerConfigurationJsonReadingException("File was not read properly", e);
        }
    }

    public ServerConfigurationJson() {
        this.configurationProperties = new ConfigurationProperties();
    }


    private void setPropertiesFromString(String propertiesString) {
        try{
            this.configurationProperties = new Gson().fromJson(propertiesString, ConfigurationProperties.class);
        }
        catch (JsonSyntaxException e) {
            throw new ServerConfigurationJsonReadingException("ServerConfiguration string has inappropriate format", e);
        }
    }

    public int getPort() {
        return configurationProperties.getPort();
    }

    public void setPort(int port) {
        this.configurationProperties.setPort(port);
    }
}

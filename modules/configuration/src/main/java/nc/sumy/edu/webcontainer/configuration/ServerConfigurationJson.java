package nc.sumy.edu.webcontainer.configuration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;

import com.google.gson.*;
import nc.sumy.edu.webcontainer.common.FileNotReadException;
import org.apache.maven.shared.utils.io.IOUtil;
import nc.sumy.edu.webcontainer.common.FileNotFoundException;

import static nc.sumy.edu.webcontainer.common.ClassUtil.fileToString;
import static nc.sumy.edu.webcontainer.common.ClassUtil.getInputStreamByName;


public class ServerConfigurationJson implements ServerConfiguration {
    private ConfigurationProperties configurationProperties;

    class ConfigurationProperties{
        private int port = 8090;
        private String serverHome;

        public void setPort(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public String getServerLocation() {
            return serverHome;
        }

        public void setServerLocation(String serverLocation) {
            this.serverHome = serverLocation;
        }
    }

    public ServerConfigurationJson(File configurationFile) {
        try{
            setConfigFromJson( fileToString(configurationFile));
        } catch (FileNotReadException e) {
            throw new JsonReadingException("File was not read", e);
        }
    }

    public ServerConfigurationJson(String configurationFileName) {
        try{
            InputStream inputStream = getInputStreamByName(ServerConfigurationJson.class, configurationFileName);
            setConfigFromJson(IOUtil.toString(inputStream, String.valueOf(Charset.defaultCharset())));
        } catch (IOException | FileNotFoundException e) {
            throw new JsonReadingException("File " + configurationFileName +
                     " was not read properly", e);
        }
    }

    public ServerConfigurationJson() {
        this.configurationProperties = new ConfigurationProperties();
        //checkSystemVariable("SERVER_HOME");
    }


    private void setConfigFromJson(String propertiesString) {
        try{
            this.configurationProperties = new Gson().fromJson(propertiesString, ConfigurationProperties.class);
            //checkSystemVariable("SERVER_HOME");
        }
        catch (JsonSyntaxException e) {
            throw new JsonReadingException("ServerConfiguration string has inappropriate format", e);
        }
    }

    public void checkSystemVariable(String systemVariableName) {
        String tempPath = System.getProperty(systemVariableName);
        if(Objects.isNull(tempPath)) {
            throw new JsonReadingException("System variable " + systemVariableName + " doesn't exist. Server could" +
                    " not be started");
        }
        this.configurationProperties.setServerLocation(tempPath);
    }

    public int getPort() {
        return configurationProperties.getPort();
    }

    public void setPort(int port) {
        this.configurationProperties.setPort(port);
    }

    @Override
    public String getServerLocation() {
        return this.configurationProperties.getServerLocation();
    }

    @Override
    public void setServerLocation(String serverLocation) {
        this.configurationProperties.setServerLocation(serverLocation);
    }
}

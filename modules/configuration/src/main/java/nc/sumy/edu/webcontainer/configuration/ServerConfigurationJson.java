package nc.sumy.edu.webcontainer.configuration;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.*;
import org.apache.maven.shared.utils.io.IOUtil;
import sun.misc.IOUtils;

import static nc.sumy.edu.webcontainer.configuration.ClasspathUtils.getInputStreamByName;


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
            setConfigFromJson(stringFile);
        } catch (IOException e) {
            throw new ServerConfigurationJsonReadingException("File was not read", e);
        }
    }

    public ServerConfigurationJson(String configurationFileName) {
        try{
            InputStream inputStream = getInputStreamByName(ServerConfigurationJson.class, configurationFileName);
            if (inputStream == null) {
                throw new ServerConfigurationJsonReadingException("Unable to find the file " + configurationFileName);
            }
            setConfigFromJson(IOUtil.toString(inputStream, String.valueOf(Charset.defaultCharset())));
        } catch (IOException e) {
            throw new ServerConfigurationJsonReadingException("File " + configurationFileName +
                     " was not read properly", e);
        }
    }

    public ServerConfigurationJson() {
        this.configurationProperties = new ConfigurationProperties();
    }


    private void setConfigFromJson(String propertiesString) {
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

package nc.sumy.edu.webcontainer.configuration;

import java.io.*;

import com.google.gson.*;


public class JSONConfiguration implements Configuration {
    private int port;

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
        this(new File ("defaultJSONConfiguration.json"));
    }


    private void setPropertiesFromString(String propertiesString) {
        try{
            JSONConfiguration thus = new Gson().fromJson(propertiesString, JSONConfiguration.class);
            copyProperties(thus);
        }
        catch (JsonSyntaxException e) {
            throw new JSONConfigurationReadingException("String has inappropriate format", e);
        }
    }

    private void copyProperties(JSONConfiguration thus) {
        this.port = thus.port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

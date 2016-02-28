package nc.sumy.edu.webcontainer.configuration;

import java.io.*;

import com.google.gson.*;


public class JSONConfiguration implements Configuration {
    private int port;
    public JSONConfiguration(File configurationFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configurationFile));

        try{
            JSONConfiguration thus = new Gson().fromJson(bufferedReader, JSONConfiguration.class);
            this.port = thus.getPort();
        }
        catch (JsonSyntaxException e) {
            throw new IOException("File has inappropriate format", e);
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public JSONConfiguration(String configurationString) throws IOException {
        try{
            JSONConfiguration thus = new Gson().fromJson(configurationString, JSONConfiguration.class);
            this.port = thus.getPort();
        }
        catch (JsonSyntaxException e) {
            throw new IOException("String has inappropriate format", e);
        }
    }

    public JSONConfiguration() {
        super();
        this.port = 8010;
    }

    public int getPort() {
        synchronized(this) {
            return port;
        }
    }

    public void setPort(int port) {
        synchronized(this) {
            this.port = port;
        }
    }
}

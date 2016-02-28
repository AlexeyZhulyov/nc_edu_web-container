package nc.sumy.edu.webcontainer.configuration;

import java.io.*;
import java.text.ParseException;

import com.google.gson.*;
import com.google.gson.JsonSyntaxException;


public class JSONConfiguration implements Configuration {
    private int port;
    public JSONConfiguration(File configurationFile) throws FileNotFoundException {
        InputStream inputStream = JSONConfiguration.class.getResourceAsStream(configurationFile.getName());
        if (inputStream == null) {
            throw new FileNotFoundException("Such file wasn't found");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try{
            JSONConfiguration thus = new Gson().fromJson(bufferedReader, JSONConfiguration.class);
            this.port = thus.getPort();
        }
        catch (JsonSyntaxException e) {
            throw new FileNotFoundException("File has inappropriate format");
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public JSONConfiguration(String configurationString) throws ParseException {
        try{
            JSONConfiguration thus = new Gson().fromJson(configurationString, JSONConfiguration.class);
            this.port = thus.getPort();
        }
        catch (JsonSyntaxException e) {
            throw new ParseException("String has inappropriate format",0);
        }
    }

    public JSONConfiguration() {
        super();
        this.port = 8010;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.*;

public class JSONConfiguration implements ConfigurationInterface {
    int port;
    public JSONConfiguration(String filepath) throws FileNotFoundException {
        File jsonFile = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(jsonFile));
        JSONConfiguration thus = new Gson().fromJson(br, JSONConfiguration.class);
        this.port = thus.getPort();
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

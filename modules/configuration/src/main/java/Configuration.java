import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.*;

public class Configuration implements ConfigurationInterface {
    int port;
    public Configuration(String filepath) throws FileNotFoundException {
        File jsonFile = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(jsonFile));
        Configuration thus = new Gson().fromJson(br, Configuration.class);
        this.port = thus.getPort();
    }

    public Configuration() {
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

package nc.sumy.edu.webcontainer.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that starts server. Program start point.
 * @author Lukianykhin O.V.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String line = consoleReader.readLine();
                if("Stop server".equals(line)) {
                    server.stopServer();
                    System.exit(0);
                }
            } catch (IOException e) {
                LOG.warn("IOException occured. Continue work.", e);
            }
        }
    }
}

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketListener implements Runnable {
    private ServerSocket serverSocket;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketListener.class);

    public ServerSocketListener(ServerConfiguration configuration) {
        int port = configuration.getPort();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOGGER.error("Could not create ServerSocket at port " + port, e);
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                new ClientSocketProcessingThread(clientSocket).start();
            } catch (IOException e) {
                LOGGER.error("IOException occured", e);
            }
        }
    }
}

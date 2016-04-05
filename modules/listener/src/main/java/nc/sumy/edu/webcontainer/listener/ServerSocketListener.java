package nc.sumy.edu.webcontainer.listener;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that listen to port on a server, gets Socket and starts its processing.
 * @author Lukianykhin O.V.
 */

public class ServerSocketListener extends Thread {
    private final ServerSocket serverSocket;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketListener.class);
    private ModelSocketProcessing model;
    private boolean flag = true;

    public ServerSocketListener(ServerConfiguration configuration, Deployment deployment) throws IOException {
        //this.model = new ModelSocketProcessing(new ServerDispatcher(configuration, deployment));
        this.model = new ModelSocketProcessing(configuration, deployment);
        int port = configuration.getPort();
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Stops listening of port on a server. Must be used before stopping server.
     */
    public void stopListening(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            LOGGER.warn("ServerSocket was not closed properly.", e);
        }
        flag = false;
    }


    @Override
    public void run() {
        while (flag) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                new Thread() {
                    @Override
                    public void run() {
                        model.processRequest(clientSocket);
                    }
                }.start();
            } catch (IOException e) {
                LOGGER.error("IOException occured", e);
                LOGGER.error("Listener will be stopped");
                break;
            }
        }
    }

    protected void setModel(ModelSocketProcessing model) {
        this.model = model;
    }
}

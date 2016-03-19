package nc.sumy.edu.webcontainer.listener;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketListener implements Runnable {
    private ServerSocket serverSocket;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketListener.class);
    private ModelSocketProcessing model;

    public ServerSocketListener(ServerConfiguration configuration, Deployment deployment) throws IOException {
        this.model = new ModelSocketProcessing(new ServerDispatcher(configuration, deployment));
        int port = configuration.getPort();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            LOGGER.error("Could not create ServerSocket at port " + port, e);
            throw e;
        }
    }


    @Override
    public void run() {
        while (true) {
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
}

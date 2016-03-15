import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketListener implements Runnable {
    private ServerSocket serverSocket;

    public ServerSocketListener(ServerConfiguration configuration) {
        this.serverSocket = new ServerSocket(configuration.getPort());
    }


    @Override
    public void run() {
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            new ClientSocketProcessingThread(clientSocket).start();
        }
    }
}

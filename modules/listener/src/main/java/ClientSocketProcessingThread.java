import java.net.Socket;

public class ClientSocketProcessingThread extends Thread{
    private Socket clientSocket;

    public ClientSocketProcessingThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        //do smth with Request and return Response to client
    }
}

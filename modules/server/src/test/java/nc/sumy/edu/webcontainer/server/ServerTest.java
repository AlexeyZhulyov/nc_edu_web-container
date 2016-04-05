package nc.sumy.edu.webcontainer.server;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTest {
    @Test
    public void mainSimulateTest() throws InterruptedException {
        System.setProperty("SERVER_HOME", "src\\test\\resources");
        Server server = new Server();
        server.startServer();
        Thread.sleep(3000);
        server.stopServer();
    }

    @Test(expected = ServerFailException.class)
    public void startServerWithoutSystemVariableTest() {
        new Server();
    }

    @Test(expected = ServerFailException.class)
    public void startServerWithBadSystemVariableTest()  {
        System.setProperty("SERVER_HOME", "src\\test\\resources\\badpath");
        new Server();
    }

    @Test(expected = ServerFailException.class)
    public void systemVariableAtFileTest() {
        System.setProperty("SERVER_HOME", "src\\test\\resources\\test.txt");
        new Server();
    }

    @Test(expected = ServerFailException.class)
    public void usedPortTest() throws  IOException {
        try(ServerSocket serverSocket = new ServerSocket(8096)){
            System.setProperty("SERVER_HOME", "src\\test\\resources");
            new Server();
        }
    }
}

package nc.sumy.edu.webcontainer.server;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTest {
    public static final String VARIABLE_NAME = "SERVER_HOME";
    @Test
    public void mainSimulateTest() throws InterruptedException {
        System.setProperty(VARIABLE_NAME, "src\\test\\resources");
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
        System.setProperty(VARIABLE_NAME, "src\\test\\resources\\badpath");
        new Server();
    }

    @Test(expected = ServerFailException.class)
    public void systemVariableAtFileTest() {
        System.setProperty(VARIABLE_NAME, "src\\test\\resources\\test.txt");
        new Server();
    }

    @Test(expected = ServerFailException.class)
    public void usedPortTest() throws  IOException {
        try(ServerSocket serverSocket = new ServerSocket(8096)){
            System.setProperty(VARIABLE_NAME, "src\\test\\resources");
            new Server();
        }
    }
}

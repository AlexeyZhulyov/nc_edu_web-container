package nc.sumy.edu.webcontainer.server;

import org.junit.Test;

public class ServerTest {
    @Test
    public void maiTest() throws InterruptedException {
        System.setProperty("SERVER_HOME", "src\\test\\resources");
        Server server = new Server();
        server.startServer();
        Thread.sleep(3000);
        server.stopServer();
    }
}

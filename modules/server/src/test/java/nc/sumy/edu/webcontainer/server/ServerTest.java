package nc.sumy.edu.webcontainer.server;

import org.junit.Test;

public class ServerTest {
    @Test
    public void maiTest() throws InterruptedException {
        System.setProperty("SERVER_HOME", "C:\\Users\\Вадим\\IdeaProjects\\nc_edu_web-container-3\\modules");
        Server server = new Server();
        server.startServer();
        Thread.sleep(3000);
        server.stopServer();
    }
}

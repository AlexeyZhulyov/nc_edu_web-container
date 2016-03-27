package nc.sumy.edu.webcontainer.server;


public class Main {
    public static void main(String[] args) {
        System.setProperty("SERVER_HOME", "C:\\Users\\LENOVO\\Desktop\\nc_web_server\\server2\\nc_edu_web-container\\modules");
        Server server = new Server();
        server.startServer();
    }
}

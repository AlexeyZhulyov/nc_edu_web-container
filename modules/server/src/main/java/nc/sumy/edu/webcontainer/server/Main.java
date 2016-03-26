package nc.sumy.edu.webcontainer.server;


public class Main {
    public static void main(String[] args) {
        System.setProperty("SERVER_HOME", "C:\\Users\\User\\IdeaProjects\\nc_edu_web-container-5\\modules");
        Server server = new Server();
        server.startServer();
    }
}

package nc.sumy.edu.webcontainer.configuration;

public interface ServerConfiguration {
    int getPort();

    void setPort(int port);

    String getServerLocation();

    void setServerLocation(String wwwLocation);

    void checkSystemVariable(String systemVariableName);
}

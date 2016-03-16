package nc.sumy.edu.webcontainer.configuration;

public interface ServerConfiguration {
    int getPort();

    void setPort(int port);

    String getWwwLocation();

    void setWwwLocation(String wwwLocation);
}

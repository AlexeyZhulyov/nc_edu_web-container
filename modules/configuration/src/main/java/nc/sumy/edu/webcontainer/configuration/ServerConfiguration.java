package nc.sumy.edu.webcontainer.configuration;

/**
 * Interface that provides methods for working with server configuration
 */

public interface ServerConfiguration {
    int getPort();

    void setPort(int port);

    String getWwwLocation();

    void setWwwLocation(String wwwLocation);

    /**
     * Method checks whether system variable is set.
     * If not - throws JsonReadingException.
     * Else - saves it into server configuration
     * @param systemVariableName system variable that must be checked
     */
    void checkSystemVariable(String systemVariableName);
}

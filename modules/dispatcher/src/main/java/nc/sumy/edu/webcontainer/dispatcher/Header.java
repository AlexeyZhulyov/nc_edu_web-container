package nc.sumy.edu.webcontainer.dispatcher;

/**
 * <p>Enum that contain headers titles of HTTP response.</p>
 * @author Vinogradov Maxim
 */
public enum Header {
    CONTENT_TYPE("Content-Type"),
    CACHE_CONTROL("Cache-Control"),
    DATE("Date"),
    SERVER("Server"),
    CONNECTION("Connection"),
    CONTENT_LANGUAGE("Content-Language"),
    PRAGMA("Pragma");

    private String header;

    Header(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}

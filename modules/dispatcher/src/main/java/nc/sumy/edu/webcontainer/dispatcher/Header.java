package nc.sumy.edu.webcontainer.dispatcher;

/**
 * Enum that contains several headers of HTTP response.
 * @author Vinogradov M.O.
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

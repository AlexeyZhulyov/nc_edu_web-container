package nc.sumy.edu.webcontainer.http;

/**
 * <p>Enum that contain method types of request.</p>
 * @author Vinogradov Maxim
 */
public enum HttpMethod {

    /**
     * <p>GET value is equals GET request-type.</p>
     */
    GET,

    /**
     * <p>OPTION value is equals OPTION request-type.</p>
     */
    OPTIONS,

    /**
     * <p>POST value is equals POST request-type.</p>
     */
    POST,

    /**
     * <p>UNKNOWN value of HttpMethod is used when browser send unsupported type of HTTP-method of request.</p>
     */
    UNKNOWN
}

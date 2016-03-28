package nc.sumy.edu.webcontainer.http;

/**
 * <p>Enum that contain method types of request.</p>
 * @author Vinogradov Maxim
 */
public enum HttpMethod {
    GET,
    OPTIONS,
    POST,

    /**
     * <p>UNKNOWN value of HttpMethod is used when browser send unsupported type of HTTP-method of request.</p>
     */
    UNKNOWN
}

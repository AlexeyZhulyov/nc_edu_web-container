package nc.sumy.edu.webcontainer.http;

/**
 * Enum that contains the types of request methods.
 * @author Vinogradov M.O.
 */
public enum HttpMethod {
    GET,

    OPTIONS,

    POST,

    /**
     * UNKNOWN value is used when browser sends unsupported type of HTTP-method of request.
     */
    UNKNOWN
}

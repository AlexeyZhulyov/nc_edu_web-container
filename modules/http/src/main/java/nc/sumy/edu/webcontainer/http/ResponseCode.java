package nc.sumy.edu.webcontainer.http;

/**
 * Enum that  contain codes of response.
 * @author Vinogradov Maxim
 */
public enum ResponseCode {

    /**
     * <p>OK value is equals 200 HTTP response-code.</p>
     */
    OK(200),

    /**
     * <p>FOUND value is equals 302 HTTP response-code.</p>
     */
    FOUND(302),

    /**
     * <p>BAD_REQUEST value is equals 400 HTTP response-code.</p>
     */
    BAD_REQUEST(400),

    /**
     * <p>FORBIDDEN value is equals 403 HTTP response-code.</p>
     */
    FORBIDDEN(403),

    /**
     * <p>NOT_FOUND value is equals 404 HTTP response-code.</p>
     */
    NOT_FOUND(404),

    /**
     * <p>NOT_ALLOWED value is equals 405 HTTP response-code.</p>
     */
    NOT_ALLOWED(405);

    private final int code;

    /**
     * Constructor of ResponseCode enum? that set the http-response code.
     * @param code set code of http-response.
     */
    ResponseCode(final int code) {
        this.code = code;
    }

    /**
     * @return return code as number (int value).
     */
    public int getCode() {
        return code;
    }

    /**
     * @return return code as String.
     */
    public String getString() {
        return Integer.toString(code);
    }

}

package nc.sumy.edu.webcontainer.http;

/**
 * Enum that contain codes of response.
 * @author Vinogradov M.O.
 */
public enum ResponseCode {
    OK(200),

    FOUND(302),

    BAD_REQUEST(400),

    FORBIDDEN(403),

    NOT_FOUND(404),

    NOT_ALLOWED(405);

    private final int code;

    ResponseCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getString() {
        return Integer.toString(code);
    }

}

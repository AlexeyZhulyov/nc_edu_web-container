package nc.sumy.edu.webcontainer.http;

/**
 * Enum that  contain codes of response.
 * @author Vinogradov Maxim
 */
public enum ResponseCode {
    CODE_200(200),
    CODE_400(400),
    CODE_403(403),
    CODE_404(404),
    CODE_405(405);

    private final int code;

    ResponseCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

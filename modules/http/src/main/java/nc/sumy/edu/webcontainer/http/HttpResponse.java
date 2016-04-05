package nc.sumy.edu.webcontainer.http;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.arraycopy;
import static java.util.Objects.isNull;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;

/**
 * Class that creates HTTP response and contains data of it.
 * @author Vinogradov M.O.
 */
public class HttpResponse implements Response {
    private static final String ENDL = "\r\n";
    private static final Map<Integer, String> RESPONSE_CODES = new HashMap<>();
    private int code;
    private String response = "HTTP/1.1 ";
    private Map<String, String> headers;
    private byte[] body;
    private String redirectUrl = "";

    static {
        RESPONSE_CODES.put(OK.getCode(), "200 OK");
        RESPONSE_CODES.put(FOUND.getCode(), "302 Found");
        RESPONSE_CODES.put(BAD_REQUEST.getCode(), "400 Bad Request");
        RESPONSE_CODES.put(FORBIDDEN.getCode(), "403 Forbidden");
        RESPONSE_CODES.put(NOT_FOUND.getCode(), "404 Not Found");
        RESPONSE_CODES.put(NOT_ALLOWED.getCode(), "405 Method Not Allowed");
    }

    public HttpResponse(int code) {
        this.code = code;
        headers = new LinkedHashMap<>();
    }

    public HttpResponse(int code, Map<String, String> headers, byte[] body) {
        this.code = code;
        this.headers = headers;
        this.body = new byte[body.length];
        arraycopy(body, 0, this.body, 0, body.length);
    }

    public byte[] getResponse() {
        response = "HTTP/1.1 ";
        response += RESPONSE_CODES.get(code)
                + ENDL
                + getHeadersSting();
        byte[] part1 = response.getBytes();
        byte[] part2 = body;
        byte[] result = new byte[part1.length + part2.length];
        System.arraycopy(part1, 0, result, 0, part1.length);
        System.arraycopy(part2, 0, result, part1.length, part2.length);
        return result;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public Response setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Response setHeader(String key, String value) {
        headers.put(key,value);
        return this;
    }

    public Response setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Response setBody(byte[] body) {
        this.body = new byte[body.length];
        arraycopy(body, 0, this.body, 0, body.length);
        return this;
    }

    public byte[] getBody() {
        byte[] newBody = new byte[body.length];
        arraycopy(body, 0, newBody, 0, body.length);
        return newBody;
    }

    private String getHeadersSting() {
        StringBuilder headersStr = new StringBuilder("");
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            headersStr.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(ENDL);
        }
        headersStr.append(ENDL);
        return headersStr.toString();
    }

    public static String getResponseCode(int code) {
        return RESPONSE_CODES.get(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (isNull(obj) || getClass() != obj.getClass()) return false;
        HttpResponse that = (HttpResponse) obj;
        return new EqualsBuilder()
                .append(code, that.code)
                .append(response, that.response)
                .append(headers, that.headers)
                .append(body, that.body)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .append(response)
                .append(headers)
                .append(body)
                .toHashCode();
    }

}
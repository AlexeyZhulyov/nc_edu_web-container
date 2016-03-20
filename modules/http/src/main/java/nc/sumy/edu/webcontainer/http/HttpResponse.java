package nc.sumy.edu.webcontainer.http;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import java.util.*;

import static java.lang.System.arraycopy;
import static java.util.Objects.isNull;

/**
 * Class that build HTTP response and contain response-data.
 * @author Vinogradov Maxim
 */
public class HttpResponse implements Response {
    private static final String ENDL = "\r\n";
    private static final Map<Integer, String> RESPONSE_CODES = new HashMap<>();
    private int code;
    private String response = "HTTP/1.1 ";
    private Map<String, String> headers;
    private byte[] body;

    static {
        RESPONSE_CODES.put(200, "200 OK");
        RESPONSE_CODES.put(400, "400 Bad Request");
        RESPONSE_CODES.put(403, "403 Forbidden");
        RESPONSE_CODES.put(404, "404 Not Found");
        RESPONSE_CODES.put(405, "405 Method Not Allowed");
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
                + getHeadersSting()
                + new String(body);
        return response.getBytes();
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
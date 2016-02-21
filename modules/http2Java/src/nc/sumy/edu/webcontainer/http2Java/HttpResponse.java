package nc.sumy.edu.webcontainer.http2Java;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse implements Response {
    private final String endl = "\r\n";
    private final Map<Integer, String> responseCodes = new HashMap<Integer, String>() {{
        put(200, "200 OK");
        put(400, "400 Bad Request");
        put(403, "403 Forbidden");
        put(404, "404 Not Found");
        put(405, "405 Method Not Allowed");
        put(500, "500 Internal Server Error");
        put(501, "501 Not Implemented");
    }};
    private int code;
    private StringBuilder response = new StringBuilder("HTTP/1.1 ");
    private Map<String, String> headers;
    private byte[] body;

    public HttpResponse(int code) {
        this.code = code;
        headers = new LinkedHashMap<String, String>();
    }

    public HttpResponse(int code, Map<String, String> headers, byte[] body) {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public byte[] getResponse() {
        response = new StringBuilder("HTTP/1.1 ");
        response.append(responseCodes.get(code))
                .append(endl)
                .append(getHeadersSting())
                .append(new String(body));
        return response.toString().getBytes();
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
        this.body = body;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    private String getHeadersSting() {
        StringBuilder headersStr = new StringBuilder("");
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            headersStr.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(endl);
        }
        headersStr.append(endl);
        return headersStr.toString();
    }

}
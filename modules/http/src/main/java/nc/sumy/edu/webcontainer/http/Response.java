package nc.sumy.edu.webcontainer.http;

import java.util.Map;

public interface Response {

    /**
     * @param code of HTTP response.
     */
    Response setCode(int code);

    /**
     * @return code of HTTP response.
     */
    int getCode();

    /**
     * Set header to the headers map of response.
     * @param name
     * @param value
     */
    Response setHeader(String name, String value);

    /**
     * Set headers map.
     * @param headers
     */
    Response setHeaders(Map<String, String> headers);

    /**
     * @param key
     * @return header value by header title from header map.
     */
    String getHeader(String key);

    /**
     * @return map of headers.
     */
    Map<String, String> getHeaders();

    /**
     * Set body of HTTP response.
     * @param body
     */
    Response setBody(byte[] body);

    byte[] getBody();

    byte[] getResponse();
}

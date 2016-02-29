package nc.sumy.edu.webcontainer.http;

import java.util.Map;

public interface Response {
    Response setCode(int code);
    int getCode();
    Response setHeader(String name, String value);
    Response setHeaders(Map<String, String> headers);
    String getHeader(String key);
    Map<String, String> getHeaders();
    Response setBody(byte[] body);
    byte[] getBody();
    byte[] getResponse();
}

package nc.sumy.edu.webcontainer.http2Java;

import java.util.Map;

public interface Request {
    HttpMethod getMethod();
    String getURN();
    Map<String, String> getHeaders();
    Map<String, String> getParameters();
    String getHeader(String key);
    String getParameter(String key);
}

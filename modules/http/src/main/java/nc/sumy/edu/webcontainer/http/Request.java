package nc.sumy.edu.webcontainer.http;

import java.util.Map;

public interface Request {
    String getHost();
    String getIpAddress();
    HttpMethod getMethod();
    String getUrn();
    Map<String, String> getHeaders();
    Map<String, String> getParameters();
    String getHeader(String key);
    String getParameter(String key);
    String getRequestText();
    String getDomainName();
}

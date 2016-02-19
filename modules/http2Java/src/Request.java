import java.util.Map;

public interface Request {
    HttpMethod getMethod();
    String getURI();
    Map<String, String> getHeaders();
    Map<String, String> getParameters();
    String getHeader(String key);
    String getParameter(String key);
}

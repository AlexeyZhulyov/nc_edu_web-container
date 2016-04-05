package nc.sumy.edu.webcontainer.http;

import java.util.Map;

/**
 * Interface that provides methods for working with HTTP request data.
 * @author Vinogradov M.O.
 */
public interface Request {

    String getHost();

    String getIpAddress();

    HttpMethod getMethod();

    String getUrn();

    Request setUrn(String urn);

    Map<String, String> getHeaders();

    Map<String, String> getParameters();

    String getHeader(String key);

    String getParameter(String key);

    String getRequestText();

    String getDomainName();

}

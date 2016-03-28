package nc.sumy.edu.webcontainer.http;

import java.util.Map;

/**
 * Interface that provides methods for working with request.
 * @author Vinogradov M.O.
 */
public interface Request {

    /**
     * @return host, that gotten from socket and used in web-module.
     */
    String getHost();

    /**
     * @return ip address, that gotten from socket and used in web-module.
     */
    String getIpAddress();

    HttpMethod getMethod();

    String getUrn();

    Map<String, String> getHeaders();

    Map<String, String> getParameters();

    String getHeader(String key);

    String getParameter(String key);

    /**
     * @return full request text.
     */
    String getRequestText();

    String getDomainName();

}

package nc.sumy.edu.webcontainer.http;

import java.util.Map;

/**
 * Interface that provides methods for working with request.
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

    /**
     * @return method of HTTP request.
     */
    HttpMethod getMethod();

    /**
     * @return URN of HTTP request.
     */
    String getUrn();

    /**
     * @return map of headers, where keys is title of headers.
     */
    Map<String, String> getHeaders();

    /**
     * @return map of parameters, where keys is title of parameters map.
     */
    Map<String, String> getParameters();

    /**
     * @param key of header.
     * @return value by key from header.
     */
    String getHeader(String key);

    /**
     * @param key of parameters map.
     * @return value by key from parameters.
     */
    String getParameter(String key);

    /**
     * @return full request text.
     */
    String getRequestText();

    /**
     * @return name of domain if it exist.
     */
    String getDomainName();

}

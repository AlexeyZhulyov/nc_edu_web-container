package nc.sumy.edu.webcontainer.http;

import java.util.Map;

/**
 * Interface that provides methods for creating HTTP response and works with its data.
 * @author Vinogradov M.O.
 */
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

    String getRedirectUrl();

    Response setRedirectUrl(String redirectUrl);

}

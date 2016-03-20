package nc.sumy.edu.webcontainer.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that parse HTTP request and contain request-data.
 * @author Vinogradov Maxim
 */
public class HttpRequest implements Request {
    private HttpMethod method;
    private String urn;
    private final String HOST;
    private final String IP_ADDRESS;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequest.class);
    private static final String UTF8 = "UTF-8";
    private final String request;
    private final String requestLines[];
    private final String firstLine[];

    public HttpRequest(String request, String ipAddress, String host) {
        this.request = request;
        this.IP_ADDRESS = ipAddress;
        this.HOST = host;
        requestLines = split(request, "\r\n");
        firstLine = split(requestLines[0], " ");
        parseMethod();
        parseURI();
        parseHeaders();
        parsePostParam();
        decodeParameters();
    }

    private void parseMethod() {
        if (startsWith(request, "GET")) {
            method = HttpMethod.GET;
        } else if (startsWith(request, "POST")) {
            method = HttpMethod.POST;
        } else if (startsWith(request, "OPTION")) {
            method = HttpMethod.OPTIONS;
        } else {
            method = HttpMethod.UNKNOWN;
        }
    }

    private void parseURI() {
        if (startsWith(firstLine[1], ("http"))) {
            if (startsWith(firstLine[1],"http://")) {
                firstLine[1] = replace(firstLine[1], "http://", "");
            } else if (startsWith(firstLine[1], "https://")) {
                firstLine[1] = replace(firstLine[1], "https://", "");
            }
            int pos = indexOf(firstLine[1], '/');
            String host = substring(firstLine[1], 0, pos);
            headers.put("Host", host);
            firstLine[1] = replace(firstLine[1], host, "");
        }
        if (method == HttpMethod.GET && contains(firstLine[1], '?')) {
            String[] pathParts = split(firstLine[1], "\\?");
            urn = pathParts[0];
            String paramPairs[] = split(pathParts[1], "&");
            String pairParts[];
            for (String pair : paramPairs) {
                pairParts = split(pair, "=");
                parameters.put(pairParts[0], pairParts[1]);
            }
        } else {
            urn = firstLine[1];
        }
    }

    private void parseHeaders() {
        String temp[] = new String[2];
        int pos;
        for (int i = 1; i < requestLines.length; i++) {
            if (StringUtils.equals(requestLines[i], "")) {
                break;
            }
            pos = indexOf(requestLines[i],':');
            temp[0] = substring(requestLines[i], 0, pos);
            temp[1] = substring(requestLines[i], pos+1, length(requestLines[i]));
            if (pos != -1) {
                headers.put(trim(temp[0]), trim(temp[1]));
            }
        }
    }

    private void parsePostParam() {
        int lastItem = requestLines.length - 1;
        if (method == HttpMethod.POST  && nonNull(requestLines[lastItem])) {
            String paramPairs[] = split(trim(requestLines[lastItem]), "&");
            String pairParts[];
            for (String pair : paramPairs) {
                pairParts = split(pair, "=");
                parameters.put(pairParts[0], pairParts[1]);
            }
        }
    }

    private void decodeParameters() {
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            try {
                param.setValue(URLDecoder.decode(param.getValue(), UTF8));
            } catch (UnsupportedEncodingException e) {
               LOGGER.error("Cannot decode with UTF-8.", e);
            }
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrn() {
        return urn;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getHeader(String key) {
        return isNull(headers.get(key)) ? "" : headers.get(key);
    }

    public String getParameter(String key) {
        return isNull(parameters.get(key)) ? "" : parameters.get(key);
    }

    public String getHost() {
        return HOST;
    }

    public String getIpAddress() {
        return IP_ADDRESS;
    }

    public String getRequestText() {
        return request;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (isNull(obj) || getClass() != obj.getClass()) return false;

        HttpRequest that = (HttpRequest) obj;

        return new EqualsBuilder()
                .append(method, that.method)
                .append(urn, that.urn)
                .append(HOST, that.HOST)
                .append(IP_ADDRESS, that.IP_ADDRESS)
                .append(headers, that.headers)
                .append(parameters, that.parameters)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(method)
                .append(urn)
                .append(HOST)
                .append(IP_ADDRESS)
                .append(headers)
                .append(parameters)
                .toHashCode();
    }
}
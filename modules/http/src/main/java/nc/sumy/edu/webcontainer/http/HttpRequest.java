package nc.sumy.edu.webcontainer.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that parse HTTP request and contain request-data.
 * @author Vinogradov Maxim
 */
public class HttpRequest implements Request {
    private HttpMethod method;
    private String urn;
    private final Map<String, String> headers = new HashMap();
    private final Map<String, String> parameters = new HashMap();
    private final String request;
    private final String requestLines[];
    private final String firstLine[];

    public HttpRequest(String request) {
        this.request = request;
        requestLines = split(request, "\r\n");
        firstLine = split(requestLines[0], " ");
        parseMethod();
        parseURI();
        parseHeaders();
        parsePostParam();
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
        if (method == HttpMethod.POST  && !Objects.isNull(requestLines[lastItem])) {
            String paramPairs[] = split(trim(requestLines[lastItem]), "&");
            String pairParts[];
            for (String pair : paramPairs) {
                pairParts = split(pair, "=");
                parameters.put(pairParts[0], pairParts[1]);
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
        String value;
        value = headers.get(key);
        if (Objects.isNull(value)) {
            value = "";
        }
        return value;
    }

    public String getParameter(String key) {
        String value;
        value = parameters.get(key);
        if (Objects.isNull(value)) {
            value = "";
        }
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        HttpRequest that = (HttpRequest) obj;

        return new EqualsBuilder()
                .append(method, that.method)
                .append(urn, that.urn)
                .append(headers, that.headers)
                .append(parameters, that.parameters)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(method)
                .append(urn)
                .append(headers)
                .append(parameters)
                .toHashCode();
    }
}
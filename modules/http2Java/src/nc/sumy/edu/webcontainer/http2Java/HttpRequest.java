package nc.sumy.edu.webcontainer.http2Java;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.indexOf;

/**
 * Class that parse HTTP request and contain request-data.
 * @author Vinogradov Maxim
 */
public class HttpRequest implements Request {
    private HttpMethod method;
    private String URN;
    private Map<String, String> headers = new HashMap();
    private Map<String, String> parameters = new HashMap();
    private String request;
    private String requestLines[];
    private String firstLine[];

    public HttpRequest(String request) {
        this.request = request;
        requestLines = request.split("\r\n");
        firstLine = requestLines[0].split(" ");
        parseMethod();
        parseURI();
        parseHeaders();
        parsePostParam();
    }

    private void parseMethod() {
        if (indexOf(request, "GET") == 0) {
            method = HttpMethod.GET;
        } else if (indexOf(request, "POST") == 0) {
            method = HttpMethod.POST;
        } else if (indexOf(request, "OPTION") == 0) {
            method = HttpMethod.OPTIONS;
        } else {
            method = HttpMethod.UNKNOWN;
        }
    }

    private void parseURI() {
        if (firstLine[1].startsWith("http")) {
            if (firstLine[1].startsWith("http://")) {
                firstLine[1] = firstLine[1].replace("http://", "");
            } else if (firstLine[1].startsWith("https://")) {
                firstLine[1] = firstLine[1].replace("https://", "");
            }
            int pos = firstLine[1].indexOf('/');
            String host = firstLine[1].substring(0, pos);
            headers.put("Host", host);
            firstLine[1] = firstLine[1].replace(host, "");
        }
        if (method == HttpMethod.GET && contains(firstLine[1], '?')) {
            String[] pathParts = firstLine[1].split("\\?");
            URN = pathParts[0];
            String paramPairs[] = pathParts[1].split("&");
            String pairParts[];
            for (String pair : paramPairs) {
                pairParts = pair.split("=");
                parameters.put(pairParts[0], pairParts[1]);
            }
        } else {
            URN = firstLine[1];
        }
    }

    private void parseHeaders() {
        String temp[];
        for (int i = 1; i < requestLines.length; i++) {
            if (StringUtils.equals(requestLines[i], "")) {
                break;
            }
            temp = requestLines[i].split(": ");
            if (temp.length == 2) {
                headers.put(temp[0].trim(), temp[1].trim());
            }
        }
    }

    private void parsePostParam() {
        int i = requestLines.length - 1;
        if (method == HttpMethod.POST  && !Objects.isNull(requestLines[i])) {
            String paramPairs[] = requestLines[i].trim().split("&");
            String pairParts[];
            for (String pair : paramPairs) {
                pairParts = pair.split("=");
                parameters.put(pairParts[0], pairParts[1]);
            }
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getURN() {
        return URN;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        if (method != that.method) return false;
        if (URN != null ? !URN.equals(that.URN) : that.URN != null) return false;
        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (request != null ? !request.equals(that.request) : that.request != null) return false;
        if (!Arrays.equals(requestLines, that.requestLines)) return false;
        return Arrays.equals(firstLine, that.firstLine);

    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (URN != null ? URN.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (request != null ? request.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(requestLines);
        result = 31 * result + Arrays.hashCode(firstLine);
        return result;
    }
}
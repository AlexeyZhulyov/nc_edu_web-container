package nc.sumy.edu.webcontainer.web.servlet;

import nc.sumy.edu.webcontainer.web.WebURLException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.net.MalformedURLException;

@SuppressWarnings("PMD")
public class RequestDispatcherImpl implements RequestDispatcher {

    private String relativePath;
    private RequestWrapper requestWrapper;

    public RequestDispatcherImpl(String relativePath, RequestWrapper requestWrapper) {
        this.relativePath = relativePath;
        this.requestWrapper = requestWrapper;
    }

    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) {
        String fullPath = null;
        try {
            fullPath = requestWrapper.getServletContext().getResource(relativePath).getPath();
        } catch (MalformedURLException e) {
            throw new WebURLException(relativePath, e);
        }
        String path = StringUtils.substringAfter(fullPath, "www");
        ((ResponseWrapper) servletResponse).getResponse().setRedirectUrl(path);
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) {
        throw new UnsupportedOperationException();
    }
}

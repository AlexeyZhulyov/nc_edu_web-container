package nc.sumy.edu.webcontainer.web.servlet;

//import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
//import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.common.FileNotReadException;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.http.*;
import nc.sumy.edu.webcontainer.web.JspHandler;
import nc.sumy.edu.webcontainer.web.JspHandlerImpl;
import nc.sumy.edu.webcontainer.web.StaticContentHandler;
import nc.sumy.edu.webcontainer.web.StaticContentHandlerImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;
import static nc.sumy.edu.webcontainer.http.ResponseCode.NOT_FOUND;
import static org.apache.commons.lang3.StringUtils.*;

@SuppressWarnings("PMD")
public class RequestDispatcherImpl implements RequestDispatcher {

    private String relativePath;
    private Request request;
    private RequestWrapper requestWrapper;
    private Response response;
    private String errorPagesPath;
    //private ServerDispatcher dispatcher = new ServerDispatcher(new ServerConfigurationJson(), null);
    private final ServerConfiguration serverConfiguration = new ServerConfigurationJson();

    public RequestDispatcherImpl(String relativePath, RequestWrapper requestWrapper) {
        this.relativePath = relativePath;
        this.requestWrapper = requestWrapper;
        request = requestWrapper.getRequest();
    }

    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        response = getResponse(request);
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        for (Map.Entry<String,String> entry : response.getHeaders().entrySet()) {
            httpServletResponse.addHeader(entry.getKey(), entry.getValue());
        }
        servletResponse.getWriter().write(new String(response.getBody()));
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }


    /**
     * Below is the code of the class nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher
     */

    public Response getResponse(Request request) {
        this.request = request;
        errorPagesPath = serverConfiguration.getServerLocation() + File.separator + "default" + File.separator;
        makeResponse();
        return response;
    }

    private void makeResponse() {
        String pagePath = null;
        try {
            pagePath = requestWrapper.getServletContext().getResource(relativePath).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (createIndexPage(pagePath))
            return;
        if (createStaticOrJspPage(pagePath))
            return;
        if (createServletPage())
            return;
        createErrorPageResponse(NOT_FOUND);
        response.setHeader("Content-Length", Integer.toString(response.getBody().length));
    }

    private boolean createIndexPage(String pagePath) {
        File page = new File(pagePath);
        if (page.exists() && page.isDirectory()) {
            String index = "index.";
            File indexPage = new File(pagePath + File.separator + index + "html");
            if (indexPage.exists()) {
                createStaticPageResponse(page);
                return true;
            }
            indexPage = new File(pagePath + File.separator + index + "jsp");
            if (indexPage.exists()) {
                createJspPageResponse(page);
                return true;
            }
        }
        return false;
    }

    private boolean createStaticOrJspPage(String pagePath) {
        File page = new File(pagePath);
        if (page.exists()) {
            if (endsWith(pagePath, "." +  "jsp")) {
                createJspPageResponse(page);
                return true;
            } else {
                createStaticPageResponse(page);
                return true;
            }
        }
        return false;
    }

    private boolean createServletPage() {
        throw new UnsupportedOperationException();
//        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainData = deployment.getDomainsData();
//        for (Map.Entry<File, ConcurrentHashMap<String, Class>> domain : domainData.entrySet()) {
//            if (findUrlMapping(request, domain))
//                return true;
//
//        }
//        return false;
    }

//    @SuppressWarnings("PMD")
//    private boolean findUrlMapping(Request request, Map.Entry<File, ConcurrentHashMap<String, Class>> domain) {
//        String domainName = substring(domain.getKey().getPath(),
//                lastIndexOf(domain.getKey().getPath(), File.separator));
//        if (StringUtils.equals(request.getDomainName(), domainName)) {
//            for (Map.Entry<String, Class> servletPair : domain.getValue().entrySet()) {
//                if (endsWith(request.getUrn(), servletPair.getKey())) {
//                    ServletHandler handler = new ServletHandlerImpl();
//                    setSuccessHeaders((HttpResponse) response);
//                    response = handler.processServlet((HttpRequest) request, servletPair.getValue());
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private void createJspPageResponse(File page) {
        JspHandler handler = new JspHandlerImpl();
        response = handler.processJSP((HttpRequest) request, page);
        setSuccessHeaders((HttpResponse) response);
    }

    private void createStaticPageResponse(File page) {
        StaticContentHandler handler = new StaticContentHandlerImpl();
        response.setBody(handler.process(page));
        setSuccessHeaders((HttpResponse) response);
    }

    private void createErrorPageResponse(ResponseCode code) {
        String errorPageTitle = code.getString() + ".html";
        response = new HttpResponse(code.getCode());
        setErrorPageHeaders((HttpResponse) response);
        File errorPage = new File(errorPagesPath + errorPageTitle);
        StaticContentHandler handler = new StaticContentHandlerImpl();
        try {
            response.setBody(handler.process(errorPage));
        } catch (FileNotReadException e) {
            response.setBody(getResponseCode(code.getCode()).getBytes());
        }
    }

    private void setErrorPageHeaders(HttpResponse response){
        setDefaultHeaders(response);
        response.setHeader("Content-Type", "text/html");
        response.setHeader("Content-Language", "en");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    }

    private void setSuccessHeaders(HttpResponse response) {
        setDefaultHeaders(response);
        String temp[] = split(request.getUrn(), ".");
        String extension = temp[temp.length - 1];
        switch (extension) {
            case "html" : setContentType(response, "text/html");
                break;
            case "htm" : setContentType(response, "text/htm");
                break;
            case "css" : setContentType(response, "text/css");
                break;
            case "xml" : setContentType(response, "text/xml");
                break;
            case "jsp" : setContentType(response, "text/html");
                break;
            case "pdf" : setContentType(response, "application/pdf");
                break;
            case "zip" : setContentType(response, "application/zip");
                break;
            case "js"  : setContentType(response, "application/javascript");
                break;
            case "gif" : setContentType(response, "image/gif");
                break;
            case "jpeg" : setContentType(response, "image/jpeg");
                break;
            case "jpg" : setContentType(response, "image/jpg");
                break;
            case "swg" : setContentType(response, "image/swg");
                break;
            case "png" : setContentType(response, "image/png");
                break;
            default:  setContentType(response, "text/htm");
                break;
        }
        response.setHeader("Cache-Control", "public, max-age=60");
    }

    private void setContentType(HttpResponse response, String mime) {
        response.setHeader("Content-Type", mime + "; charset=utf-8");
    }

    private void setDefaultHeaders(HttpResponse response) {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader("Date", dateFormat.format(new Date()));
        response.setHeader("Server", "ServerLite");
        response.setHeader("Connection", "close");
    }
}

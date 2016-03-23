package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.common.FileNotReadException;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.*;
import nc.sumy.edu.webcontainer.sequrity.*;
import nc.sumy.edu.webcontainer.web.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;
import static nc.sumy.edu.webcontainer.dispatcher.PageType.*;
import static org.apache.commons.lang3.StringUtils.*;
import static nc.sumy.edu.webcontainer.dispatcher.Header.*;

/**
 * Class that takes a request, analyzes it and gives the output response.
 * @author Vinogradov Maxim
 */
@SuppressWarnings("PMD")
public class ServerDispatcher implements Dispatcher{
    private static final Logger LOG = LoggerFactory.getLogger(ServerDispatcher.class);
    private String errorPagesPath;
    private final ServerConfiguration serverConfiguration;
    private final Deployment deployment;
    private Security security;
    private Request request;
    private HttpResponse response;

    public ServerDispatcher(ServerConfiguration serverConfiguration, Deployment deployment) {
        this.deployment = deployment;
        this.serverConfiguration = serverConfiguration;
    }

    @Override
    public HttpResponse getResponse(Request request) {
        this.request = request;
        security = new ServerSecurity(request, serverConfiguration);
        errorPagesPath  = serverConfiguration.getWwwLocation() + File.separator + "default" + File.separator;
        makeResponse();
        return response;
    }

    private void makeResponse() {
        String pagePath = serverConfiguration.getWwwLocation() + File.separator + request.getUrn();
        if (initialInspection())
            return;
        if (!security.isAllow()) {
            createErrorPageResponse(FORBIDDEN);
            return;
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

    private boolean initialInspection() {
        if (Objects.isNull(request.getRequestText()) || isEmpty(request.getRequestText())) {
            createErrorPageResponse(BAD_REQUEST);
            return true;
        } else if (request.getMethod() == HttpMethod.OPTIONS) {
            setErrorPageHeaders(response);
            response.setBody("200 OK".getBytes());
            return true;
        } else if (request.getMethod() == HttpMethod.UNKNOWN) {
            createErrorPageResponse(NOT_ALLOWED);
            return true;
        }
        return false;
    }

    private boolean createIndexPage(String pagePath) {
        File page = new File(pagePath);
        if (page.exists() && page.isDirectory()) {
            String index = "index.";
            File indexPage = new File(pagePath + File.separator + index + HTML.getFileExtension());
            if (indexPage.exists()) {
                createStaticPageResponse(page);
                return true;
            }
            indexPage = new File(pagePath + File.separator + index + JSP.getFileExtension());
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
            if (endsWith(pagePath, "." +  JSP.getFileExtension())) {
                createJspPageResponse(page);
                return true;
            } else {
                createStaticPageResponse(page);
                return true;
            }
        }
        return false;
    }

    //Smth could happened here =(
    private boolean createServletPage() {
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainData = deployment.getDomainsData();
        for (Map.Entry<File, ConcurrentHashMap<String, Class>> domain : domainData.entrySet()) {
            if (findUrlMapping(request, domain))
                return true;

        }
        return false;
    }

    @SuppressWarnings("PMD")
    private boolean findUrlMapping(Request request, Map.Entry<File, ConcurrentHashMap<String, Class>> domain) {
        String domainName = substring(domain.getKey().getPath(),
                lastIndexOf(domain.getKey().getPath(), File.separator));
        if (StringUtils.equals(request.getDomainName(), domainName)) {
            for (Map.Entry<String, Class> servletPair : domain.getValue().entrySet()) {
                if (endsWith(request.getUrn(), servletPair.getKey())) {
                    ServletHandler handler = new ServletHandlerImpl();
                    setSuccessHeaders(response);
                    response = handler.processServlet((HttpRequest) request, servletPair.getValue());
                    return true;
                }
            }
        }
        return false;
    }

    private void createJspPageResponse(File page) {
        JspHandler handler = new JspHandlerImpl();
        response = handler.processJSP((HttpRequest) request, page);
        setSuccessHeaders(response);
    }

    private void createStaticPageResponse(File page) {
        WebHandler handler = new WebHandlerImpl();
        response.setBody(handler.process(page).getBytes());
        setSuccessHeaders(response);
    }

    private void createErrorPageResponse(ResponseCode code) {
        String errorPageTitle = code.getString() + ".html";
        response = new HttpResponse(code.getCode());
        setErrorPageHeaders(response);
        File errorPage = new File(errorPagesPath + errorPageTitle);
        WebHandler handler = new WebHandlerImpl();
        try {
            response.setBody(handler.process(errorPage).getBytes());
        } catch (FileNotReadException e) {
            response.setBody(getResponseCode(code.getCode()).getBytes());
            LOG.warn("Cannot find or read default page " + errorPageTitle, e);
        }
    }

    private void setErrorPageHeaders(HttpResponse response){
        setDefaultHeaders(response);
        response.setHeader(CONTENT_TYPE.getHeader(), "text/html");
        response.setHeader(CONTENT_LANGUAGE.getHeader(), "en");
        response.setHeader(CACHE_CONTROL.getHeader(), "no-cache");
        response.setHeader(PRAGMA.getHeader(), "no-cache");
    }

    @SuppressWarnings("PMD")
    private void setSuccessHeaders(HttpResponse response) {
        setDefaultHeaders(response);
        String temp[] = split(request.getUrn(), ".");
        String extension = temp[temp.length - 1];
        switch (extension) {
            case "html" : setContentType(response, HTML.getMIME());
                break;
            case "htm" : setContentType(response, HTM.getMIME());
                break;
            case "css" : setContentType(response, CSS.getMIME());
                break;
            case "xml" : setContentType(response, XML.getMIME());
                break;
            case "jsp" : setContentType(response, JSP.getMIME());
                break;
            case "pdf" : setContentType(response, PDF.getMIME());
                break;
            case "zip" : setContentType(response, ZIP.getMIME());
                break;
            case "js"  : setContentType(response, JAVASCRIPT.getMIME());
                break;
            case "gif" : setContentType(response, GIF.getMIME());
                break;
            case "jpeg" : setContentType(response, JPEG.getMIME());
                break;
            case "jpg" : setContentType(response, JPG.getMIME());
                break;
            case "swg" : setContentType(response, SWG.getMIME());
                break;
            case "png" : setContentType(response, PNG.getMIME());
                break;
            default:  setContentType(response, HTML.getMIME());
                break;
        }
        response.setHeader(CACHE_CONTROL.getHeader(), "public, max-age=60");
    }

    private void setContentType(HttpResponse response, String mime) {
        response.setHeader(CONTENT_TYPE.getHeader(), mime + "; charset=utf-8");
    }

    private void setDefaultHeaders(HttpResponse response) {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader(DATE.getHeader(), dateFormat.format(new Date()));
        response.setHeader(SERVER.getHeader(), "ServerLite");
        response.setHeader(CONNECTION.getHeader(), "close");
    }

}
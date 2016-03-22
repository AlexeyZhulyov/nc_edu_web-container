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
import java.util.concurrent.ConcurrentHashMap;

import static java.text.DateFormat.getTimeInstance;
import static java.util.Objects.isNull;
import static java.util.TimeZone.getTimeZone;
import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;
import static nc.sumy.edu.webcontainer.dispatcher.PageType.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that takes a request, analyzes it and gives the output response.
 * @author Vinogradov Maxim
 */
public class ServerDispatcher implements Dispatcher{
    private static final Logger LOG = LoggerFactory.getLogger(ServerDispatcher.class);
    private String errorPagesPath;
    private final ServerConfiguration serverConfiguration;
    private Deployment deployment;
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
        String pagePath = serverConfiguration.getWwwLocation() + File.separator + request.getFilePath();
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
    }

    private boolean initialInspection() {
        if (isNull(request.getRequestText()) || isEmpty(request.getRequestText())) {
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

    private boolean createServletPage() {
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainData = deployment.getDomainsData();
        String domainName;
        for (Map.Entry<File, ConcurrentHashMap<String, Class>> domain : domainData.entrySet()) {
            domainName = substring(domain.getKey().getPath(),
                    lastIndexOf(domain.getKey().getPath(), File.separator));
            if (StringUtils.equals(request.getHeader("Host"), domainName)) {
                for (Map.Entry<String, Class> servletPair:  domain.getValue().entrySet()) {
                    if (endsWith(request.getFilePath(), servletPair.getKey())) {
                        ServletHandler handler = new ServletHandlerImpl();
                        handler.processServlet((HttpRequest) request, servletPair.getValue());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void createJspPageResponse(File page) {
        JspHandler handler = new JspHandlerImpl();
        response = handler.processJSP((HttpRequest) request, page);
    }

    private void createStaticPageResponse(File page) {
        WebHandler handler = new WebHandlerImpl();
        response.setBody(handler.process(page).getBytes());
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
        DateFormat df = getTimeInstance();
        df.setTimeZone(getTimeZone("GMT"));
        response.setHeader("Date:", df.format(new Date()));
        response.setHeader("Content-Type", "text/html");
        response.setHeader("Content-Language", "en");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "close");
    }

}
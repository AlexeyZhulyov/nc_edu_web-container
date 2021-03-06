package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.cgi.CgiHandlerImpl;
import nc.sumy.edu.webcontainer.common.FileNotReadException;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.*;
import nc.sumy.edu.webcontainer.sequrity.Security;
import nc.sumy.edu.webcontainer.sequrity.ServerSecurity;
import nc.sumy.edu.webcontainer.web.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import static nc.sumy.edu.webcontainer.dispatcher.Header.*;
import static nc.sumy.edu.webcontainer.dispatcher.PageType.*;
import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that takes a request, analyzes it and gives the output response.
 * @author Vinogradov M.O.
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
        errorPagesPath  = serverConfiguration.getServerLocation() + File.separator +
                "www" + File.separator + "default" + File.separator;
        makeResponse();
        return response;
    }

    private void makeResponse() {
        String pagePath = serverConfiguration.getServerLocation() + File.separator + "www" + File.separator
                + request.getUrn().replace("/",File.separator);
        if (initialInspection())
            return;
        if (!security.isAllow()) {
            createErrorPageResponse(FORBIDDEN);
            return;
        }
        if (createIndexPage(pagePath))
            return;
        if (createCgiPage(pagePath))
            return;
        if (createStaticOrJspPage(pagePath)) {
            makeRedirection();
            return;
        }
        if (createServletPage()) {
            makeRedirection();
            return;
        }
        createErrorPageResponse(NOT_FOUND);
    }

    private boolean initialInspection() {
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response = new HttpResponse(OK.getCode());
            setErrorPageHeaders(response);
            response.setBody("200 OK".getBytes());
            return true;
        } else if (request.getMethod() == HttpMethod.UNKNOWN) {
            createErrorPageResponse(NOT_ALLOWED);
            return true;
        }
        return false;
    }

    private void makeRedirection() {
        response.setHeader("Content-Length", Integer.toString(response.getBody().length));
        if (isNotEmpty(response.getRedirectUrl())) {
            request.setUrn(response.getRedirectUrl());
            makeResponse();
        }
    }

    private boolean createIndexPage(String pagePath) {
        File page = new File(pagePath);
        if (page.exists() && page.isDirectory()) {
            if(!endsWith(pagePath.replace("\\", "/"), "/")) {
                createRedirectPage();
                return true;
            }
            String index = "index.";
            File indexPage = new File(pagePath + File.separator + index + HTML.getFileExtension());
            if (indexPage.exists()) {
                createStaticPageResponse(indexPage);
                return true;
            }
            indexPage = new File(pagePath + File.separator + index + JSP.getFileExtension());
            if (indexPage.exists()) {
                createJspPageResponse(indexPage);
                return true;
            }
            createFileListPageResponse(page.listFiles());
            return true;
        }
        return false;
    }

    private void createRedirectPage() {
        response = new HttpResponse(OK.getCode());
        response.setHeader("Refresh", "0;url=" + request.getUrn() + "/");
        response.setBody(new byte[0]);
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

    private boolean createCgiPage(String pagePath) {
        File page = new File(pagePath);
        if (page.exists() && endsWith(pagePath, "." +  CGI.getFileExtension())) {
            createCgiPageResponse(request.getParameters());
            return true;
        }
        return false;
    }

    private void createCgiPageResponse(Map<String, String> parameters) {
        response = new HttpResponse(OK.getCode());
        String[] classNameArr = split(request.getUrn(), "/");
        String classNameWithExtension = classNameArr[classNameArr.length-1];
        String className = split(classNameWithExtension, ".")[0];
        CgiHandlerImpl cgi= new CgiHandlerImpl();
        String responseBody = cgi.process(className, parameters);
        response.setBody(responseBody.getBytes());
    }

    private boolean createServletPage() {
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainData = deployment.getDomainsData();
        for (Map.Entry<File, ConcurrentHashMap<String, Class>> domain : domainData.entrySet()) {
            if (findUrlMapping(request, domain)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("PMD")
    private boolean findUrlMapping(Request request, Map.Entry<File, ConcurrentHashMap<String, Class>> domain) {
        String domainName = substring(domain.getKey().getPath(),
                lastIndexOf(domain.getKey().getPath(), File.separator)).replace(File.separatorChar, '/');
        if (StringUtils.equals(request.getDomainName(), domainName)) {
            for (Map.Entry<String, Class> servletPair : domain.getValue().entrySet()) {
                if (endsWith(request.getUrn(), servletPair.getKey())) {
                    ServletHandler handler = new ServletHandlerImpl();
                    response = handler.processServlet((HttpRequest) request, servletPair.getValue());
                    setSuccessHeaders(response);
                    return true;
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
        response = new HttpResponse(OK.getCode());
        StaticContentHandler handler = new StaticContentHandlerImpl();
        response.setBody(handler.process(page));
        setSuccessHeaders(response);
    }

    private void createFileListPageResponse(File[] filesList) {
        response = new HttpResponse(OK.getCode());
        StaticContentHandler handler = new StaticContentHandlerImpl();
        byte[] top = handler.process(new File(errorPagesPath + "filesListTop.html"));
        byte[] bottom = handler.process(new File(errorPagesPath + "filesListBottom.html"));
        StringBuilder buider = new StringBuilder();
        buider.append("<ul>");
        for (File f: filesList) {
            buider.append("<li class=\"files-list-item\">");
            if(f.isDirectory()) {
                buider.append("Folder - ");
            }
            else{
                buider.append("File - ");
            }
            buider.append(f.getName());
            buider.append("</li>");
        }
        buider.append("</ul>");
        byte[] middle = new String(buider).getBytes();
        byte[] body = new byte[top.length + middle.length + bottom.length];
        System.arraycopy(top, 0, body, 0, top.length);
        System.arraycopy(middle, 0, body, top.length, middle.length);
        System.arraycopy(bottom, 0, body, top.length + middle.length, bottom.length);

        response.setBody(body);
        setSuccessHeaders(response);
    }

    protected void createErrorPageResponse(ResponseCode code) {
        String errorPageTitle = code.getString() + ".html";
        response = new HttpResponse(code.getCode());
        setErrorPageHeaders(response);
        File errorPage = new File(errorPagesPath + errorPageTitle);
        StaticContentHandler handler = new StaticContentHandlerImpl();
        try {
            response.setBody(handler.process(errorPage));
        } catch (FileNotReadException | StaticContentFileException e) {
            response.setBody(getResponseCode(code.getCode()).getBytes());
            LOG.warn("Cannot find or read default page " + errorPageTitle, e);
        }
    }

    private void setErrorPageHeaders(HttpResponse response){
        setDefaultHeaders(response);
        setContentType(response, HTML.getMIME());
        response.setHeader(CONTENT_LANGUAGE.getHeader(), "en");
        response.setHeader(CACHE_CONTROL.getHeader(), "no-cache");
        response.setHeader(PRAGMA.getHeader(), "no-cache");
    }

    @SuppressWarnings("PMD")
    private void setSuccessHeaders(HttpResponse response) {
        setDefaultHeaders(response);
        String temp[] = split(request.getUrn(), ".");
        String extension = temp[temp.length - 1];
        setContentType(response, DEFAULT.getMimeViaExtension(extension));
        response.setHeader(CACHE_CONTROL.getHeader(), "public, max-age=0");
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
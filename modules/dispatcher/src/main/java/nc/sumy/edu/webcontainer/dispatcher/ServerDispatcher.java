package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.cgi.CgiHandlerImpl;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.*;
import nc.sumy.edu.webcontainer.sequrity.Security;
import nc.sumy.edu.webcontainer.sequrity.ServerSecurity;
import nc.sumy.edu.webcontainer.web.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static nc.sumy.edu.webcontainer.dispatcher.PageType.*;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Class that takes a request, analyzes it and gives the output response.
 */
public class ServerDispatcher implements Dispatcher{
    private final ServerConfiguration serverConfiguration;
    private final Deployment deployment;
    private Security security;
    private Request request;
    private HttpResponse response;
    private ServerDispatcherUtils utils;

    public ServerDispatcher(ServerConfiguration serverConfiguration, Deployment deployment) {
        this.deployment = deployment;
        this.serverConfiguration = serverConfiguration;
    }

    @Override
    public HttpResponse getResponse(Request request) {
        this.request = request;
        security = new ServerSecurity(request, serverConfiguration);
        String errorPagesPath = serverConfiguration.getWwwLocation() + File.separator +
                "www" + File.separator + "default" + File.separator;
        utils = new ServerDispatcherUtils(errorPagesPath, this.request, response);
        makeResponse();
        return response;
    }

    private void makeResponse() {
        String pagePath = serverConfiguration.getWwwLocation() + File.separator + "www" + File.separator
                + request.getUrn().replace("/",File.separator);
        if (initialInspection())
            return;
        if (!security.isAllow()) {
            utils.createErrorPageResponse(FORBIDDEN);
            return;
        }
        if (createIndexPage(pagePath))
            return;
        if (createCgiPage(pagePath))
            return;
        if (createStaticOrJspPage(pagePath))
            return;
        if (createServletPage())
            return;
        utils.createErrorPageResponse(NOT_FOUND);
        response.setHeader("Content-Length", Integer.toString(response.getBody().length));
    }

    private boolean initialInspection() {
        if (request.getMethod() == HttpMethod.OPTIONS) {
            utils.setErrorPageHeaders(response);
            response.setBody("200 OK".getBytes());
            return true;
        } else if (request.getMethod() == HttpMethod.UNKNOWN) {
            utils.createErrorPageResponse(NOT_ALLOWED);
            return true;
        }
        return false;
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
                utils.createStaticPageResponse(indexPage);
                return true;
            }
            indexPage = new File(pagePath + File.separator + index + JSP.getFileExtension());
            if (indexPage.exists()) {
                utils.createJspPageResponse(indexPage);
                return true;
            }
            utils.createFileListPageResponse(page.listFiles());
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
                utils.createJspPageResponse(page);
                return true;
            } else {
                utils.createStaticPageResponse(page);
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
                    utils.setSuccessHeaders(response);
                    return true;
                }
            }
        }
        return false;
    }

}
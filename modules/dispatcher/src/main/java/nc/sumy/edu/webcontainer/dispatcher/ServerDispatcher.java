package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.HttpMethod;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.ResponseCode;
import nc.sumy.edu.webcontainer.sequrity.Security;
import nc.sumy.edu.webcontainer.sequrity.ServerSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.isNull;
import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;

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
        this.serverConfiguration    = serverConfiguration;
    }

    @Override
    public HttpResponse getResponse(Request request) {
        this.request = request;
        security   = new ServerSecurity(request, serverConfiguration);
        errorPagesPath  = serverConfiguration.getWwwLocation() + File.separator + "default" + File.separator;
        makeResponse();
        return null;
    }

    private void makeResponse() {
        if (isNull(request.getRequestText())) {
            createErrorPageResponse(BAD_REQUEST);
        } else if (request.getMethod() == HttpMethod.OPTIONS) {
            setDefaultPageHeaders(response);
            response.setBody("200 OK".getBytes());
        } else if (request.getMethod() == HttpMethod.UNKNOWN) {
            createErrorPageResponse(NOT_ALLOWED);
        } else if (!security.isAllow()) {
            createErrorPageResponse(FORBIDDEN);
        } else if (isItServlet()) {

        } else if (isPageNotFound()) {
            createErrorPageResponse(NOT_FOUND);
        } else if (isItJsp()) {

        } else {

        }
    }

    private boolean isItServlet() {
        return false;
    }

    private boolean isItJsp() {
        return false;
    }

    private boolean isPageNotFound() {
        return false;
    }

    private void createErrorPageResponse(ResponseCode code) {
        String errorPageTitle = code.getString() + ".html";
        response = new HttpResponse(code.getCode());
        setDefaultPageHeaders(response);
        File errorPage = new File(errorPagesPath + errorPageTitle);
        try {
            response.setBody(readAllBytes(errorPage.toPath()));
        } catch (IOException e) {
            response.setBody(getResponseCode(code.getCode()).getBytes());
            LOG.warn("Cannot find or read default page " + errorPageTitle, e);
        }
    }

    private void setDefaultPageHeaders(HttpResponse response){
        response.setHeader("Content-Type", "text/html");
        response.setHeader("Content-Language", "en");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "close");
    }


}

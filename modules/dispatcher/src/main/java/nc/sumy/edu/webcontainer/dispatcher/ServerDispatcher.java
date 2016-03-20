package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.HttpMethod;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.sequrity.Security;
import nc.sumy.edu.webcontainer.sequrity.ServerSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.isNull;
import static nc.sumy.edu.webcontainer.http.HttpResponse.getResponseCode;

/**
 * Class that takes a request, analyzes it and gives the output response.
 * @author Vinogradov Maxim
 */
public class ServerDispatcher implements Dispatcher{
    private static final Logger LOG = LoggerFactory.getLogger(ServerDispatcher.class);
    private final String errorPagesPath;
    private ServerConfiguration serverConfiguration = new ServerConfigurationJson();
    private Deployment deployment;
    private Security security;
    private Request request;
    private HttpResponse response;

    public ServerDispatcher(Request request, Deployment deployment) {
        this.deployment = deployment;
        this.request    = request;
        this.security   = new ServerSecurity(request, serverConfiguration);
        errorPagesPath  = serverConfiguration.getWwwLocation() + File.separator + "default" + File.separator;
        makeResponse();
    }

    private void makeResponse() {
        if (isNull(request.getRequestText())) {
            createErrorPageResponse(400);
        } else if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Connection", "close");
            response.setBody("200 OK".getBytes());
        } else if (request.getMethod() == HttpMethod.UNKNOWN) {
            createErrorPageResponse(405);
        } else if (!security.isAllow()) {
            createErrorPageResponse(403);
        } else if (isItServlet()) {

        } else if (isPageNotFound()) {
            createErrorPageResponse(404);
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

    private void createErrorPageResponse(int code) {
        String errorPageTitle = Integer.toString(code) + ".html";
        response = new HttpResponse(code);
        response.setHeader("Content-Type", "text/html");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Connection", "close");
        File errorPage = new File(errorPagesPath + errorPageTitle);
        /*TODO try to find another variant of reading bytes*/
        try {
            response.setBody(readAllBytes(errorPage.toPath()));
        } catch (IOException e) {
            response.setBody(getResponseCode(code).getBytes());
            LOG.warn("Cannot find or read default page " + errorPageTitle, e);
        }
    }

    @Override
    public HttpResponse getResponse(Request request) {
        return null;
    }

    @Override
    public HttpResponse getResponse() {
        return null;
    }

}

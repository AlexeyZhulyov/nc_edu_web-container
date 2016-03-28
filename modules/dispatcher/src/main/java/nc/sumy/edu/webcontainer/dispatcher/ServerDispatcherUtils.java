package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.common.FileNotReadException;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.ResponseCode;
import nc.sumy.edu.webcontainer.web.JspHandler;
import nc.sumy.edu.webcontainer.web.JspHandlerImpl;
import nc.sumy.edu.webcontainer.web.WebHandler;
import nc.sumy.edu.webcontainer.web.WebHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import static nc.sumy.edu.webcontainer.dispatcher.Header.*;
import static nc.sumy.edu.webcontainer.dispatcher.PageType.*;
import static nc.sumy.edu.webcontainer.http.ResponseCode.*;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * Util-class for ServerDispatcher that support it to analyze HttpRequest and make HttpResponse.
 */
public class ServerDispatcherUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ServerDispatcherUtils.class);
    private final String errorPagesPath;
    private final Request request;
    private HttpResponse response;

    public ServerDispatcherUtils(String errorPagesPath, Request request, HttpResponse response) {
        this.errorPagesPath = errorPagesPath;
        this.request = request;
        this.response = response;
    }

    void createJspPageResponse(File page) {
        JspHandler handler = new JspHandlerImpl();
        response = handler.processJSP((HttpRequest) request, page);
    }

    void createStaticPageResponse(File page) {
        response = new HttpResponse(OK.getCode());
        WebHandler handler = new WebHandlerImpl();
        response.setBody(handler.process(page));
        setSuccessHeaders(response);
    }

    void createFileListPageResponse(File[] filesList) {
        response = new HttpResponse(OK.getCode());
        WebHandler handler = new WebHandlerImpl();
        byte[] top = handler.process(new File(errorPagesPath + "filesListTop.html"));
        byte[] bottom = handler.process(new File(errorPagesPath + "filesListBottom.html"));
        StringBuilder builder = new StringBuilder(51);
        builder.append("<ul>");
        for (File file: filesList) {
            builder.append("<li class=\"files-list-item\">");
            if(file.isDirectory()) {
                builder.append("Folder - ");
            }
            else{
                builder.append("File - ");
            }
            builder.append(file.getName());
            builder.append("</li>");
        }
        builder.append("</ul>");
        byte[] middle = new String(builder).getBytes();
        byte[] body = new byte[top.length + middle.length + bottom.length];
        System.arraycopy(top, 0, body, 0, top.length);
        System.arraycopy(middle, 0, body, top.length, middle.length);
        System.arraycopy(bottom, 0, body, top.length + middle.length, bottom.length);
        response.setBody(body);
        setSuccessHeaders(response);
    }

    void createErrorPageResponse(ResponseCode code) {
        String errorPageTitle = code.toString() + ".html";
        response = new HttpResponse(code.getCode());
        setErrorPageHeaders(response);
        File errorPage = new File(errorPagesPath + errorPageTitle);
        WebHandler handler = new WebHandlerImpl();
        try {
            response.setBody(handler.process(errorPage));
        } catch (FileNotReadException e) {
            response.setBody(HttpResponse.getResponseCode(code.getCode()).getBytes());
            LOG.warn("Cannot find or read default page " + errorPageTitle, e);
        }
    }

    void setErrorPageHeaders(HttpResponse response){
        setDefaultHeaders(response);
        response.setHeader(CONTENT_TYPE.getHeader(), "text/html");
        response.setHeader(CONTENT_LANGUAGE.getHeader(), "en");
        response.setHeader(CACHE_CONTROL.getHeader(), "no-cache");
        response.setHeader(PRAGMA.getHeader(), "no-cache");
    }

    @SuppressWarnings("PMD")
    void setSuccessHeaders(HttpResponse response) {
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
        response.setHeader(CACHE_CONTROL.getHeader(), "public, max-age=0");
    }

    void setContentType(HttpResponse response, String mime) {
        response.setHeader(CONTENT_TYPE.getHeader(), mime + "; charset=utf-8");
    }

    void setDefaultHeaders(HttpResponse response) {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        response.setHeader(DATE.getHeader(), dateFormat.format(new Date()));
        response.setHeader(SERVER.getHeader(), "ServerLite");
        response.setHeader(CONNECTION.getHeader(), "close");
    }

}

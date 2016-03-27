package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;

import java.io.File;

public interface JspHandler {
    HttpResponse processJSP(HttpRequest request, File file);
}

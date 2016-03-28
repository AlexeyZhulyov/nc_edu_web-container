package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;

import java.io.File;

/**
* Defines method for processing jsps.
*/
public interface JspHandler {
    HttpResponse processJSP(HttpRequest request, File file);
}

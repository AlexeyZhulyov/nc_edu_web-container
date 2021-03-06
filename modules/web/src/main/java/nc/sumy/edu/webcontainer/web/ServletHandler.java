package nc.sumy.edu.webcontainer.web;

import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.HttpResponse;

/**
* Defines method for processing servlets.
*/
public interface ServletHandler {
    HttpResponse processServlet(HttpRequest request, Class klass);
}

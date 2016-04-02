package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;

/**
 * Interface that provides a method to create a response based on the request data.
 */
public interface Dispatcher {

    HttpResponse getResponse(Request request);

}

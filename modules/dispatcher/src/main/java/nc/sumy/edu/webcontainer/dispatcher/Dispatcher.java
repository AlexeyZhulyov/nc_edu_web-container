package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;

/**
 * Interface that provides getting response on getting request.
 * @version 0.1
 */
public interface Dispatcher {

    HttpResponse getResponse(Request request);

}

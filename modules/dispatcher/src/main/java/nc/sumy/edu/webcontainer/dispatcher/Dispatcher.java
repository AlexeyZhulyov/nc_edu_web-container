package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;

/**
 * Interface that provides methods for working with dispatcher implementation.
 * @author Vinogradov M.O.
 */
public interface Dispatcher {

    HttpResponse getResponse(Request request);

}

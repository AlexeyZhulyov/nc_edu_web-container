package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;

public interface Dispatcher {

    HttpResponse getResponse(Request request);

}

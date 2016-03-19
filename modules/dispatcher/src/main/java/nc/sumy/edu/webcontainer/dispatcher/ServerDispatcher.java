package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;

/**
 * Class that takes a request, analyzes it and gives the output response.
 * @author Vinogradov Maxim
 */
public class ServerDispatcher implements Dispatcher{

    public ServerDispatcher(ServerConfiguration configuration, Deployment deployment) {

    }

    @Override
    public HttpResponse getResponse(Request request) {
        return null;
    }
    
}

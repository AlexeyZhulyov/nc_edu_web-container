import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.AutoDeployment;
import nc.sumy.edu.webcontainer.dispatcher.Dispatcher;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import nc.sumy.edu.webcontainer.listener.ModelSocketProcessing;
import org.junit.Test;

import java.net.Socket;

public class
ModelSocketProcessingTest {
    Dispatcher dispatcher = new Dispatcher() {
        @Override
        public HttpResponse getResponse(Request request) {
            return null;
        }
    };

    ModelSocketProcessing model = new ModelSocketProcessing(dispatcher);
    @Test
    public void simpleSocketTest() {
        Socket socket = new Socket();
        model.processRequest(socket);
        //compare result

    }
}

import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.AutoDeployment;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.listener.ModelSocketProcessing;
import org.junit.Test;

import java.net.Socket;

public class ModelSocketProcessingTest {
    ModelSocketProcessing model = new ModelSocketProcessing(new ServerDispatcher(new ServerConfigurationJson(),
            new AutoDeployment(new ServerConfigurationJson())));
    @Test
    public void simpleSocketTest() {

    }
}

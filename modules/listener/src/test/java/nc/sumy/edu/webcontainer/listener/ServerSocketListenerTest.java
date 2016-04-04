package nc.sumy.edu.webcontainer.listener;


import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ServerSocketListenerTest {
    //create
    ServerConfiguration config = new ServerConfigurationJson();
    Deployment deployment = mock(Deployment.class);

    @Test
    public void serverSocketListnerCreationTest() throws IOException {
        new ServerSocketListener(config, deployment);
    }

    //create on used port
    @Test(expected = IOException.class)
    public void listenerCreationOnUsedPortTest() throws IOException {
        new ServerSocket(7121);
        config.setPort(7121);
        new ServerSocketListener(config, deployment);
    }

}

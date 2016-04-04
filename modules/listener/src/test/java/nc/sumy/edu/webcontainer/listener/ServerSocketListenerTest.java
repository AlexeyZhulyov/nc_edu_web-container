package nc.sumy.edu.webcontainer.listener;


import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ServerSocketListenerTest {
    //create
    ServerConfiguration config = new ServerConfigurationJson();
    Deployment deployment = mock(Deployment.class);

    @Test
    public void serverSocketListenerCreationTest() throws IOException, InterruptedException {
        ServerSocketListener listener = new ServerSocketListener(config, deployment);
        listener.start();
        Thread.sleep(2000);
        listener.stopListening();
    }

    //create on used port
    @Test(expected = IOException.class)
    public void listenerCreationOnUsedPortTest() throws IOException {
        config.setPort(7121);
        try (ServerSocket serverSocket = new ServerSocket(7121)) {
            new ServerSocketListener(config, deployment);
        }

    }

    @Test
    public void serverSocketListenerWorkTest() throws IOException, InterruptedException {
        ServerSocketListener listener =  new ServerSocketListener(config, deployment);
        try(Socket socketOnClientSide = new Socket("localhost", config.getPort());
            OutputStream clientOutput = socketOnClientSide.getOutputStream();
            InputStream clientInput = socketOnClientSide.getInputStream())
        {
            ModelSocketProcessing model = mock(ModelSocketProcessing.class);
            when(model.readStringFromSocket(any())).thenCallRealMethod();
            when(model.processRequest(any())).thenCallRealMethod();
            ServerDispatcher dispatcher = mock(ServerDispatcher.class);
            HttpResponse response = new HttpResponse(200);
            response.setBody("This is good response".getBytes());
            when(dispatcher.getResponse(any())).thenReturn(response);
            when(model.getDispatcher()).thenReturn(dispatcher);

            listener.setModel(model);
            listener.start();
            //write request

            clientOutput.write("Test request\n\n".getBytes());
            Thread.sleep(3000);
            BufferedReader clientBufferedReader = new BufferedReader(
                    new InputStreamReader(clientInput));
            StringBuilder requestStringBuilder = new StringBuilder();
            String temp;
            while ((temp = clientBufferedReader.readLine()) != null) {
                if ("".equals(temp)) {
                    break;
                }
                requestStringBuilder.append(temp);
                requestStringBuilder.append("\r\n");
            }
            String actual = new String(requestStringBuilder);
            assertEquals("Response must be", "HTTP/1.1 200 OK\n", actual.replace("\r", ""));
        } finally {
            listener.stopListening();
        }
    }

}

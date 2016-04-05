package nc.sumy.edu.webcontainer.listener;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static nc.sumy.edu.webcontainer.common.ClassUtil.readInputStreamToString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class
ModelSocketProcessingTest {
    private static final String DOMAIN ="localhost";


    @Test
    public void readingFromSocketTest() throws IOException {
        Deployment deployment = mock(Deployment.class);
        ServerConfiguration config = mock(ServerConfiguration.class);
        ModelSocketProcessing model = new ModelSocketProcessing(config,deployment);
        ServerSocket serverSocket = new ServerSocket(7002);
        Socket socketOnClientSide = new Socket(DOMAIN, 7002);
        //write request
        OutputStream clientOutput = socketOnClientSide.getOutputStream();
        clientOutput.write("Test request\n\n".getBytes());
        Socket socketOnServerSide = serverSocket.accept();
        String actual = model.readStringFromSocket(socketOnServerSide.getInputStream());
        assertEquals("Read string must be 'Test request'","Test request\n", actual.replace("\r", "") );
        socketOnClientSide.close();
        serverSocket.close();
    }

    @Test(expected = IOException.class)
    public void readingFromClosedSocketTest() throws IOException {
        Deployment deployment = mock(Deployment.class);
        ServerConfiguration config = mock(ServerConfiguration.class);
        ModelSocketProcessing model = new ModelSocketProcessing(config,deployment);
        try(ServerSocket serverSocket = new ServerSocket(7002);
            Socket socketOnClientSide = new Socket(DOMAIN, 7002);
            Socket socketOnServerSide = serverSocket.accept(); ) {
            socketOnServerSide.close();
            model.readStringFromSocket(socketOnServerSide.getInputStream());
        }
    }

    @Test
    public void processingClosedSocketTest() throws IOException {
        ModelSocketProcessing model = mock(ModelSocketProcessing.class);
        when(model.readStringFromSocket(any())).thenCallRealMethod();
        when(model.processRequest(any())).thenCallRealMethod();
        ServerDispatcher dispatcher = mock(ServerDispatcher.class);
        when(model.getDispatcher()).thenReturn(dispatcher);
        ServerSocket serverSocket = new ServerSocket(7003);
        Socket socketOnClientSide = new Socket(DOMAIN, 7003);
        Socket socketOnServerSide = serverSocket.accept();
        socketOnServerSide.close();
        boolean resultOfProcessing = model.processRequest(socketOnServerSide);
        assertEquals("Processing of closed socket must return false", false, resultOfProcessing);
        socketOnClientSide.close();
        serverSocket.close();

    }
    @Test
    public void processingEmptySocketTest() throws IOException {
        ModelSocketProcessing model = mock(ModelSocketProcessing.class);
        when(model.readStringFromSocket(any())).thenCallRealMethod();
        when(model.processRequest(any())).thenCallRealMethod();
        ServerDispatcher dispatcher = mock(ServerDispatcher.class);
        when(model.getDispatcher()).thenReturn(dispatcher);
        ServerSocket serverSocket = new ServerSocket(7003);
        Socket socketOnClientSide = new Socket(DOMAIN, 7003);
        OutputStream clientOutput = socketOnClientSide.getOutputStream();
        clientOutput.write("\n".getBytes());
        Socket socketOnServerSide = serverSocket.accept();
        boolean resultOfProcessing = model.processRequest(socketOnServerSide);
        String response = readInputStreamToString(socketOnClientSide.getInputStream());
        assertEquals("Processing of empty socket must return true", true, resultOfProcessing);
        assertEquals("Processing of empty socket must return empty response", "", response);
        socketOnClientSide.close();
        serverSocket.close();
    }

    @Test
    public void processingSocketTest() throws IOException {
        ModelSocketProcessing model = mock(ModelSocketProcessing.class);
        when(model.readStringFromSocket(any())).thenCallRealMethod();
        when(model.processRequest(any())).thenCallRealMethod();
        ServerDispatcher dispatcher = mock(ServerDispatcher.class);
        HttpResponse response = new HttpResponse(200);
        response.setBody("This is good response".getBytes());
        when(dispatcher.getResponse(any())).thenReturn(response);
        when(model.getDispatcher()).thenReturn(dispatcher);
        ServerSocket serverSocket = new ServerSocket(7002);
        Socket socketOnClientSide = new Socket(DOMAIN, 7002);
        OutputStream clientOutput = socketOnClientSide.getOutputStream();
        clientOutput.write("Test request\n\n".getBytes());
        Socket socketOnServerSide = serverSocket.accept();
        model.processRequest(socketOnServerSide);
        String actual = readInputStreamToString(socketOnClientSide.getInputStream());
        assertEquals("Response must be", "HTTP/1.1 200 OK\n", actual.replace("\r", ""));
        serverSocket.close();
        socketOnClientSide.close();
        clientOutput.close();
    }

/*
    @Test
    public void simpleSocketTest() {
        ServerSocket serverSocket = null;
        Socket socketOnClientSide = null;
        Socket socketOnServerSide = null;
        OutputStream clientOutput = null;
        InputStream inputStream = null;
        try {//create sockets
            serverSocket = new ServerSocket(7013);
            socketOnClientSide = new Socket("localhost", 7013);
            socketOnServerSide = serverSocket.accept();
            //write request
            clientOutput = socketOnClientSide.getOutputStream();
            clientOutput.write("Test request".getBytes());
            //process request
            model.processRequest(socketOnServerSide);
            //compare results
            inputStream = socketOnClientSide.getInputStream();
            String requestString = IOUtil.toString(inputStream, String.valueOf(Charset.defaultCharset()));
            Assert.assertEquals("Test request", "HTTP/1.1 200 OK\n\nTest request".replace("\r", ""), requestString.replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
                socketOnClientSide.close();
                socketOnServerSide.close();
                clientOutput.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void failedSocketProcessingTest() throws IOException {
        ServerConfigurationJson config = new ServerConfigurationJson();
        ModelSocketProcessing model = new ModelSocketProcessing(config, new AutoDeployment(config));
        //create sockets
        ServerSocket serverSocket = new ServerSocket(7002);
        Socket socketOnClientSide = new Socket("localhost", 7002);
        //write request
        OutputStream clientOutput = socketOnClientSide.getOutputStream();
        clientOutput.write("Test request".getBytes());
        //process request
        Socket socketOnServerSide = serverSocket.accept();
        socketOnServerSide.close();
        model.processRequest(socketOnServerSide);
        //compare results
        InputStream inputStream = socketOnClientSide.getInputStream();
        String requestString = IOUtil.toString(inputStream, String.valueOf(Charset.defaultCharset()));
        serverSocket.close();
                socketOnClientSide.close();
                socketOnServerSide.close();
                clientOutput.close();
                inputStream.close();
        Assert.assertEquals("Test request", "", requestString);

    }
    */
}

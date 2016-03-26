import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.AutoDeployment;
import nc.sumy.edu.webcontainer.dispatcher.Dispatcher;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpResponse;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import nc.sumy.edu.webcontainer.listener.ModelSocketProcessing;
import org.apache.maven.shared.utils.io.IOUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class
ModelSocketProcessingTest {
    Dispatcher dispatcher = new Dispatcher() {
        @Override
        public HttpResponse getResponse(Request request) {
            byte[] body = request.getRequestText().getBytes();
            HttpResponse hr = new HttpResponse(200);
            hr.setBody(body);
            return hr;
        }
    };

    ModelSocketProcessing model = new ModelSocketProcessing(dispatcher);

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

}

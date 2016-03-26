package nc.sumy.edu.webcontainer.listener;


import nc.sumy.edu.webcontainer.dispatcher.Dispatcher;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import org.apache.maven.shared.utils.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class ModelSocketProcessing {
    private Dispatcher dispatcher;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelSocketProcessing.class);

    public ModelSocketProcessing(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void processRequest(Socket clientSocket) {

        try( InputStream clientInput = clientSocket.getInputStream();
             OutputStream clientOutput = clientSocket.getOutputStream())
        {
            //String requestString = IOUtil.toString(clientInput, String.valueOf(Charset.defaultCharset()));
            byte[] bytes = new byte[clientInput.available()];
            clientInput.read(bytes);
            String requestString = new String(bytes);
            Request clientRequest = new HttpRequest(requestString,
                    clientSocket.getRemoteSocketAddress().toString(), clientSocket.getInetAddress().getHostName() );
            Response serverResponse = dispatcher.getResponse(clientRequest);
            clientOutput.write(serverResponse.getResponse());
        } catch (IOException e) {
            LOGGER.error("Request processing was unsuccessful. IOException appeared", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.warn("Socket was not closed properly.", e);
            }
        }
    }
}

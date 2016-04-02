package nc.sumy.edu.webcontainer.listener;


import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Class that processes client socket.
 * @author Lukianykhin O.V.
 */

public class ModelSocketProcessing {
    private final ServerConfiguration configuration;
    private final Deployment deployment;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelSocketProcessing.class);



    public ModelSocketProcessing(ServerConfiguration configuration, Deployment deployment) {
        this.configuration = configuration;
        this.deployment = deployment;
    }


    /**
     * Processes Socket from client.
     * Gets request from socket -> call Dispatcher to get appropriate response
     * -> send response to client through Socket
     * @param clientSocket Socket that should be processed
     */
    public void processRequest(Socket clientSocket) {

        try(BufferedInputStream clientInput = new BufferedInputStream(clientSocket.getInputStream());
             OutputStream clientOutput = clientSocket.getOutputStream())
        {
            // Read a set of characters from the socket
            ServerDispatcher serverDispatcher = new ServerDispatcher(this.configuration, this.deployment);
            //    2nd method of reading Socket to String
            BufferedReader clientBufferedreader = new BufferedReader(new InputStreamReader(clientInput));
            StringBuilder requestStringBuilder = new StringBuilder();
            String temp;
            while((temp = clientBufferedreader.readLine()) != null) {
                if(temp.equals("")) {
                    break;
                }
                requestStringBuilder.append(temp);
                requestStringBuilder.append("\r\n");
            }
            String requestString = new String(requestStringBuilder);


            /* 1st method of reading Socket to String (works but kostyl' stail)
            StringBuffer request = new StringBuffer(20480);
            int readedSize;
            byte[] buffer = new byte[2048];
            try {
                readedSize = clientInput.read(buffer);
            }
            catch (IOException e) {
                e.printStackTrace();
                readedSize = -1;
            }
            for (int j=0; j<readedSize; j++) {
                request.append((char) buffer[j]);
            }
                String requestString = request.toString();
            */
            /* 3rd method of reading Socket to String (02.04.16 - doesn't work)
            //BufferedInputStream bufferedInputStream = new BufferedInputStream(clientInput);
            byte[] bytes = new byte[clientInput.available()];
            String requestString;
            if(clientInput.read(bytes) != -1) {
                 requestString = new String(bytes, Charset.defaultCharset());
            } else {
                requestString = "";
            }
            */
            /*4th method of reading Socket to String (doesn't work)
            BufferedInputStream bufferedInputStream = new BufferedInputStream(clientInput);
            String requestString = IOUtils.toString(bufferedInputStream);
            */
            Request clientRequest = null;
            if(requestString != null && !("").equals(requestString)) {
                clientRequest = new HttpRequest(requestString,
                        clientSocket.getRemoteSocketAddress().toString(), clientSocket.getInetAddress().getHostName());
                Response serverResponse = serverDispatcher.getResponse(clientRequest);
                clientOutput.write(serverResponse.getResponse());
            }
            else {
                clientOutput.write("".getBytes());
            }
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

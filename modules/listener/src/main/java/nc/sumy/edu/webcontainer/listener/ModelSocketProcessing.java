package nc.sumy.edu.webcontainer.listener;


import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.dispatcher.Dispatcher;
import nc.sumy.edu.webcontainer.dispatcher.ServerDispatcher;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import org.apache.commons.io.IOUtils;
import org.apache.maven.shared.utils.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ModelSocketProcessing {
    private Dispatcher dispatcher;
    private ServerConfiguration configuration;
    private Deployment deployment;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelSocketProcessing.class);

    public ModelSocketProcessing(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ModelSocketProcessing(ServerConfiguration configuration, Deployment deployment) {
        this.configuration = configuration;
        this.deployment = deployment;
    }

    public void processRequest(Socket clientSocket) {

        try(BufferedInputStream clientInput = new BufferedInputStream(clientSocket.getInputStream());
             OutputStream clientOutput = clientSocket.getOutputStream())
        {
            // Read a set of characters from the socket
            System.out.println("request size " + clientInput.available());
            ServerDispatcher serverDispatcher = new ServerDispatcher(this.configuration, this.deployment);
            StringBuffer request = new StringBuffer(20480);
            int i;
            byte[] buffer = new byte[2048];
            try {
                i = clientInput.read(buffer);
            }
            catch (IOException e) {
                e.printStackTrace();
                i = -1;
            }
            for (int j=0; j<i; j++) {
                request.append((char) buffer[j]);
            }

            byte[] bytes = new byte[clientInput.available()];
            clientInput.read(bytes);
            clientOutput.flush();
//            String requestString = new String(bytes, Charset.forName("UTF-8"));
            String requestString = request.toString();
            System.out.println("requestString " + requestString);
            Request clientRequest = new HttpRequest(requestString,
                    clientSocket.getRemoteSocketAddress().toString(), clientSocket.getInetAddress().getHostName() );
            Response serverResponse = serverDispatcher.getResponse(clientRequest);
            System.out.println("------------------Start Response -------------------------");
            System.out.println(new String(serverResponse.getResponse()));
            System.out.println("------------------End Response -------------------------");
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

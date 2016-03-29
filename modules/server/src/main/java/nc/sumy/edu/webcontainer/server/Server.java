package nc.sumy.edu.webcontainer.server;

import nc.sumy.edu.webcontainer.configuration.JsonReadingException;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.AutoDeployment;
import nc.sumy.edu.webcontainer.listener.ServerSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Class that represents API for work with server. Allows to create, start and stop server.
 * @author Lukianykhin O.V.
 */

public class Server {
    ServerConfiguration config;
    AutoDeployment deploy;
    ServerSocketListener listener;
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    public Server() {
        LOG.info("Server starting");
        config = new ServerConfigurationJson();
        try{
            config.checkSystemVariable("SERVER_HOME");
        } catch (JsonReadingException e) {
            LOG.error("Server could not be started. System variable 'SERVER_HOME' is not defined", e);
            System.exit(-1);
        }
        if(!(config.getServerLocationAsFile().exists()) || !(new File(config.getServerLocation()).isDirectory())) {
            LOG.error("Server could not be started. System variable 'SERVER_HOME' defined, but location does not" +
                    "exist.");
            System.exit(-1);
        }
        LOG.info("Configuration loaded successfully");

        deploy = new AutoDeployment(config);

        try {
            listener = new ServerSocketListener(config, deploy);
            LOG.info("Listener created successfully");
        } catch (IOException e) {
            LOG.error("Server fall as Listner could not be created. IOException appeared.", e);
            System.exit(-1);
        }
    }

    public void startServer() {
        deploy.setDaemon(true);
        deploy.start();
        listener.start();

    }

    public void stopServer() {
        listener.stopListening();
        //deploy.stopDeployControll();
        LOG.info("Server stopped successfully.");
        System.exit(0);
    }
}


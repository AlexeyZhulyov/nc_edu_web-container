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

public class Server {
    ServerConfiguration config;
    AutoDeployment deploy;
    ServerSocketListener listener;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public Server() {
        LOGGER.info("Server starting");
        config = new ServerConfigurationJson();
        try{
            config.checkSystemVariable("SERVER_HOME");
        } catch (JsonReadingException e) {
            LOGGER.error("Server could not be started. System variable 'SERVER_HOME' is not defined", e);
            System.out.println("tut");
            System.exit(-1);
        }
        if(!(new File(config.getWwwLocation()).exists()) || !(new File(config.getWwwLocation()).isDirectory())) {
            LOGGER.error("Server could not be started. System variable 'SERVER_HOME' defined, but location does not" +
                    "exist.");
            System.out.println("tut2");
            System.exit(-1);
        }
        LOGGER.info("Configuration loaded successfully");

        deploy = new AutoDeployment(config);

        try {
            listener = new ServerSocketListener(config, deploy);
            LOGGER.info("Listener created successfully");
        } catch (IOException e) {
            LOGGER.error("Server fall as Listner could not be created. IOException appeared.", e);
            System.out.println("vot tut");
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
        LOGGER.info("Server stopped successfully.");
        System.exit(0);
    }
}


package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AutoDeployment
        extends Thread
        implements Deployment {
    ServerConfiguration configuration;
    Map<File, Map<String, Class>> domainsData = new HashMap<>();

    public AutoDeployment(ServerConfiguration configuration) {
        this.configuration = configuration;
        run();
    }

    public void run() {

    }

    @Override
    public Map<File, Map<String, Class>> getDomainsData() {
        return domainsData;
    }

}

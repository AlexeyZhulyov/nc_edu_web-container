package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import org.junit.Test;

import static java.io.File.separator;

public class AutoDeploymentTest {

    @Test
    public void deploymentTest() {
        ServerConfiguration configuration = new ServerConfigurationJson();
        configuration.setWwwLocation("src" + separator +
                "test" + separator + "resources" + separator + "folder_listener");
        AutoDeployment autoDeployment = new AutoDeployment(configuration);
        autoDeployment.run();
        /*for (int i = 0; i < 10000000; i++) {
            for (int j = 0; j < 10000000; j++) {
                for (int k = 0; k < 10000000; k++) {
                    for (int l = 0; l < 10000000; l++) {
                        i--;
                    }
                }
            }
        }*/
    }

}

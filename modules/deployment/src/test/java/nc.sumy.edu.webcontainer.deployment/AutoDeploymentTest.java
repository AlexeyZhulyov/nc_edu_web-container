package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static java.io.File.*;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;

public class AutoDeploymentTest {
    private static final String WWW_PATH = "src/test/resources/folder_listener";
    private static final String DEMO_PATH = WWW_PATH + separator + "www" + separator + "Servlets_demo-master";
    private AutoDeployment deployment;
    private final static String BLOG = "/Blog";

    @Before
    public void setInitData() {
        ServerConfiguration configuration= new ServerConfigurationJson();
        configuration.setServerLocation(WWW_PATH);
        deployment = new AutoDeployment(configuration);
        deployment.start();
    }

    @After
    public void finishDeploy() {
        deployment.interrupt();
        deleteQuietly(new File(DEMO_PATH));
    }

    @Test
    public void testDeploymentOnStart() {
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainsData = deployment.getDomainsData();
        ConcurrentHashMap<String, Class> dataMap = domainsData.get(new File(DEMO_PATH));
        assertEquals(dataMap.containsKey(BLOG), true);
    }
}
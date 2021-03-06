package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static java.io.File.*;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;

public class AutoDeploymentTest {
    private static final String WWW_PATH = "src/test/resources/folder_listener";
    private static final String DEMO_PATH = WWW_PATH + separator + "www";
    private static final String SAMPLE = "sample";
    private static final File SAMPLE_FILE = new File(DEMO_PATH + separator + SAMPLE);
    private final static String BLOG = "/Blog";
    private final static String DELETED_BLOG = "/Deleted";

    @Test
    public void testDeploymentOnStart() throws Exception {
        ServerConfiguration configuration= new ServerConfigurationJson();
        configuration.setServerLocation(WWW_PATH);
        AutoDeployment deployment = new AutoDeployment(configuration);
        deployment.start();
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainsData = deployment.getDomainsData();
        ConcurrentHashMap<String, Class> dataMap = domainsData.get(new File(DEMO_PATH + separator + "Servlets_demo-master"));
        assertEquals(dataMap.containsKey(BLOG), true);
        assertEquals(dataMap.containsKey(DELETED_BLOG), false);
        dataMap = domainsData.get(SAMPLE_FILE);
        assertEquals(dataMap.containsKey(BLOG), false);
        assertEquals(dataMap.containsKey(DELETED_BLOG), true);
        assertEquals(domainsData.get(new File(DEMO_PATH + separator + "notWebInf")).size(), 0);
        deployment.interrupt();
        deleteQuietly(new File(DEMO_PATH + separator + "Servlets_demo-master"));
        deleteQuietly(new File(DEMO_PATH + separator + SAMPLE));
    }

}
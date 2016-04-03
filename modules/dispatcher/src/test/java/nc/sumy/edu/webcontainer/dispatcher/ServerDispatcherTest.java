package nc.sumy.edu.webcontainer.dispatcher;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import nc.sumy.edu.webcontainer.configuration.ServerConfigurationJson;
import nc.sumy.edu.webcontainer.deployment.Deployment;
import nc.sumy.edu.webcontainer.deployment.WebXMLAnalyzer;
import nc.sumy.edu.webcontainer.http.HttpRequest;
import nc.sumy.edu.webcontainer.http.Request;
import nc.sumy.edu.webcontainer.http.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerDispatcherTest {

    private Dispatcher dispatcher;

    @Before
    public void setUp() {
        ServerConfiguration configuration = new ServerConfigurationJson();
        configuration.setServerLocation(getClass().getResource("/").getFile());
        ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainData = new ConcurrentHashMap<>();
        File domainFolder = new File(getClass().getResource("/www/Servlets_demo-master/").getFile());
        File webInf = new File(domainFolder.getPath() + "/WEB-INF");
        WebXMLAnalyzer xmlAnalyzer = new WebXMLAnalyzer(webInf);
        domainData.put(domainFolder, xmlAnalyzer.getDataMap());
        Deployment deployment = mock(Deployment.class);
        when(deployment.getDomainsData()).thenReturn(domainData);
        dispatcher = new ServerDispatcher(configuration, deployment);
    }

    @Test
    public void getResponseEmptyUrl() {
        String stringRequest = "GET /";
        Request request = new HttpRequest(stringRequest, "", "");
        Response response = dispatcher.getResponse(request);
        String stringBody = new String(response.getBody());
        File resourceFile = new File(getClass().getResource("/emptyUrl").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(expected.replace(" ",""), stringBody.replace(" ",""));
    }

    @Test
    public void getResponseIndexPage() {
        String stringRequest = "GET /my_site/index.html\n";
        Request request = new HttpRequest(stringRequest, "", "");
        Response response = dispatcher.getResponse(request);
        String stringBody = new String(response.getBody());
        File resourceFile = new File(getClass().getResource("/www/my_site/index.html").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(expected.replace(" ",""), stringBody.replace(" ",""));
    }

    @Test
    public void getResponseServlet() {
        String stringRequest = "GET /Servlets_demo-master/Blog\n";
        Request request = new HttpRequest(stringRequest, "", "");
        Response response = dispatcher.getResponse(request);
        String stringBody = new String(response.getBody());
        File resourceFile = new File(getClass().getResource("/servletResponse").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(expected.replace("\r",""), stringBody.replace(" ",""));
    }

}

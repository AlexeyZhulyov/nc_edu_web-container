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
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServerDispatcherTest {
    private Dispatcher dispatcher;
    private String bodyString;

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
        setInitData("GET /");
        File resourceFile = new File(getClass().getResource("/emptyUrl").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(expected.replace(" ",""), bodyString.replace(" ",""));
    }

    @Test
    public void getResponseIndexPage() {
        testStaticPage("GET /my_site/\n");
    }

    @Test
    public void getResponseStaticPage() {
        testStaticPage("GET /my_site/index.html\n");
    }

    @Test
    public void getResponseServlet() {
        setInitData("GET /Servlets_demo-master/Blog\n");
        File resourceFile = new File(getClass().getResource("/servletResponse").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        replace(expected, "\r", "\n");
        replace(bodyString, "\r", "\n");
        assertEquals(expected, replaceSpaces(bodyString));
    }

    @Test
    public void getResponseAbsentPage() {
        setInitData("GET /absent\n");
        File resourceFile = new File(getClass().getResource("/www/default/404.html").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(replaceSpaces(expected), replaceSpaces(bodyString));
    }

    @Test
    public void testOptionPage() {
        setInitData("OPTION /absent\n");
        assertEquals(bodyString, "200 OK");
    }

    @Test
    public void enumFunctionTest() {
        assertEquals(Header.valueOf("DATE"), Header.DATE);
        String headersString = Arrays.toString(Header.values());
        assertTrue(contains(headersString, "DATE"));
        assertEquals(PageType.valueOf("PNG"), PageType.PNG);
    }

    private void testStaticPage(String stringRequest) {
        setInitData(stringRequest);
        File resourceFile = new File(getClass().getResource("/www/my_site/index.html").getFile());
        String expected = ClassUtil.fileToString(resourceFile);
        assertEquals(replaceSpaces(expected), replaceSpaces(bodyString));
    }

    private void setInitData(String requestString) {
        Request request = new HttpRequest(requestString, "", "");
        Response response = dispatcher.getResponse(request);
        bodyString = new String(response.getBody());

    }

    private String replaceSpaces(String src) {
        return replace(src, " ","");
    }

}

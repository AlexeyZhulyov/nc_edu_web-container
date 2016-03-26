package nc.sumy.edu.webcontainer.web;

//import nc.sumy.edu.webcontainer.common.ClassUtil;
//import nc.sumy.edu.webcontainer.common.InstanceNotCreatedException;
import nc.sumy.edu.webcontainer.http.HttpRequest;
//import nc.sumy.edu.webcontainer.http.Response;
//import nc.sumy.edu.webcontainer.web.stub.*;
import org.junit.Before;
//import org.junit.Test;

//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//
//import static org.junit.Assert.assertEquals;
//import static java.lang.String.format;

public class ServletHandlerImplTest {
    private ServletHandler servletHandler;
    private HttpRequest request;

    @Before
    public void setUp() {
        servletHandler = new ServletHandlerImpl();
        String requestStr = "GET " + "/" + "TestServlet" + " HTTP/1.1" + "\r\n" +
                "Host" + ": foo.com" + "\r\n" +
                "Accept" + ": text/html" + "\r\n" +
                "Range-Unit: 3388 | 1024";
        request = new HttpRequest(requestStr, "", "");
    }

//    @Test
//    public void processMainDemoServlet() throws ClassNotFoundException, MalformedURLException {
//        String expected = ClassUtil.fileToString(new File(getClass().getResource("/TestIndex.html").getPath()));
//        ClassLoader classLoader = new URLClassLoader(new URL[]{new File("../www/Servlets_demo/WEB-INF/classes").toURI().toURL()});
//        Class klass = Class.forName("sumy.javacourse.webdemo.controller.Main", true, classLoader);
//        Response response = servletHandler.processServlet(request, klass);
//        byte[] body = response.getBody();
//        String actual = new String(body);
//        assertEquals(expected.replaceAll("\\s", ""), actual.replace("\r", "").replaceAll("\\s", ""));
//    }
//
//    @Test
//    public void processServletInstances() {
//        int number = 1;
//        String expected = "<h1>Hello Servlet</h1>\n<body>Test servlet #%s.</body>\n";
//        byte[] body = servletHandler.processServlet(request, TestServlet.class).getBody();
//        String actual = new String(body);
//        assertEquals(format(expected, number), actual.replace("\r", ""));
//        number++;
//        body = servletHandler.processServlet(request, TestServlet.class).getBody();
//        actual = new String(body);
//        assertEquals(format(expected, number), actual.replace("\r", ""));
//    }
//
//    @Test(expected = InstanceNotCreatedException.class)
//    public void processExceptionMessage1() {
//        servletHandler.processServlet(request, AbstractTestServlet.class);
//    }
//
//    @Test(expected = InstanceNotCreatedException.class)
//    public void processExceptionMessage2() {
//        servletHandler.processServlet(request, TestServletWithPrivateConstructor.class);
//    }
//
//    @Test(expected = WebException.class)
//    public void processExceptionMessage3() {
//        servletHandler.processServlet(request, TestServletInitException.class);
//    }
//
//    @Test(expected = WebException.class)
//    public void processExceptionMessage4() {
//        servletHandler.processServlet(request, TestServletServiceException.class);
//    }
//
//    @Test(expected = WebException.class)
//    public void processExceptionMessage5() {
//        servletHandler.processServlet(request, TestServletIOException.class);
//    }
}
package nc.sumy.edu.webcontainer.deployment;

import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebXMLAnalyzerTest {
    private WebXMLAnalyzer analyzer;
    private Map<String, Class> dataMap;
    private final static String WEB_INF = "/WEB-INF";
    private final static String TEST_PATH = "src/test/java/resources/xml_test_files/test";

    @Test
    public void isValidFileContainsKey() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(1) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(dataMap.containsKey("/Servlet"), true);
        assertEquals(dataMap.containsKey("/Servlet2"), true);
    }

    @Test
    public void errorHandlerTest() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(2) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), false);
        assertEquals(dataMap.containsKey("/Servlet"), false);
        assertEquals(dataMap.containsKey("/Servlet2"), false);
    }

    @Test
    public void errorInputInfoTest1() {
        analyzer = new WebXMLAnalyzer(new File("src/test/" + Integer.toString(1) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), false);
    }

    @Test
    public void errorInputInfoTest2() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(3) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), true);
        assertEquals(dataMap.containsKey("/Servlet"), true);
        assertEquals(dataMap.containsKey("/Servlet2"), true);
    }

}

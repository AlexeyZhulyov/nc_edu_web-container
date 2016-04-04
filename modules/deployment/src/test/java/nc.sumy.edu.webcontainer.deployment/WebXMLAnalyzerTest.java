package nc.sumy.edu.webcontainer.deployment;

import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class WebXMLAnalyzerTest {
    private WebXMLAnalyzer analyzer;
    private Map<String, Class> dataMap;
    private final static String WEB_INF = "/WEB-INF";
    private final static String TEST_PATH = "src/test/resources/xml_test_files/test";
    private final static String SERVLET1 = "/Servlet";
    private final static String SERVLET2 = "/Servlet2";

    @Test
    public void isValidFileContainsKey() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(1) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(dataMap.containsKey(SERVLET1), true);
        assertEquals(dataMap.containsKey(SERVLET2), true);
    }

    @Test
    public void errorHandlerTest() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(2) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), false);
        assertEquals(dataMap.containsKey(SERVLET1), false);
        assertEquals(dataMap.containsKey(SERVLET2), false);
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
        assertEquals(dataMap.containsKey(SERVLET1), true);
        assertEquals(dataMap.containsKey(SERVLET2), true);
    }

    @Test
    public void errorInputInfoTest3() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(10) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), false);
        assertEquals(dataMap.containsKey(SERVLET1), false);
        assertEquals(dataMap.containsKey(SERVLET2), false);
    }

    @Test
    public void errorInputInfoTest4() {
        analyzer = new WebXMLAnalyzer(new File(TEST_PATH + Integer.toString(4) + WEB_INF));
        dataMap = analyzer.getDataMap();
        assertEquals(analyzer.isXmlValid(), true);
        assertEquals(dataMap.containsKey(SERVLET1), false);
        assertEquals(dataMap.containsKey(SERVLET2), false);
    }

}

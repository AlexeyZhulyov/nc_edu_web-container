package nc.sumy.edu.webcontainer.deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;
import static java.util.Objects.nonNull;

public class WebXMLAnalyzer {
    private boolean isValid = true;
    private final File webInf;
    private final File webXml;
    private Document document;
    private final File classPath;
    private final Map<String, String> servletTagMap = new HashMap<>();
    private final Map<String, String> servletMappingTagMap = new HashMap<>();
    private final Map<String, Class> dataMap = new HashMap<>();
    /*Constants*/
    private static final Logger LOG = LoggerFactory.getLogger(WebXMLAnalyzer.class);
    private static final String WEB_XML = "web.xml";
    private static final String CLASS = "classes";
    private static final String SERVLET_TAG = "servlet";
    private static final String SERVLET_MAPPING_TAG = "servlet-mapping";
    private static final String SERVLET_NAME_TAG = "servlet-name";
    private static final String SERVLET_CLASS_TAG = "servlet-class";
    private static final String URL_PATTERN_TAG = "url-pattern";

    public WebXMLAnalyzer(File webInf) {
        this.webInf = webInf;
        webXml = new File(webInf.toString() + separator + WEB_XML);
        classPath = new File(webInf.toString() + separator + CLASS + separator);
        validateXMLbyDTD();
        if (isValid) {
            makeDataMap();
        }
    }

    private void validateXMLbyDTD() {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(true);
        DocumentBuilder builder;
        try {
            builder = domFactory.newDocumentBuilder();
            ParsingErrorHandler errorHandler = new ParsingErrorHandler(LOG, webInf);
            builder.setErrorHandler(errorHandler);
            isValid = errorHandler.isXmlValid();
            try {
                document = builder.parse(webXml);
                document.getDocumentElement().normalize();
            } catch (SAXException | IOException e) {
                LOG.warn(webXml.getAbsolutePath() + " cannot be parsed.", e);
                isValid = false;
            }
        } catch (ParserConfigurationException e) {
            LOG.warn(webXml.getAbsolutePath() + " have wrong configuration.", e);
            isValid = false;
        }
    }

    private void makeDataMap() {
        getServletTagMap(SERVLET_TAG, SERVLET_CLASS_TAG, servletTagMap);
        getServletTagMap(SERVLET_MAPPING_TAG, URL_PATTERN_TAG, servletMappingTagMap);
        Class servletClass;
        for (Map.Entry<String, String> item : servletTagMap.entrySet()) {
            servletClass = createClass(item.getValue());
            if (nonNull(servletClass)) {
                dataMap.put(servletMappingTagMap.get(item.getKey()), servletClass);
            }
        }
    }

    private void getServletTagMap(String innerTag, String childTag, Map<String, String> container) {
        NodeList nodeList = document.getElementsByTagName(innerTag);
        Element servletTag;
        String servletName;
        String infoTag;
        for (int i = 0; i < nodeList.getLength(); i++) {
            servletTag = (Element) nodeList.item(i).getChildNodes();
            servletName = servletTag.getElementsByTagName(SERVLET_NAME_TAG).item(0).getTextContent();
            infoTag = servletTag.getElementsByTagName(childTag).item(0).getTextContent();
            container.put(servletName, infoTag);
        }
    }

    private Class createClass(String fullClassName) {
        Class servletClass = null;
        URI uri = classPath.toURI();
        URL url;
        try {
            url = uri.toURL();
            ClassLoader classLoader = new URLClassLoader(new URL[]{url});
            servletClass = classLoader.loadClass(fullClassName);
        } catch (MalformedURLException e) {
            LOG.warn("Malformed URL had occurred: ", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("Class cannot be found: ", e);
        }
        return servletClass;
    }

    public boolean isXmlValid() {
        return isValid;
    }

    public Map<String, Class> getDataMap() {
        return dataMap;
    }

}
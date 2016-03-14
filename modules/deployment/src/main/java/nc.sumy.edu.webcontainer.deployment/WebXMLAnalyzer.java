package nc.sumy.edu.webcontainer.deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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

class WebXMLAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebXMLAnalyzer.class);
    private static final String URL_PATTERN = "url-pattern";
    private static final String SERVLET_PATH = "servlet-class";
    private static final String WEB_XML = "web.xml";
    private static final String CLASS_FOLDER = "classes";
    private final File webInf;
    private final File webXml;
    private boolean isValid;
    private Document document;
    private Map<String, String>  sa = new HashMap<>();
    private Map<String, String>  sa2 = new HashMap<>();
    private Map<String, Class> warData = new HashMap<>();

    public WebXMLAnalyzer(String webInf) {
        this.webInf = new File(webInf);
        webXml = new File(webInf + separator + WEB_XML);
        validateXMLbyDTD();
        setData();
    }

    private void validateXMLbyDTD() {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setValidating(true);
        DocumentBuilder builder;
        try {
            builder = domFactory.newDocumentBuilder();
            builder.setErrorHandler(new ParsingErrorHandler());
            try {
                document = builder.parse(webXml);
                document.getDocumentElement().normalize();
            } catch (SAXException | IOException e) {
                LOGGER.warn(webXml.getAbsolutePath() + " cannot be parsed.", e);
                isValid = false;
            }
        } catch (ParserConfigurationException e) {
            LOGGER.warn(webXml.getAbsolutePath() + " have wrong configuration.", e);
            isValid = false;
        }
        isValid = true;
    }

    private void setData() {
        NodeList nodeList = document.getElementsByTagName(URL_PATTERN);
    }

    private Class createClass(File servletFile) {
        NodeList nodeList = document.getElementsByTagName(URL_PATTERN);
        String urlPattern = nodeList.item(0).getTextContent();
        nodeList = document.getElementsByTagName(SERVLET_PATH);
        String servletFolder = nodeList.item(0).getTextContent();
        servletFile = new File(webInf + separator + CLASS_FOLDER + separator);
        Class servletClass;
        URI uri = null;
        try {
            uri = servletFile.toURI();
            URL url = uri.toURL();
            ClassLoader classLoader = new URLClassLoader(new URL[]{url});
            servletClass = classLoader.loadClass(servletFolder);
        } catch (MalformedURLException e) {
            LOGGER.warn("Malformed URL had occurred: ", e);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Class cannot be found: ", e);
        }
        return null;
    }

    public boolean isValid() {
        return isValid;
    }

    private class ParsingErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) {
            LOGGER.warn(webInf.getAbsolutePath() + " is not valid.", exception);
            isValid = false;
        }

        @Override
        public void error(SAXParseException exception) {
            LOGGER.warn(webInf.getAbsolutePath() + " is not valid.", exception);
            isValid = false;
        }

        @Override
        public void fatalError(SAXParseException exception) {
            LOGGER.warn(webInf.getAbsolutePath() + " is not valid.", exception);
            isValid = false;
        }
    }

}

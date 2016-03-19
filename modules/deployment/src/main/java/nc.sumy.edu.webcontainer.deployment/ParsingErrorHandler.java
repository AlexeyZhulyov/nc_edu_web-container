package nc.sumy.edu.webcontainer.deployment;


import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.File;

public class ParsingErrorHandler implements ErrorHandler {
    private final Logger log;
    private boolean isValid = true;
    private final File webInf;

    public ParsingErrorHandler(Logger log, File webInf) {
        this.log = log;
        this.webInf = webInf;
    }

    @Override
    public void warning(SAXParseException exception) {
        log.warn(webInf.getAbsolutePath() + " is not valid.", exception);
    }

    @Override
    public void error(SAXParseException exception) {
        log.warn(webInf.getAbsolutePath() + " is not valid.", exception);
    }

    @Override
    public void fatalError(SAXParseException exception) {
        log.warn(webInf.getAbsolutePath() + " is not valid.", exception);
        isValid = false;
    }

    public boolean isXmlValid() {
        return isValid;
    }
}

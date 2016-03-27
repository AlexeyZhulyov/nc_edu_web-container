package nc.sumy.edu.webcontainer.web;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class StaticContentHandlerImplTest {
    private final StaticContentHandler staticContentHandler = new StaticContentHandlerImpl();

    @Test
    public void processStaticContent() throws URISyntaxException {
        String processResult = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<title>Page Title</title>\n" +
                "<body>\n" +
                "\n" +
                "<h1>This is a heading</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        File testPage = new File(StaticContentHandlerImplTest.class.getResource("/TestHtml.html").toURI());
        assertEquals(processResult, new String(staticContentHandler.process(testPage)).replace("\r", ""));
    }

    @Test(expected = StaticContentFileException.class)
    public void processException() {
        staticContentHandler.process(new File("AbsentHtml.html"));
    }
}
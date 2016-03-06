package nc.sumy.edu.webcontainer.web;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WebHandlerImplTest {
    private WebHandler webHandler = null;
    private String processResult = null;
    private File testPage = null;

    @Before
    public void setUp() {
        webHandler = new WebHandlerImpl();
        processResult = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<title>Page Title</title>\n" +
                "<body>\n" +
                "\n" +
                "<h1>This is a heading</h1>\n" +
                "<p>This is a paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        testPage = new File("src\\test\\resources\\TestHtml.html");
    }

    @Test
    public void process() {
        assertEquals(processResult, webHandler.process(testPage));
    }
}
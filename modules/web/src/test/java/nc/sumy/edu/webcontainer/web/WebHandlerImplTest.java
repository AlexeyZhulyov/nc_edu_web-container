package nc.sumy.edu.webcontainer.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static nc.sumy.edu.webcontainer.web.WebException.*;
import static java.lang.String.format;
import static org.junit.Assert.*;

public class WebHandlerImplTest {
    private WebHandler webHandler = null;
    private String processResult = null;
    private File testPage = null;
    private File absentPage = null;

    private final static String EXPECT_EXCEPTION = "Expected an WebException to be thrown";

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
        absentPage = new File("src\\test\\resources\\AbsentHtml.html");
    }

    @Test
    public void process() {
        assertEquals(processResult, webHandler.process(testPage));
    }

    @Test(expected = WebException.class)
    public void processException() {
        webHandler.process(absentPage);
    }

    @Test
    public void runExceptionMessage() {
        try {
            webHandler.process(absentPage);
            fail(EXPECT_EXCEPTION);
        } catch (WebException e) {
            assertEquals(format(CANNOT_FIND_READ_FILE, "AbsentHtml.html"), e.getMessage());
        }
    }
}
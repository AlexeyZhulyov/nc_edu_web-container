package nc.sumy.edu.webcontainer.web;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;

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

        try {
            testPage = new File(WebHandlerImplTest.class.getResource("/TestHtml.html").toURI());
        } catch (URISyntaxException e) {
            throw new WebException(format("Cannot parse URI: %s", testPage.getName()), e);
        }
        absentPage = new File("AbsentHtml.html");
    }

    @Test
    public void process() {
        assertEquals(processResult, webHandler.process(testPage).replace("\r",""));
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
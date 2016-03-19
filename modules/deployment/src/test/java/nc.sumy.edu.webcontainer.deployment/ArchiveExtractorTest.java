package nc.sumy.edu.webcontainer.deployment;

import org.junit.After;
import org.junit.Test;
import java.io.File;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;
import static java.io.File.*;

public class ArchiveExtractorTest {
    private static final String DOMAIN_PATH = "src/test/resources/www_extractor_test/domains";
    private final File createdFolder1 = new File(DOMAIN_PATH + separator + "sample");
    private final File createdFolder2 = new File(DOMAIN_PATH + separator + "sample2");
    private final File createdFolder3 = new File(DOMAIN_PATH + separator + "sample3");
    private final File createdFolderIllegal = new File(DOMAIN_PATH + separator + "sampleNotExist");
    private final ArchiveExtractor extractor = new ArchiveExtractor(
            new File("src/test/resources/www_extractor_test/war"),
            new File(DOMAIN_PATH));

    @Test
    public void testExtractor1() {
        assertEquals(createdFolder1.exists(), false);
        extractor.extractWarFile("sample.war");
        assertEquals(createdFolder1.exists(), true);
    }

    @Test
    public void testExtractor2() {
        assertEquals(createdFolder2.exists(), false);
        extractor.extractWarFile("sample2.war");
        assertEquals(createdFolder2.exists(), false);
    }

    @Test
    public void testExtractor3() {
        assertEquals(createdFolder3.exists(), true);
        extractor.extractWarFile("sample3.war");
        assertEquals(createdFolder3.exists(), true);
    }

    @Test
    public void testExtractorIllegal() {
        assertEquals(createdFolderIllegal.exists(), false);
        extractor.extractWarFile("sampleNotExist.war");
        assertEquals(createdFolderIllegal.exists(), false);
    }

    @After
    public void deleteDomainFolder() {
        deleteQuietly(createdFolder1);
        deleteQuietly(createdFolder2);
    }

}

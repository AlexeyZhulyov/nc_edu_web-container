package nc.sumy.edu.webcontainer.deployment;

import org.junit.After;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;
import static java.io.File.*;

public class ArchiveExtractorTest {
    private static final String DOMAIN_PATH = "src/test/resources/www_extractor_test/domains";
    private final File createdFolder1 = new File(DOMAIN_PATH + separator + "sample");
    private final File createdFolder2 = new File(DOMAIN_PATH + separator + "sample2");
    private final File createdFolder3 = new File(DOMAIN_PATH + separator + "sample3");
    private final File createdFolder4 = new File(DOMAIN_PATH + separator + "sample4");
    private final File createdFolder5 = new File(DOMAIN_PATH + separator + "sample5");
    private final File createdFolderIllegal = new File(DOMAIN_PATH + separator + "sampleNotExist");
    private static final File WAR_DIRECTORY = new File("src/test/resources/www_extractor_test/war");
    private final ArchiveExtractor extractor = new ArchiveExtractor(
            WAR_DIRECTORY,
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

    @SuppressWarnings("PMD")
    @Test
    public void createFileMethodTest() {
        assertEquals(createdFolder4.exists(), false);
        try {
            JarFile jar = new JarFile(new File("src/test/resources/www_extractor_test/war/sample4.war"));
            JarEntry jarEntry = jar.entries().nextElement();
            extractor.createFile(jar, jarEntry, createdFolder4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(createdFolder4.exists(), true);
        deleteQuietly(createdFolder4);
        assertEquals(createdFolder5.exists(), false);
        try {
            JarFile jar = new JarFile(new File("src/test/resources/www_extractor_test/war/sample5.war"));
            deleteQuietly(new File("src/test/resources/www_extractor_test/war/sample5.war"));
            JarEntry jarEntry = jar.entries().nextElement();
            extractor.createFile(jar, jarEntry, createdFolder5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(createdFolder5.exists(), true);

    }

    @After
    public void deleteDomainFolder() {
        deleteQuietly(createdFolder1);
        deleteQuietly(createdFolder2);
        deleteQuietly(createdFolder4);
        deleteQuietly(createdFolder5);
    }

}

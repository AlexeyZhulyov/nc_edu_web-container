package nc.sumy.edu.webcontainer.deployment;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;

public class ArchiveExtractorTest {
    //private static final String DOMAIN_PATH = "src/test/resources/www_extractor_test/";
    private static final String WWW_PATH = "src/test/resources/www_extractor_test/www/";
    private final File createdFolder1 = new File(WWW_PATH + "sample");
    private final File createdFolder2 = new File(WWW_PATH + "sample2");
    private final File createdFolder3 = new File(WWW_PATH + "sample3");
    private final File createdFolder4 = new File(WWW_PATH + "sample4");
    private final File createdFolder5 = new File(WWW_PATH + "sample5");
    private final File createdFolderIllegal = new File(WWW_PATH + "sampleNotExist");
    private final ArchiveExtractor extractor = new ArchiveExtractor(new File(WWW_PATH));

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
            JarFile jar = new JarFile(new File("src/test/resources/www_extractor_test/www/sample4.war"));
            JarEntry jarEntry = jar.entries().nextElement();
            extractor.createFile(jar, jarEntry, createdFolder4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(createdFolder4.exists(), true);
        deleteQuietly(createdFolder4);
        assertEquals(createdFolder5.exists(), false);
        try {
            JarFile jar = new JarFile(new File("src/test/resources/www_extractor_test/www/sample5.war"));
            deleteQuietly(new File("src/test/resources/www_extractor_test/www/sample5.war"));
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

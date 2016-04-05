package nc.sumy.edu.webcontainer.deployment;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.io.File.separator;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ArchiveExtractorTest {
    private static final String WWW_PATH = "src/test/resources/www_extractor_test/www/";
    private final File createdFolder1 = new File(WWW_PATH + "sample");
    private final File createdFolder2 = new File(WWW_PATH + "sample2");
    private final File createdFolder3 = new File(WWW_PATH + "sample3");
    private final File createdFolder4 = new File(WWW_PATH + "sample4");
    private final File createdFolder5 = new File(WWW_PATH + "sample5");
    private final File createdFolder6 = new File(WWW_PATH + "sample6");
    private final File createdFolderIllegal = new File(WWW_PATH + "sampleNotExist");
    private final ArchiveExtractor extractor = new ArchiveExtractor(new File(WWW_PATH));

    @Test
    public void testExtractor1() {
        assertEquals(createdFolder1.exists(), false);
        extractor.extractWarFile("sample.war");
        assertEquals(createdFolder1.exists(), true);
    }

    @Test(expected = IOException.class)
    public void testExceptionOnGetInputStream() throws IOException {
        JarFile jar = mock(JarFile.class);
        JarEntry jarEntry = mock(JarEntry.class);
        File file = mock(File.class);
        when(jar.getInputStream(jarEntry)).thenThrow(IOException.class);
        extractor.createFile(jar, jarEntry, file);
    }

    @Test(expected = IOException.class)
    public void createFileException2() throws IOException {
        JarFile jar = new JarFile(new File(WWW_PATH + "sample6.war"));
        extractor.createFile(jar, new JarEntry(""), createdFolder4);
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
    @Test(expected = IOException.class)
    public void errorOutputStream() throws IOException {
        assertEquals(createdFolder6.exists(), false);
        Enumeration entries;
        JarFile jar = null;
        try {
            jar = new JarFile(WWW_PATH + "sample6.war");
        } catch (IOException e) {
        }
        entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            File file = new File(WWW_PATH + "sample6" + separator + jarEntry.getName());
            if (jarEntry.isDirectory()) {
                file.mkdir();
            } else {
                extractor.createFile(jar, jarEntry, file);
            }
        }
    }

    @After
    public void deleteDomainFolder() {
        deleteQuietly(createdFolder1);
        deleteQuietly(createdFolder2);
        deleteQuietly(createdFolder4);
        deleteQuietly(createdFolder5);
        deleteQuietly(createdFolder6);
    }

}

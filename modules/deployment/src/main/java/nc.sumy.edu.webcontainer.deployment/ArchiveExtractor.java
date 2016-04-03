package nc.sumy.edu.webcontainer.deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.io.File.*;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.IOUtils.copy;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * Class that provides auto-extracting .war files.
 * @author Vinogradov M.O.
 */
public class ArchiveExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(ArchiveExtractor.class);
    private final File wwwDirectory;

    public ArchiveExtractor(File wwwDirectory) {
        this.wwwDirectory = wwwDirectory;
    }

    @SuppressWarnings("PMD")
    public void extractWarFile(String jarFile) {
        refreshDirectory(jarFile);
        try (JarFile jar = new JarFile(wwwDirectory + separator + jarFile)) {
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                extractFile(jarFile, jar, entries);
            }
        } catch (IOException e) {
            deleteQuietly(new File(wwwDirectory.getPath() + separator + split(jarFile, ".")[0]));
            LOG.warn("Cannot read/write/found file: ", e);
        }
    }

    protected void extractFile(String jarFile, JarFile jar, Enumeration entries) throws IOException {
        JarEntry jarEntry = (JarEntry) entries.nextElement();
        File file = new File(wwwDirectory.getPath() + separator + split(jarFile, ".")[0] + separator + jarEntry.getName());
        if (jarEntry.isDirectory()) {
            file.mkdir();
            return;
        }
        createFile(jar, jarEntry, file);
    }

    protected void createFile(JarFile jar, JarEntry jarEntry, File file) throws IOException {
        try (InputStream in = new BufferedInputStream(jar.getInputStream(jarEntry));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            copy(in, out);
            out.flush();
        }
    }

    private void refreshDirectory(String jarFile) {
        File domainDirectory = new File(wwwDirectory.getPath() + separator + split(jarFile, ".")[0]);
        if (domainDirectory.exists()) {
            deleteQuietly(domainDirectory);
        }
        domainDirectory.mkdir();
    }

}
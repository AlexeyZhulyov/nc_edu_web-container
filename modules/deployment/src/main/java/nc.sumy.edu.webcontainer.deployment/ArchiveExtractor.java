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

public class ArchiveExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(ArchiveExtractor.class);
    private final File warDirectory;
    private File deployDirectory;

    public ArchiveExtractor(File warDirectory, File domainsDirectory) {
        this.warDirectory = warDirectory;
        this.deployDirectory = domainsDirectory;
    }

    @SuppressWarnings("PMD")
    public void extractWarFile(String jarFile) {
        refreshDirectory(jarFile);
        try {
            JarFile jar = new JarFile(warDirectory + separator + jarFile);
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                extractFile(jar, entries);
            }
        } catch (IOException e) {
            deleteQuietly(deployDirectory);
            LOG.warn("Cannot read/write/found file: ", e);
        }
    }

    private void extractFile(JarFile jar, Enumeration entries) throws IOException {
        JarEntry jarEntry = (JarEntry) entries.nextElement();
        File file = new File(deployDirectory.getPath() + separator + jarEntry.getName());
        if (jarEntry.isDirectory()) {
            file.mkdir();
            return;
        }
        createFile(jar, jarEntry, file);
    }

    void createFile(JarFile jar, JarEntry jarEntry, File file) throws IOException {
        try (InputStream in = new BufferedInputStream(jar.getInputStream(jarEntry));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(file))
        ) {
            copy(in, out);
        }
    }

    private void refreshDirectory(String jarFile) {
        deployDirectory = new File(deployDirectory.getPath() + separator + split(jarFile, ".")[0]);
        if (deployDirectory.exists()) {
            deleteQuietly(deployDirectory);
        }
        deployDirectory.mkdir();
    }

}
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

public class WarExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(WarExtractor.class);
    private final File warDirectory;
    private File deployDirectory;

    public WarExtractor(File warDirectory, File domainsDirectory) {
        this.warDirectory = warDirectory;
        this.deployDirectory = domainsDirectory;
    }

    @SuppressWarnings("PMD")
    public void extractWarFile(String jarFile) {
        deployDirectory = new File(deployDirectory.getPath() + separator + split(jarFile, ".")[0]);
        if (deployDirectory.exists()) {
            deleteQuietly(deployDirectory);
        }
        deployDirectory.mkdir();
        try {
            JarFile jar = new JarFile(warDirectory + separator + jarFile);
            Enumeration entries = jar.entries();
            JarEntry jarEntry;
            File file;
            while (entries.hasMoreElements()) {
                jarEntry = (JarEntry) entries.nextElement();
                file = new File(deployDirectory.getPath() + separator + jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    file.mkdir();
                    continue;
                }
                try (InputStream in = new BufferedInputStream(jar.getInputStream(jarEntry));
                     OutputStream out = new BufferedOutputStream(new FileOutputStream(file))
                ) {
                    copy(in, out);
                }
            }
        } catch (IOException e) {
            deleteQuietly(deployDirectory);
            LOG.warn("Cannot read/write/found file: ", e);
        }
    }

}
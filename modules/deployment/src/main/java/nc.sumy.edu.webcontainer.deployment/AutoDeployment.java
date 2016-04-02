package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ConcurrentHashMap;

import static java.io.File.separator;
import static java.nio.file.StandardWatchEventKinds.*;
import static org.apache.commons.lang3.StringUtils.endsWithIgnoreCase;
import static org.apache.commons.lang3.StringUtils.replace;

/**
 * Class that provides listening of changes in www-folder and
 * gathers together functionality of another classes in this package.
 * @author Vinogradov M.O.
 */
public class AutoDeployment extends Thread implements Deployment {
    private static final Logger LOG = LoggerFactory.getLogger(AutoDeployment.class);
    private final File wwwFolder;
    ServerConfiguration configuration;
    ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainsData = new ConcurrentHashMap<>();

    public AutoDeployment(ServerConfiguration configuration) {
        this.configuration = configuration;
        wwwFolder = new File(configuration.getServerLocation() + separator + "www");
        initialDeployment();
    }

    public void run() {
        Path path = wwwFolder.toPath();
        try {
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    for (WatchEvent event : key.pollEvents()) {
                        eventChooser(event);
                        key.reset();
                    }
                } catch (InterruptedException e) {
                    LOG.warn("Cannot take watch service in my folder: ", e);
                }
            }
        } catch (IOException e) {
            LOG.warn("Cannot get default file system or register path: ", e);
        }
    }

    private void initialDeployment() {
        File[] files = wwwFolder.listFiles();
        for (File file: files) {
            if (!createDomain(file.getName())) {
                File webInf = new File(getFileInDomainFolder(file.getName()).getPath() + separator + "WEB-INF");
                if (webInf.exists()) {
                    WebXMLAnalyzer xmlAnalyzer = new WebXMLAnalyzer(webInf);
                    domainsData.put(getFileInDomainFolder(file.getName()), xmlAnalyzer.getDataMap());
                }
            }
        }
    }

    private void eventChooser(WatchEvent event) {
        switch (event.kind().name()) {
            case "OVERFLOW":
                LOG.info("Overflow event in deployment-folder.");
                break;
            case "ENTRY_CREATE": createDomain(event.context().toString());
                break;
            case "ENTRY_MODIFY": {
                createDomain(event.context().toString());
            }
            break;
            case "ENTRY_DELETE": {
                LOG.info("File deleted: " +
                        getFileInDomainFolder(event.context().toString()).getName());
                domainsData.remove(getFileInDomainFolder(event.context().toString()));
            }
                break;
            default: LOG.info("Unknown event in deployment-folder.");
                break;
        }
    }

    private boolean createDomain(String fileTitle) {
        if (endsWithIgnoreCase(fileTitle, ".war")) {
            ArchiveExtractor extractor = new ArchiveExtractor(wwwFolder);
            extractor.extractWarFile(fileTitle);
            File webInf = new File(getFileInDomainFolder(fileTitle).getPath() + separator + "WEB-INF");
            WebXMLAnalyzer xmlAnalyzer = new WebXMLAnalyzer(webInf);
            domainsData.put(getFileInDomainFolder(fileTitle), xmlAnalyzer.getDataMap());
            return true;
        } else {
            return false;
        }
    }

    private File getFileInDomainFolder(String fileTitle) {
        return new File(wwwFolder + separator + replace(fileTitle, ".war", ""));
    }

    @Override
    public ConcurrentHashMap <File, ConcurrentHashMap <String, Class>> getDomainsData() {
        return domainsData;
    }
}

package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.io.File.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.lang3.StringUtils.*;

public class AutoDeployment extends Thread implements Deployment {
    private static final Logger LOG = LoggerFactory.getLogger(AutoDeployment.class);
    private final File domainFolder;
    private final File warFolder;
    ServerConfiguration configuration;
    ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> domainsData = new ConcurrentHashMap<>();

    public AutoDeployment(ServerConfiguration configuration) {
        this.configuration = configuration;
        domainFolder = new File(configuration.getWwwLocation() + separator + "www" + separator + "domain");
        warFolder = new File(configuration.getWwwLocation() + separator + "www" + separator + "war");
    }

    public void run() {
        Path path = warFolder.toPath();
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

    private void eventChooser(WatchEvent event) {
        switch (event.kind().name()) {
            case "OVERFLOW":
                LOG.info("Overflow event in deployment-folder.");
                break;
            case "ENTRY_CREATE": createDomain(event.context().toString());
                break;
            case "ENTRY_MODIFY": {
                deleteDomain(event.context().toString());
                createDomain(event.context().toString());
            }
            break;
            case "ENTRY_DELETE": deleteDomain(event.context().toString());
                break;
            default: LOG.info("Unknown event in deployment-folder.");
                break;
        }
    }

    private void deleteDomain(String fileTitle) {
        if (endsWithIgnoreCase(fileTitle, "war")) {
            deleteQuietly(getFileInDomainFolder(fileTitle));
            domainsData.remove(getFileInDomainFolder(fileTitle));
        }
    }

    private void createDomain(String fileTitle) {
        if (endsWithIgnoreCase(fileTitle, "war")) {
            ArchiveExtractor extractor = new ArchiveExtractor(warFolder, domainFolder);
            extractor.extractWarFile(fileTitle);
            File webInf = new File(getFileInDomainFolder(fileTitle).getPath() + separator + "WEB-INF");
            WebXMLAnalyzer xmlAnalyzer = new WebXMLAnalyzer(webInf);
            domainsData.put(getFileInDomainFolder(fileTitle), xmlAnalyzer.getDataMap());
        }
    }

    private File getFileInDomainFolder(String fileTitle) {
        return new File(domainFolder + separator + replace(fileTitle, ".war", ""));
    }

    @Override
    public ConcurrentHashMap <File, ConcurrentHashMap <String, Class>> getDomainsData() {
        return domainsData;
    }
}

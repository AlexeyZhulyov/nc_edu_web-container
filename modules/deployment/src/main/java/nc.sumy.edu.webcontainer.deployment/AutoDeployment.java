package nc.sumy.edu.webcontainer.deployment;

import nc.sumy.edu.webcontainer.configuration.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.io.File.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class AutoDeployment extends Thread implements Deployment {
    private static final Logger LOG = LoggerFactory.getLogger(AutoDeployment.class);
    private final File deployFolder;
    private final File warFolder;
    ServerConfiguration configuration;
    Map<File, Map<String, Class>> domainsData = new HashMap<>();

    public AutoDeployment(ServerConfiguration configuration) {
        this.configuration = configuration;
        deployFolder = new File(configuration.getWwwLocation() + separator + "www" + separator + "deploy");
        warFolder = new File(configuration.getWwwLocation() + separator + "www" + separator + "war");
    }

    public void run() {
        Path path = deployFolder.toPath();
        try {
            WatchService watchService = path.getFileSystem().newWatchService();
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    ArchiveExtractor extractor = new ArchiveExtractor(deployFolder, warFolder);
                    WebXMLAnalyzer xmlAnalyzer;
                    for (WatchEvent event : key.pollEvents()) {
                        switch (event.kind().name()) {
                            case "OVERFLOW":
                                LOG.info("Overflow event in deployment-folder.");
                                break;
                            case "ENTRY_CREATE": {
                                extractor.extractWarFile((String) event.context());
                                File webInf = new File(warFolder + separator + event.context() + separator + "WEB-INF");
                                xmlAnalyzer = new WebXMLAnalyzer(webInf);
                                domainsData.put(new File(deployFolder + separator + event.context()), xmlAnalyzer.getDataMap());
                            }
                                break;
                            case "ENTRY_MODIFY": {
                                extractor.extractWarFile((String) event.context());
                                File webInf = new File(warFolder + separator + event.context() + separator + "WEB-INF");
                                xmlAnalyzer = new WebXMLAnalyzer(webInf);
                                domainsData.put(new File(deployFolder + separator + event.context()), xmlAnalyzer.getDataMap());
                            }
                                break;
                            case "ENTRY_DELETE": {
                                extractor.extractWarFile((String) event.context());
                                File webInf = new File(warFolder + separator + event.context() + separator + "WEB-INF");
                                xmlAnalyzer = new WebXMLAnalyzer(webInf);
                                domainsData.put(new File(deployFolder + separator + event.context()), xmlAnalyzer.getDataMap());
                            }
                                break;
                        }
                        key.reset();
                    }
                } catch (InterruptedException e) {
                    LOG.warn("Cannot take watch service in my folder^ ", e);
                }
            }
        } catch (IOException e) {
            LOG.warn("Cannot get default file system or register path: ", e);
        }
    }

    @Override
    public Map<File, Map<String, Class>> getDomainsData() {
        return domainsData;
    }
}

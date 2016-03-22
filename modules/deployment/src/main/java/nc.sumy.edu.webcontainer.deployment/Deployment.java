package nc.sumy.edu.webcontainer.deployment;


import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public interface Deployment {

    ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> getDomainsData();

}

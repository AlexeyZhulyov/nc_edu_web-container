package nc.sumy.edu.webcontainer.deployment;


import java.io.File;
import java.util.Map;

public interface Deployment {

    Map<File, Map<String, Class>> getDomainsData();

}

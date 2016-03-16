package nc.sumy.edu.webcontainer.deployment;


import java.util.Map;

public interface Deployment {

    void listenDeployDirectory();

    Map<String, Class> getServletInfo();

}

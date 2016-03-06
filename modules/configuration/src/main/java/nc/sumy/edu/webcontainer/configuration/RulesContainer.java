package nc.sumy.edu.webcontainer.configuration;


import java.util.Set;

public interface RulesContainer {

    String order();

    Set<String> allow();

    Set<String> deny();


}

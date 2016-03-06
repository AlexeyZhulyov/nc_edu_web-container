package nc.sumy.edu.webcontainer.sequrity.interfaces;


import java.util.Set;

public interface RulesContainer {

    String order();

    Set<String> allow();

    Set<String> deny();


}

package nc.sumy.edu.webcontainer.configuration.security;


import java.util.Set;

public interface RulesContainer {

    String getOrder();

    Set<String> getAllow();

    Set<String> getDeny();


}

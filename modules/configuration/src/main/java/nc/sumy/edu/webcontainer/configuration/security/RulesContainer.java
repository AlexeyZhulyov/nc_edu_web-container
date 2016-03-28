package nc.sumy.edu.webcontainer.configuration.security;


import java.util.Set;
/**
 * Interface that provides methods for working with rules for some amount of files
 */
public interface RulesContainer {

    String getOrder();

    Set<String> getAllow();

    Set<String> getDeny();


}

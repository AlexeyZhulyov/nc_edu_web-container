package nc.sumy.edu.webcontainer.configuration;


import java.util.Set;

/**
 * Class-containers that include http-access info about all folder.
 * @author Vinogradov Maxim
 */
public class AccessRules implements RulesContainer, Rules{
    private final String order;
    private final Set<AccessFile> files;
    private final Set<String> allow;
    private final Set<String> deny;

    public AccessRules(Set<AccessFile> files, String order, Set<String> allow, Set<String> deny) {
        this.order = order;
        this.files = files;
        this.allow = allow;
        this.deny = deny;
    }

    @Override
    public String getOrder() {
        return order;
    }

    @Override
    public Set<String> getAllow() {
        return allow;
    }

    @Override
    public Set<String> getDeny() {
        return deny;
    }

    @Override
    public Set<AccessFile> getFiles() {
        return files;
    }

}

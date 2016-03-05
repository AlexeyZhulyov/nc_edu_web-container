package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.sequrity.interfaces.AccessFile;

import java.util.Set;

public class ServerAccessFile implements AccessFile {
    private final String name;
    private final String order;
    private final Set<String> allow;
    private final Set<String> deny;

    public ServerAccessFile(String name, String order, Set<String> allow, Set<String> deny) {
        this.name = name;
        this.order = order;
        this.allow = allow;
        this.deny = deny;
    }

    @Override
    public String order() {
        return order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> allow() {
        return allow;
    }

    @Override
    public Set<String> deny() {
        return deny;
    }

}

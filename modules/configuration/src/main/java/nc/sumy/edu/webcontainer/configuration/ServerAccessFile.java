package nc.sumy.edu.webcontainer.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;
import java.util.Set;

/**
 * Class-containers that include http-access info about separated files.
 * @author Vinogradov Maxim
 */
public class ServerAccessFile implements RulesContainer, AccessFile {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (Objects.isNull(obj) || getClass() != obj.getClass()) return false;

        ServerAccessFile that = (ServerAccessFile) obj;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(order, that.order)
                .append(allow, that.allow)
                .append(deny, that.deny)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(order)
                .append(allow)
                .append(deny)
                .toHashCode();
    }
}

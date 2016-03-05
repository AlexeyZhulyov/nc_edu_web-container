package nc.sumy.edu.webcontainer.sequrity;

import nc.sumy.edu.webcontainer.sequrity.interfaces.AccessFile;
import nc.sumy.edu.webcontainer.sequrity.interfaces.Rules;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AccessContainersTest {
    private static final String FILE_NAME = "file.json";
    private static final String ORDER = "allow";
    private static final Set<String> ALLOW = new HashSet<>();
    private static final Set<String> DENY = new HashSet<>();

    static {
        ALLOW.add("93.48.37.56");
        ALLOW.add("93.44.37.53");
        ALLOW.add("13.49.37.18");
        DENY.add("95.49.37.54");
        DENY.add("93.49.37.58");
    }

    @Test
    public void serverAccessFile() {
        AccessFile file = new ServerAccessFile(FILE_NAME, ORDER, ALLOW, DENY);
        assertEquals(file.getName(), FILE_NAME);
        assertEquals(file.allow(), ALLOW);
        assertEquals(file.deny(), DENY);
        assertEquals(file.order(), ORDER);
    }

    @Test
    public void accessRules() {
        Set<AccessFile> accessFiles = new HashSet<>();
        Rules rules = new AccessRules(accessFiles, ORDER, ALLOW, DENY);
        assertEquals(rules.getFiles(), accessFiles);
        assertEquals(rules.allow(), ALLOW);
        assertEquals(rules.deny(), DENY);
        assertEquals(rules.order(), ORDER);

    }
}

package nc.sumy.edu.webcontainer.configuration;

import nc.sumy.edu.webcontainer.configuration.security.AccessRules;
import nc.sumy.edu.webcontainer.configuration.security.ServerAccessFile;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AccessContainersTest {
    private final static String ORDER = "allow";
    private final static Set<String> ALLOW = new HashSet<>();
    private final static Set<String> DENY = new HashSet<>();

    @Test
    public void serverAccessFile() {
        String fileName = "file.json";
        ServerAccessFile file = new ServerAccessFile(fileName, ORDER, ALLOW, DENY);
        assertEquals(file.getName(), fileName);
        assertEquals(file.getAllow(), ALLOW);
        assertEquals(file.getDeny(), DENY);
        assertEquals(file.getOrder(), ORDER);
    }

    @Test
    public void accessRules() {
        Set<ServerAccessFile> accessFiles = new HashSet<>();
        AccessRules rules = new AccessRules(accessFiles, ORDER, ALLOW, DENY);
        assertEquals(rules.getFiles(), accessFiles);
        assertEquals(rules.getAllow(), ALLOW);
        assertEquals(rules.getDeny(), DENY);
        assertEquals(rules.getOrder(), ORDER);

    }
}

package nc.sumy.edu.webcontainer.configuration.security;

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
    public void serverAccessFileEqualsTest() {
        String fileName = "file.json";
        ServerAccessFile file = new ServerAccessFile(fileName, ORDER, ALLOW, DENY);
        ServerAccessFile comparedSame = new ServerAccessFile(fileName, ORDER, ALLOW, DENY);
        ServerAccessFile comparedOther = new ServerAccessFile("otherPath.json", ORDER, ALLOW, DENY);
        assertEquals("file.equals(file) must return true", true, file.equals(file));
        //assertEquals("file.equals(null) must return false", false, file.equals(null));
        assertEquals("Equals used on equal files must return true", true, file.equals(comparedSame));
        assertEquals("Equals used on nonequal files must return false", false, file.equals(comparedOther));
    }

    @Test
    public void serverAccessFileHashCoseTest() {
        String fileName = "file.json";
        ServerAccessFile file = new ServerAccessFile(fileName, ORDER, ALLOW, DENY);
        ServerAccessFile comparedSame = new ServerAccessFile(fileName, ORDER, ALLOW, DENY);
        ServerAccessFile comparedOther = new ServerAccessFile("otherPath.json", ORDER, ALLOW, DENY);
        assertEquals("Equals files must have same hashcodes", true, file.hashCode() == comparedSame.hashCode());
        assertEquals("Different files must have different hashcodes", false, file.hashCode() == comparedOther.hashCode());
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

package nc.sumy.edu.webcontainer.common;

import nc.sumy.edu.webcontainer.common.stub.TestWithPrivateConstructor;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.*;

import static org.junit.Assert.*;

public class ClassUtilTest {
    public static final String TEST = "Integer a sagittis lacus. " +
            "Ut odio nulla, viverra a nibh ut, " +
            "eleifend tincidunt risus. Cras volutpat.";

    @Test
    public void getInputStreamByNameTest() throws IOException {
        assertEquals(TEST, IOUtils.toString(ClassUtil.getInputStreamByName(ClassUtilTest.class, "Test.txt")));
    }

    @Test
    public void newInstance() {
        Class<String> klass = String.class;
        String instance = ClassUtil.newInstance(klass);
        assertSame(String.class, instance.getClass());
    }

    @Test
    public void fileToString() {
        File file = new File(ClassUtilTest.class.getResource("/Test.txt").getFile());
        assertEquals(TEST, ClassUtil.fileToString(file));
    }

    @Test
    public void addFileToSystemClassPath() {
        URL urlTest = ClassUtilTest.class.getResource("/Test.txt");
        ClassUtil.addFileToSystemClassPath(urlTest.getPath());
        for (URL url : ClassUtil.getUrlFromSysClassPath())
            if (url.getPath().contains("Test.txt"))
                return;
        fail("System class path does not contain Test.txt");
    }

    @Test
    public void addFilesFromDirToSysClassPathAbsent() throws URISyntaxException {
        ClassUtil.addFilesFromDirToSysClassPath("Absent");
        for (URL url : ClassUtil.getUrlFromSysClassPath())
            if (url.getPath().contains("Absent"))
                fail("System class path contain an absent directory");
    }

    @Test
    public void addFilesFromDirToSysClassPath() throws URISyntaxException {

        String classPackage = "nc.sumy.edu.webcontainer.common.stub";
        String simpleClassName = "TestWithPrivateConstructor";
        URL urlTest = ClassUtilTest.class.getResource("/" + classPackage.replace('.', '/') + "/" + simpleClassName + ".class");
        ClassUtil.addFilesFromDirToSysClassPath(new File(urlTest.toURI()).getParentFile().getPath());

        for (URL url : ClassUtil.getUrlFromSysClassPath())
            if (url.getPath().contains(simpleClassName))
                return;
        fail("System class path does not contain TestWithPrivateConstructor");
    }

    @Test(expected = FileNotFoundException.class)
    public void getInputStreamByNameTestNull() {
        ClassUtil.getInputStreamByName(ClassUtilTest.class, "Absent.txt");
    }

    @Test(expected = InstanceNotCreatedException.class)
    public void newInstanceException() {
        ClassUtil.newInstance(TestWithPrivateConstructor.class);
    }

    @Test(expected = FileNotReadException.class)
    public void fileToStringException() {
        ClassUtil.fileToString(new File("Absent"));
    }
}
package nc.sumy.edu.webcontainer.common;

import nc.sumy.edu.webcontainer.common.stub.TestWithPrivateConstructor;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static nc.sumy.edu.webcontainer.common.ClassUtil.*;
import static org.junit.Assert.*;


public class ClassUtilTest {
    public static final String TEST = "Integer a sagittis lacus. " +
            "Ut odio nulla, viverra a nibh ut, " +
            "eleifend tincidunt risus. Cras volutpat.";

    @Test
    public void getInputStreamByNameTest() throws IOException {
        assertEquals(TEST, IOUtils.toString(getInputStreamByName(ClassUtilTest.class, "Test.txt")));
    }

    @Test
    public void newInstance() {
        Class<String> klass = String.class;
        String instance = ClassUtil.newInstance(klass);
        assertSame(String.class, instance.getClass());
    }

    @Test
    public void fileToString(){
        File file = new File(ClassUtilTest.class.getResource("/Test.txt").getFile());
        assertEquals(TEST, ClassUtil.fileToString(file));
    }

    @Test(expected = FileNotFoundException.class)
    public void getInputStreamByNameTestNull() {
        getInputStreamByName(ClassUtilTest.class, "Absent.txt");
    }

    @Test(expected = InstanceNotCreatedException.class)
    public void newInstanceException() {
        ClassUtil.newInstance(TestWithPrivateConstructor.class);
    }

    @Test(expected = FileNotReadException.class)
    public void fileToStringException(){
        ClassUtil.fileToString(new File("Absent"));
    }
}
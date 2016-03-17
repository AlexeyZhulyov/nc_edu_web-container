package nc.sumy.edu.webcontainer.common;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static nc.sumy.edu.webcontainer.common.ClassUtil.*;
import static org.junit.Assert.*;


public class ClassUtilTest {

    @Test
    public void getInputStreamByNameTest() throws IOException {
        String test = "Integer a sagittis lacus. " +
                "Ut odio nulla, viverra a nibh ut, " +
                "eleifend tincidunt risus. Cras volutpat.";
        assertEquals(test, IOUtils.toString(getInputStreamByName(ClassUtilTest.class, "Test.txt")));
    }

    @Test
    public void newInstance() {
        Class<String> klass = String.class;
        String instance = nc.sumy.edu.webcontainer.common.ClassUtil.newInstance(klass);
        assertSame(String.class, instance.getClass());
    }

    @Test(expected = FileNotFoundException.class)
    public void getInputStreamByNameTestNull() throws IOException {
        getInputStreamByName(ClassUtilTest.class, "Absent.txt");
    }

    @Test(expected = InstanceNotCreatedException.class)
    public void newInstanceException() {
        nc.sumy.edu.webcontainer.common.ClassUtil.newInstance(nc.sumy.edu.webcontainer.common.stub.Test.class);
    }
}
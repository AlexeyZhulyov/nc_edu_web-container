package nc.sumy.edu.webcontainer.configuration;


import java.io.InputStream;

public class ClasspathUtils {
    static InputStream getInputStreamByName(Class clazz, String fileName) {
        return clazz.getResourceAsStream("/" + fileName);
    }
}

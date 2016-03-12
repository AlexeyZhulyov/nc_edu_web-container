package nc.sumy.edu.webcontainer.configuration;


import java.io.InputStream;

public class ClasspathUtils {
    static InputStream getInputStreamByName(Class clazz, String fileName) {
        InputStream inputStream = clazz.getResourceAsStream("/" + fileName);
        if (inputStream == null) {
            throw new JsonReadingException("Unable to find the file by name " + fileName);
        }
        return inputStream;
    }
}

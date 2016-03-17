package nc.sumy.edu.webcontainer.common;

import java.io.InputStream;

public class ClassUtil {

    public static InputStream getInputStreamByName(Class klass, String fileName) {
        InputStream inputStream = klass.getResourceAsStream("/" + fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        } else {
            return inputStream;
        }
    }

    public static <T> T newInstance(Class<T> klass) {
        try {
            return klass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InstanceNotCreatedException(klass.getCanonicalName(), e);
        }
    }
}

package nc.sumy.edu.webcontainer.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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

    public static String fileToString(File file){
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new FileNotReadException(file.getName(), e);
        }
    }
}

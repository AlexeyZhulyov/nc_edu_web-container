package nc.sumy.edu.webcontainer.common;

import org.apache.commons.io.FileUtils;

import javax.management.ReflectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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

    public static String fileToString(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new FileNotReadException(file.getName(), e);
        }
    }

    public static void addURLToSystemClassPath(URL u) {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        Method method = null;
        try {
            method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{u});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SystemClassloaderException(e);
        }
    }

    public static void addFileToSystemClassPath(String s) {
        File f = new File(s);
        addFileToSystemClassPath(f);
    }

    public static void addFileToSystemClassPath(File f) {
        try {
            addURLToSystemClassPath(f.toURI().toURL());
        } catch (IOException e) {
            throw new FileNotFoundException(f.getName(), e);
        }
    }

    public static void addFilesFromDirToSysClassPath(String libPath) {
        File lib = new File(libPath);
        if (lib != null && lib.exists()) {

            for (File file : lib.listFiles()) {
                ClassUtil.addFileToSystemClassPath(file);
            }
        }
    }
}

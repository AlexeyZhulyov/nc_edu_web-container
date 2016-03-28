package nc.sumy.edu.webcontainer.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
* Class with util methods.
*/
public class ClassUtil {

/**
* Find by name in klass classpath, read and return file in InputStream. 
*/
    public static InputStream getInputStreamByName(Class klass, String fileName) {
        InputStream inputStream = klass.getResourceAsStream("/" + fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(fileName);
        } else {
            return inputStream;
        }
    }

/**
* Create and return new instance of class klass.
*/
    public static <T> T newInstance(Class<T> klass) {
        try {
            return klass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InstanceNotCreatedException(klass.getCanonicalName(), e);
        }
    }

/**
* Write file to string and return string.
*/
    public static String fileToString(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new FileNotReadException(file.getAbsolutePath(), e);
        }
    }
/**
* Add URL to System ClassPath.
*/
    public static void addURLToSystemClassPath(URL url) {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        Method method = null;
        try {
            method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{url});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SystemClassloaderException(e);
        }
    }
/**
* Add directory defined as string path to System ClassPath.
*/
    public static void addFileToSystemClassPath(String dir) {
        File file = new File(dir);
        addFileToSystemClassPath(file);
    }
/**
* Add directory defined as file to System ClassPath.
*/
    public static void addFileToSystemClassPath(File dir) {
        try {
            addURLToSystemClassPath(dir.toURI().toURL());
        } catch (IOException e) {
            throw new FileNotFoundException(dir.getName(), e);
        }
    }
/**
* Add files in directory defined as string path to System ClassPath.
*/
    public static void addFilesFromDirToSysClassPath(String libPath) {
        File lib = new File(libPath);
        if (lib != null && lib.exists()) {

            for (File file : lib.listFiles()) {
                ClassUtil.addFileToSystemClassPath(file);
            }
        }
    }
}

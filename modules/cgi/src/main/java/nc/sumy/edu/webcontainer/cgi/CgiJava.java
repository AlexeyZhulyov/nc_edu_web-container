package nc.sumy.edu.webcontainer.cgi;

import org.atteo.classindex.ClassIndex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class CgiJava implements CgiHandler {

    private final Properties properties = System.getProperties();

    public void setEnvironmentVariable(String name, String value) {
        properties.setProperty(name, value);
    }

    @Override
    public String process(String className, Map<String, String> parameters) {

        Class newClass = searchClass(className);

        String generateResult = invokeGenerateMethod(newClass, parameters);

        return generateResult;
    }

    private Class searchClass(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class)) {
            if (klass.getSimpleName().equals(className))
                return klass;
            //klass.getAnnotation(Cgi.class).id();
        }
        throw new CgiException("Class \"" + className + "\" not found");
    }

    private String invokeGenerateMethod(Class klass, Map<String, String> parameters) {
        String generateResult = null;
        try {
            Object instance = klass.newInstance();
            Class[] argTypes = new Class[]{Map.class};
            Method generate = klass.getDeclaredMethod("generate", argTypes);
            Map<String, String> generateArgs = parameters;
            generateResult = (String) generate.invoke(instance, (Object) generateArgs);
        } catch (NoSuchMethodException|IllegalAccessException e) {
            throw new CgiException("Cannot invoke method \"generate\"", e);
        } catch (InvocationTargetException|InstantiationException e) {
            throw new CgiException("Cannot create new instance", e);
        }
        return generateResult;
    }
}
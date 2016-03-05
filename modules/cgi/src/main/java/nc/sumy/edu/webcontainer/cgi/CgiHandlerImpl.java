package nc.sumy.edu.webcontainer.cgi;

import org.atteo.classindex.ClassIndex;

import java.util.Map;
import java.util.Properties;

import static nc.sumy.edu.webcontainer.cgi.CgiException.*;
import static java.lang.String.*;

public class CgiHandlerImpl implements CgiHandler {

    private final static Properties PROPERTIES = System.getProperties();

    public void setEnvironmentVariable(String name, String value) {
        PROPERTIES.setProperty(name, value);
    }

    @Override
    public String process(String className, Map<String, String> parameters) {

        Class newClass = searchClass(className);

        String generateResult = invokeGenerateMethod(newClass, parameters);

        return generateResult;
    }

    public Class searchClass(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class)) {
            if (klass.getSimpleName().equals(className))
                return klass;
            //klass.getAnnotation(Cgi.class).id();
        }
        throw new CgiException(format(CLASS_NOT_FOUND, className));
    }

    private String invokeGenerateMethod(Class klass, Map<String, String> parameters) {
        String generateResult = null;
        if (!CgiAction.class.isAssignableFrom(klass)) {
            throw new CgiException(format(INVALID_CLASS, klass.getSimpleName()));
        }
        try {
            CgiAction instance = (CgiAction) klass.newInstance();
            Map<String, String> generateArgs = parameters;
            generateResult = instance.generate(generateArgs);
        } catch (IllegalAccessException e) {
            throw new CgiException(format(CANNOT_INVOKE_METHOD, "generate"), e);
        } catch (InstantiationException e) {
            throw new CgiException(CANNOT_CREATE_INSTANCE, e);
        }
        return generateResult;
    }
}
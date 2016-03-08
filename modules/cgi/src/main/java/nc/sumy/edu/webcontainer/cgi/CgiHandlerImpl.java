package nc.sumy.edu.webcontainer.cgi;

import org.atteo.classindex.ClassIndex;

import java.util.Map;

import static nc.sumy.edu.webcontainer.cgi.CgiException.*;
import static java.lang.String.*;

public class CgiHandlerImpl implements CgiHandler {

    @Override
    public String process(String className, Map<String, String> parameters) {
        return run(find(className), parameters);
    }

    public Class find(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class))
            if (klass.getSimpleName().equals(className))
                return klass;
        throw new CgiException(format(CLASS_NOT_FOUND, className));
    }

    protected String run(Class klass, Map<String, String> args) {
        if (!CgiAction.class.isAssignableFrom(klass))
            throw new CgiException(format(INVALID_CLASS, klass.getSimpleName()));
        try {
            CgiAction instance = (CgiAction) klass.newInstance();
            return instance.run(args);
        } catch (IllegalAccessException e) {
            throw new CgiException(format(CANNOT_INVOKE_METHOD, "run"), e);
        } catch (InstantiationException e) {
            throw new CgiException(CANNOT_CREATE_INSTANCE, e);
        }
    }
}
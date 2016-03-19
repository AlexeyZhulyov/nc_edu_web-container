package nc.sumy.edu.webcontainer.cgi;

import nc.sumy.edu.webcontainer.common.ClassUtil;
import org.atteo.classindex.ClassIndex;

import java.util.Map;

public class CgiHandlerImpl implements CgiHandler {

    @Override
    public String process(String className, Map<String, String> parameters) {
        return run(find(className), parameters);
    }

    public Class<CgiAction> find(String className) {
        for (Class<?> klass : ClassIndex.getAnnotated(Cgi.class))
            if (klass.getSimpleName().equals(className))
                return (Class<CgiAction>) klass;
        throw new CgiClassNotFoundException(className);
    }

    protected String run(Class<CgiAction> klass, Map<String, String> args) {
        if (!CgiAction.class.isAssignableFrom(klass))
            throw new CgiInvalidClassException(klass.getSimpleName());
        CgiAction instance = ClassUtil.newInstance(klass);
        return instance.run(args);
    }
}
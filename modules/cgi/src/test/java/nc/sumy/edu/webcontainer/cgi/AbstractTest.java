package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

@Cgi
public abstract class AbstractTest {
    public String generate(Map<String, String> parameters) {
        return "This class calling InstantiationException";
    }

    public abstract void someAbstractMethod();
}

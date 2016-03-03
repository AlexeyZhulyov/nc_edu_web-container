package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

public interface CgiHandler {

    String process(String className, Map<String, String> parameters);
}

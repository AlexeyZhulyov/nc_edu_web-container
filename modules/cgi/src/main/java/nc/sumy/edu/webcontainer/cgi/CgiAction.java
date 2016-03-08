package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

public interface CgiAction {
    String run(Map<String, String> parameters);
}

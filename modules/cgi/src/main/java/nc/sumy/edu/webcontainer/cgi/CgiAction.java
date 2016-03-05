package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

public interface CgiAction {
    String generate(Map<String,String> parameters);
}

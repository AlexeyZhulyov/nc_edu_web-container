package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

/**
* Interface that provides method for working with cgi programm.
*/

public interface CgiHandler {

/**
* Process string from class using parameters.
*/
    String process(String className, Map<String, String> parameters);
}

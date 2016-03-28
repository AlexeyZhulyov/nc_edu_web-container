package nc.sumy.edu.webcontainer.cgi;

import java.util.Map;

/**
* Defines an interface that provide method for running program on server side.
* The server create an instance of CgiAction and invoke run method.
*/

public interface CgiAction {	
/**
 * This method generate response body.
 */
    String run(Map<String, String> parameters);
}

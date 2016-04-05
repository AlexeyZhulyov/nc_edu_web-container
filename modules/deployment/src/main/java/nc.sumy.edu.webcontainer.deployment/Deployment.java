package nc.sumy.edu.webcontainer.deployment;


import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface that provides method for working with data of servlets and jsp in folder "www".
 * @author Vinogradov M.O.
 */
public interface Deployment {

    /**
     * Method that gives data about servlets and jsp.
     * @return ConcurrentHashMap that contains:
     * key   - domain-folder that contains servlets or jsp;
     * value - ConcurrentHashMap that contains:
     *      key   - url-mapping of servlet;
     *      value - servlet-class.
     */
    ConcurrentHashMap<File, ConcurrentHashMap<String, Class>> getDomainsData();

}

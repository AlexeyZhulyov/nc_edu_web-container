package nc.sumy.edu.webcontainer.sequrity;

/**
 * Interface that provides method for working with http-access.json files.
 */
public interface Security {

    /**
     * @return true if http-access.json allows to show this page,
     * otherwise - return false.
     */
    boolean isAllow();

}

package nc.sumy.edu.webcontainer.sequrity;

/**
 * Interface that provides method for working with http-access.json files.
 */
public interface Security {

    /**
     * @return is http-access.json allow to show this page.
     */
    boolean isAllow();

}

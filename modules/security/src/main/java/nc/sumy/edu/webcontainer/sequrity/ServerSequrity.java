package nc.sumy.edu.webcontainer.sequrity;

/**
 * Class that provides server security and access to it's resources.
 * @author Vinogradov Maxim
 */
public class ServerSequrity implements Security{

    @Override
    public boolean isAllow() {
        return false;
    }
}

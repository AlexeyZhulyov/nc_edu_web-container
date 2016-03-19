package nc.sumy.edu.webcontainer.common;

public class InstanceNotCreatedException extends RuntimeException {
    public InstanceNotCreatedException(String className, Throwable cause) {
        super("Unable to create instance of class " + className, cause);
    }
}

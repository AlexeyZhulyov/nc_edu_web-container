package nc.sumy.edu.webcontainer.cgi;

/**
* General exception for cgi module.
*/

public class CgiException extends RuntimeException {

    public static final String CLASS_NOT_FOUND = "Class \"%s\" not found";
    public static final String INVALID_CLASS = "Invalid class: class \"%s\" should implement CgiAction";

    public CgiException(String message) {
        super(message);
    }

}

package nc.sumy.edu.webcontainer.configuration;

import com.google.gson.JsonSyntaxException;

public class JSONConfigurationReadingException extends RuntimeException {
    public JSONConfigurationReadingException(String s, Exception e) {
        super(s, e);
    }
}

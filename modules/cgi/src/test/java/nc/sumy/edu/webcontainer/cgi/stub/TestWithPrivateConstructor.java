package nc.sumy.edu.webcontainer.cgi.stub;

import nc.sumy.edu.webcontainer.cgi.Cgi;
import nc.sumy.edu.webcontainer.cgi.CgiAction;

import java.util.Map;

@Cgi
public class TestWithPrivateConstructor implements CgiAction {

    private TestWithPrivateConstructor() {
        System.out.println("This class calling NoAccessException");
    }

    public TestWithPrivateConstructor(String message) {
        System.out.println(message);
    }

    @Override
    public String run(Map<String, String> parameters) {
        return "Class for test";
    }
}

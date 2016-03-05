package nc.sumy.edu.webcontainer.cgi.stub;

import nc.sumy.edu.webcontainer.cgi.Cgi;
import nc.sumy.edu.webcontainer.cgi.CgiAction;

import java.util.Map;

@Cgi
public class Test implements CgiAction {
    public String run(Map<String, String> parameters) {
        String result;

        result = ("Content-type: text/html\n\n");

        String name = parameters.get("login");

        String top = "<html>\n";
        top += "<head>\n";
        top += "<title>\n";
        top += "Hello There " + name + "!";
        top += "\n";
        top += "</title>\n";
        top += "</head>\n";
        top += "<body>\n";

        result += top;
        result += "<h1 align=center>Hello There " + name +
                "!</h1>";

        result += "</body>\n</html>\n";

        return result;
    }

}
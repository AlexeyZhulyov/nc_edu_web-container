package nc.sumy.edu.webcontainer.cgi;


import nc.sumy.edu.webcontainer.http.Request;

public interface CgiHandler {
    public String process(Request request);
}

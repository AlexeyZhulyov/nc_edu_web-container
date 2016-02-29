package nc.sumy.edu.webcontainer.cgi;


import nc.sumy.edu.webcontainer.http.Request;

public interface CGI {
    public String process(Request request);
}

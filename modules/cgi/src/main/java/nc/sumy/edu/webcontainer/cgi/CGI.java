package nc.sumy.edu.webcontainer.cgi;


import nc.sumy.edu.webcontainer.http2java.Request;

public interface CGI {
    public String process(Request request);
}

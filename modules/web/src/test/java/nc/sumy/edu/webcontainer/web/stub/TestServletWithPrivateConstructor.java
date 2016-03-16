package nc.sumy.edu.webcontainer.web.stub;

import javax.servlet.http.HttpServlet;

public class TestServletWithPrivateConstructor extends HttpServlet {
    private TestServletWithPrivateConstructor(){}

    public TestServletWithPrivateConstructor(String message){
        System.out.println(message);
    }
}

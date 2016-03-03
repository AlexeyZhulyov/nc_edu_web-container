package nc.sumy.edu.webcontainer.cgi;

@Cgi
public class TestWithPrivateConstructor {

    private TestWithPrivateConstructor(){
        System.out.println("This class calling NoAccessException");
    }

    public TestWithPrivateConstructor(String message){
        System.out.println(message);
    }
}

package nc.sumy.edu.webcontainer.common.stub;

public class TestWithPrivateConstructor {
    private TestWithPrivateConstructor() {
    }

    public TestWithPrivateConstructor(String message) {
        System.out.println(message);
    }
}

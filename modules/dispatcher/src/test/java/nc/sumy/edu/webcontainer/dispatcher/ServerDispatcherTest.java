package nc.sumy.edu.webcontainer.dispatcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerDispatcherTest {

    @Test
    public void getResponse() {
        ServerDispatcher dispatcher = new ServerDispatcher();
        assertEquals(dispatcher.getResponse(), null);
    }
}

package actors;

import akka.actor.ActorRef;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;



public class SearchSchedulerActorProtocolTest {

 
    @Test
    public void testProtocol() {
        SearchSchedulerActorProtocol protocol = new SearchSchedulerActorProtocol();
        assertThat(protocol, is(notNullValue()));
    }

   
    @Test
    public void testRefreshAll() {
        SearchSchedulerActorProtocol.RefreshAll refreshAll = new SearchSchedulerActorProtocol.RefreshAll();
        assertThat(refreshAll, is(notNullValue()));
    }

  
    @Test
    public void testRegister() {
        SearchSchedulerActorProtocol.Register register = new SearchSchedulerActorProtocol.Register(null);
        assertThat(register, is(notNullValue()));
    }
}
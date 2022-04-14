package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;


public class SearchSchedulerActorTest extends JUnitSuite {

  
    private static ActorSystem testSystem;

    @BeforeClass
    public static void setup() {
        testSystem = ActorSystem.create();
    }

   
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(testSystem);
        testSystem = null;
    }

    @Test
    public void testTwitterSearchSchedulerActorRegisterThenRefreshAll() {
        new TestKit(testSystem) {{

            final TestKit searchActorMock = new TestKit(testSystem);
            final TestKit schedulerMechanismMock = new TestKit(testSystem);

            final Props prop = Props.create(SearchSchedulerActor.class);

            ActorRef twitterSearchSchedulerActorRef = testSystem.actorOf(prop);

            twitterSearchSchedulerActorRef.tell(
                    new SearchSchedulerActorProtocol.Register(searchActorMock.getRef()),
                    searchActorMock.getRef());

            twitterSearchSchedulerActorRef.tell(
                    new SearchSchedulerActorProtocol.RefreshAll(),
                    schedulerMechanismMock.getRef());

            searchActorMock.expectMsgClass(duration("3 second"), SearchActorProtocol.Refresh.class);
        }};
    }
}
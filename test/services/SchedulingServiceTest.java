package services;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import com.miguno.akka.testing.VirtualTime;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;


public class SchedulingServiceTest {

	static ActorSystem system;
	

    @BeforeClass
    public static void setup() {
       	Config customConf = ConfigFactory.parseString("akka.actor.default-dispatcher { type=\"akka.testkit.CallingThreadDispatcherConfigurator\"}");
        system = ActorSystem.create("ActorSystem", ConfigFactory.load(customConf));
    }

    
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    
 
        
    @Test
    public void whenStartingSchedulerThenSchedulerActorStartsReceivingRefreshAllMessages() {
    	
    	VirtualTime virtualTime = new VirtualTime();
    	
        new TestKit(system) {{
        	
        	final TestKit probe = new TestKit(system);
        	SchedulingService schedulingService = new SchedulingService(system);
        	schedulingService.startScheduler(virtualTime.scheduler(), probe.getRef());
        	virtualTime.advance(Duration.create(3, TimeUnit.SECONDS));
        	
        	probe.expectMsgClass(duration("3 second"), actors.SearchSchedulerActorProtocol.RefreshAll.class);
        }};
    }
}

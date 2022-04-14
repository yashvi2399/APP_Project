package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import models.Employer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import play.libs.Json;
import services.SearchProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class SearchActorTest extends JUnitSuite {

    
    static ActorSystem system;

 
    static private SearchProjectService searchProjectService = mock(SearchProjectService.class);

 
    static private List<Display> display;


    @BeforeClass
    public static void setup() {
        Map<String, List<Display>> searchResultMap = new HashMap<>();
        searchResultMap.put("query", display);
        when(searchProjectService.getTenDisplaysForKeyword(any(List.class))).
                thenReturn(CompletableFuture.supplyAsync(() -> searchResultMap));
        Employer user1 = new Employer();
        user1.setId("215369");

        Employer user2 = new Employer();
        user2.setId("985514");

        Display project1 = new Display();
        project1.setTitle("Writer");

        Display project2 = new Display();
        project2.setTitle("Planner");

        display = new ArrayList<>();
        display.add(project1);
        display.add(project2);

        system = ActorSystem.create();
    }

  
    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

 
    @Test
    public void testActorRegister() {
        new TestKit(system) {{
            final Props props = Props.create(SearchActor.class, getRef(), getRef(), searchProjectService);
            system.actorOf(props);

            expectMsgClass(duration("1 second"), actors.SearchSchedulerActorProtocol.Register.class);
        }};
    }


    @Test
    public void testActorSearch() {
        new TestKit(system) {{
            SearchActorProtocol.Search search = new SearchActorProtocol.Search();
            search.setSearchKey("val1");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(SearchActor.class, probe1.getRef(), probe2.getRef(), searchProjectService);
            final ActorRef tsa = system.actorOf(props);
            probe2.expectMsgClass(duration("1 second"), actors.SearchSchedulerActorProtocol.Register.class);
            tsa.tell(search, probe1.getRef());
            JsonNode searchResult = Json.parse("{\"query\":null}");
            probe1.expectMsg(duration("3 second"), searchResult);
        }};
    }

    
    @Test
    public void testActorRefresh() {
        new TestKit(system) {{
            SearchActorProtocol.Search search = new SearchActorProtocol.Search();
            search.setSearchKey("val1");
            final TestKit probe1 = new TestKit(system);
            final TestKit probe2 = new TestKit(system);
            final Props props = Props.create(SearchActor.class, probe1.getRef(), probe2.getRef(), searchProjectService);
            final ActorRef tsa = system.actorOf(props);
            tsa.tell(search, probe1.getRef());
            tsa.tell(new SearchActorProtocol.Refresh(), probe1.getRef());
            JsonNode searchResult = Json.parse("{\"query\":null}");
            probe1.expectMsg(duration("3 second"), searchResult);
        }};
    }
}
package actors;

import actors.SearchActorProtocol.Refresh;
import actors.SearchActorProtocol.Search;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.*;
import play.libs.Json;
import services.SearchProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
/**
 * Actor for retrieving Projects using SearchProjectService.
 * @author Yash Trivedi
 */
public class SearchActor extends AbstractActor {
	
	/**
	 * Logger
	 */
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    
    /**
     * Reference of the websocket actor
     */
    private final ActorRef out;
    
    /**
     * Reference of the scheduler actor
     */
    private final ActorRef scheduler;

    /**
     * Service for searching projects
     */
    private SearchProjectService searchProjectService;
    
    /**
     * History of past search keywords
     */
    private ArrayList<String> keyWords = new ArrayList<>();

    /**
     * Creates a new SearchActor
     * @param out                        Actor to send projects search service results to
     * @param scheduler                  Scheduler actor
     * @param searchProjectService projects search service
     */
    public SearchActor(ActorRef out, ActorRef scheduler, SearchProjectService searchProjectService) {
        this.out = out;
        this.scheduler = scheduler;
        this.searchProjectService = searchProjectService;
        logger.debug("scheduler = {}", scheduler);
        this.scheduler.tell(new SearchSchedulerActorProtocol.Register(self()), self());
    }

    /**
     * Configures props to create SearchActor
     * @param out                        Actor to send projects search service results to
     * @param scheduler                  Scheduler actor
     * @param searchProjectService       projects search service
     * @return Newly created props
     */
    public static Props props(ActorRef out, ActorRef scheduler, SearchProjectService searchProjectService) {
        return Props.create(SearchActor.class, out, scheduler, searchProjectService);
    }

    /**
     * Handles refresh and search messages
     * Search message - adds a search phrase to a list the Actor would query
     * Refresh message - triggers querying projects search service
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Refresh.class, newRefresh -> {
                    //logger.debug("search actor refreshed");
                    if (keyWords.size() > 0) {
                        CompletionStage<Map<String, List<Display>>> reply = searchProjectService.getTenDisplaysForKeyword(keyWords);
                        reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                    }
                })
                .match(Search.class, newSearch -> {
                    keyWords.add(newSearch.searchKey);
                    logger.debug("match Search.class keyWords = {}", keyWords.toString());
                    CompletionStage<Map<String, List<Display>>> reply = searchProjectService.getTenDisplaysForKeyword(keyWords);
                    reply.thenAccept(r -> out.tell(Json.toJson(r), self()));
                })
                .build();
    }
}
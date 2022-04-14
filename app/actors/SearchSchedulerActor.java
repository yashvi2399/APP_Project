package actors;

import actors.SearchActorProtocol.Refresh;
import actors.SearchActor;
import actors.SearchSchedulerActorProtocol.RefreshAll;
import actors.SearchSchedulerActorProtocol.Register;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


/**
 * Actor that is notified by akka scheduling mechanism every given amount of time to 
 * trigger refresh of project search results. 
 * Keeps a list of Project Search Actors to trigger at refresh time.
 * 
 * @author Abhishek Mittal
 */

public class SearchSchedulerActor extends AbstractActor {

    /**
     * A list of Actors to notify
     */
    private final Set<ActorRef> SearchActors;

    /**
     * Logger instance
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Creates a new SearchSchedulerActor
     */
    public SearchSchedulerActor() {
        SearchActors = new HashSet<>();
    }

    /**
     * Configure props to create SearchSchedulerActor
     */
    public static Props props() {
        return Props.create(SearchSchedulerActor.class);
    }

    /**
     * Logs the start time
     */
    @Override
    public void preStart() {
        log.info("SearchSchedulerActor {}-{} started at " + LocalTime.now());
    }

    /**
     * Logs the stop time of the
     */
    @Override
    public void postStop() {
        log.info("SearchSchedulerActor {}-{} stopped at " + LocalTime.now());
    }

    /**
     * Handles refresh and search messages
     * Register message - register the Actor in the list Actors to notify
     * RefreshAll message - sends Refresh message to all Actors in a list
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RefreshAll.class, p -> {

                    for (ActorRef actorRef : SearchActors) {
                        actorRef.tell(new Refresh(), getSelf());

                    }
                })
                .match(Register.class, p -> {
                    SearchActors.add(p.actorRef);
                    log.debug("new registerd Actor = {}", p.actorRef);
                    log.debug("actors = {}", SearchActors);
                })
                .build();
    }
}
package controllers;

import actors.SearchActor;
import actors.SearchActorProtocol;
import actors.SearchSchedulerActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import models.Display;
import models.Stats;

import org.webjars.play.WebJarsUtil;
import play.Logger;
import play.libs.F.Either;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import services.*;
import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements responsive controller that enables opening a Websocket with origin check to handles requests for searching projects according to keywords/phrase and displaying employer profiles.
 *
 * @author Yashvi Pithadia
 */

@Singleton
public class ResponsiveApplicationController extends Controller {

    /**
     * Actors System
     */
    private final ActorSystem actorSystem;

    /**
     * Materializer
     */
    private final Materializer materializer;

    /**
     * Component to supply Client side dependency
     */
    private final WebJarsUtil webJarsUtil;

    /**
     * Logger
     */
    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("controllers.ResponsiveApplicationController");

    /**
     * Projects search service
     */
    private SearchProjectService searchProjectService;
    

    /**
     * Execution context that wraps execution pool
     */
    private HttpExecutionContext ec;

    /**
     * Scheduling service
     */
    private SchedulingService schedulingService;

    /**
     * Scheduling Actor reference
     */
    private ActorRef schedulerActorRef;


    /**
     * Creates a new Responsive Application Controller
     *
     * @param searchProjectService       Project search service
     * @param actorSystem                Actors System
     * @param materializer               Materializer
     * @param webJarsUtil                Component to supply Client side dependency
     * @param ec                         Execution context that wraps execution pool
     * @param schedulingService          Scheduling service
     */
    @Inject
    public ResponsiveApplicationController(
            SearchProjectService searchProjectService,
            ActorSystem actorSystem,
            Materializer materializer,
            WebJarsUtil webJarsUtil,
            HttpExecutionContext ec,
            SchedulingService schedulingService) {

        this.searchProjectService = searchProjectService;
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.webJarsUtil = webJarsUtil;
        this.ec = ec;
        this.schedulingService = schedulingService;

        schedulerActorRef = actorSystem.actorOf(SearchSchedulerActor.props(), "Scheduler"); // Scheduler Part.
        schedulingService.startScheduler(actorSystem.scheduler(), schedulerActorRef);
    }

    /**
     * Renders home page
     *
     * @return promise of a result with a rendered home page.
     */
    public CompletionStage<Result> index() {

        return CompletableFuture.supplyAsync(() -> {
            return ok(responsiveDisplay.render(webJarsUtil));
        }, ec.current());
    }
    
    
    public CompletionStage<Result> skills(String keyword) {

        return CompletableFuture.supplyAsync(() -> {
            return ok(skillSearch.render(webJarsUtil, keyword));
        }, ec.current());
    }
    
    /**
     * Creates WebSocket
     * The request origin parameter is been verified.
     *
     * @return a connection upgraded to a websocket
     */
    public WebSocket websocket() {

        Logger.debug("ApplicationWSController:socket");
        return WebSocket.json(SearchActorProtocol.Search.class).acceptOrResult(request -> {
            if (sameOriginCheck(request)) {

                final CompletionStage<Either<Result, Flow<SearchActorProtocol.Search, Object, ?>>> stage =
                        CompletableFuture.supplyAsync(() -> {

                            Object flowAsObject = ActorFlow.actorRef(out ->
                                            SearchActor.props(out, schedulerActorRef, searchProjectService),
                                    actorSystem, materializer);

                            @SuppressWarnings("unchecked")
                            Flow<SearchActorProtocol.Search, Object, NotUsed> flow =
                                    (Flow<SearchActorProtocol.Search, Object, NotUsed>) flowAsObject;

                            final Either<Result, Flow<SearchActorProtocol.Search, Object, ?>> right = Either.Right(flow);
                            return right;
                        });

                return stage;
            } else {
                return forbiddenResult();
            }
        });
    }

    /**
     * Creates forbidden result
     *
     * @return a HTTP FORBIDDEN if origin check fails
     */
    private CompletionStage<Either<Result, Flow<SearchActorProtocol.Search, Object, ?>>> forbiddenResult() {
        final Result forbidden = Results.forbidden("forbidden");
        final Either<Result, Flow<SearchActorProtocol.Search, Object, ?>> left = Either.Left(forbidden);

        return CompletableFuture.completedFuture(left);
    }

    /**
     * Checks that the WebSocket address matches the origin request field
     * This is necessary to protect against Cross-Site WebSocket Hijacking
     * as WebSocket does not implement Same Origin Policy.
     */
    private boolean sameOriginCheck(Http.RequestHeader rh) {
        final Optional<String> origin = rh.header("Origin");

        if (originMatches(origin.get())) {
            logger.debug("originCheck: originValue = " + origin);
            return true;
        } else {
            logger.error("originCheck: rejecting request because Origin header value " + origin + " is not in the same origin");
            return false;
        }
    }

    /**
     * Checks origin header to match WebSocket address
     *
     * @param origin http request origin field
     * @return if the origin check was successful
     */
    private boolean originMatches(String origin) {
        List<String> allowedOrigins = Arrays.asList("localhost:9000", "localhost:19001", "35.226.147.54:80");
        return allowedOrigins.stream().anyMatch(o -> origin.contains(o));
    }
}
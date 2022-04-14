package actors;

import akka.actor.ActorRef;

/**
 * Defines message protocol for a SearchSchedulerActor
 * @author Abhishek Mittal
 */

public class SearchSchedulerActorProtocol {

	 /**
     * A message carries an Actor to register in SearchActor
     */
    public static class Register {
        /**
         * Reference to the actor
         */
        public final ActorRef actorRef;

        /**
         * Creates a message
         * @param actorRef the reference to the Actor
         */
        public Register(ActorRef actorRef) {
            this.actorRef = actorRef;
        }
    }

    /**
     * A message to trigger refresh of all Actors
     */
    public static class RefreshAll {
        /**
         * Creates a message
         */
        public RefreshAll() {
        }
    }
}

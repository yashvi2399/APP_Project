package actors;


/**
 * Defines message protocol for a SearchActor
 * @author Yash Trivedi
 */

public class SearchActorProtocol {

    /**
     * A message to carry search phrase
     */
    public static class Search {
        /**
         * Search phrase
         */
        public String searchKey;

        /**
         * Creates a message
         */
        public Search() {
        }

        /**
         * Retrieves the search phrase
         * @return search phrase
         */
        public String getSearchKey() {
            return searchKey;
        }

        /**
         * Sets the search phrase
         * @param searchKey search phrase
         */
        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }

    /**
     * A message to trigger refresh

     */
    public static class Refresh {
        /**
         * Creates a message
         */
        public Refresh() {
        }
    }
}

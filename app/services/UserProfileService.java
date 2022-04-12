package services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.*;
import models.UserProfile;
import models.UserProfileAndProjects;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

/**
 * Implements the functionality to fetch user profile info and user's last ten tweets using Twitter API.
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */
@Singleton
public class UserProfileService {
	
	/**
	 * Web services client
	 */
    private final WSClient wsClient;

    
    /**
	 * Base url of Twitter API
	 */
    private String base_url="https://www.freelancer.com/api/";
    private String user_url="users/0.1/users/";
	private String project_url="projects/0.1/projects/?owners[]=";
	String query = "empty";
	String suffix = "&compact=false&job_details=true";
   
    /**
     * Creates this service
     * 
     * @param wsClient Web service client
     * @param twitterAuth Service that provides authentication token required to use Twitter API
     */
    @Inject
    public UserProfileService(WSClient wsClient) {
      this.wsClient = wsClient;
    }
    
    /**
     * Sets base URL for Twitter API
     * @param url
     */
    public void setBaseUrl(String url) {
  		this.base_url = url;
  	}
    
    /**
     * Retrieves user profile info and last ten tweets for a given <code>userName</code>.
     * First, the service retrieves a token for Twitter API. 
     * Then it gets user's profile info and user's ten last tweets and constructs a model 
     * object with the two and returns it as output. All this operations are done asynchronously.
     * 
     * @param userName Twitter User ID
     * 
     * @return a promise of UserProfileAndTweets which is the model that combines user profile info
     * and user's ten last tweets
     */
    public CompletionStage<UserProfileAndProjects> userProfle(String id) {
    	
    	
    	CompletionStage<Employer> userProfileFuture = getUserProfile(id);
    	
    	CompletionStage<List<Display>> userLastTenTweetsFuture = getUserLastTenTweets(id);
    	
    	CompletionStage<UserProfileAndProjects> userProfileAndTweetsFuture = 
    			userProfileFuture.thenCombine(userLastTenTweetsFuture, 
    					(prof, tweets) -> new UserProfileAndProjects(prof,tweets));
    	    	    	
    	return userProfileAndTweetsFuture;
    }
    
    /**
     * Retrieves user profile info via Twitter API
     * 
     * @param accessToken token used to authorize Twitter API requests
     * @param userName Twitter User ID
     * @return a promise of UserProfile model
     */
    
    private CompletionStage<Employer> getUserProfile(String id){
			
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url(base_url.concat(user_url.concat(id))))
	        .thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result"))
	        .thenApply(r -> Json.fromJson(r, Employer.class));
    }
    
    /**
     * Interacts with twitter api to fetch 10 last tweets based on given <code>userName</code>.
     * 
     * @param accessToken token used to authorize Twitter API requests
     * @param userName Twitter User ID
     * @return promise of last ten tweets as a list
     */
    public CompletionStage<List<Display>> getUserLastTenTweets(String id){
		
		return 
			CompletableFuture.supplyAsync(() -> 
				wsClient
				.url(base_url.concat(project_url).concat(id.concat(suffix)))
		        .addQueryParameter("limit", "10"))
			.thenCompose(r -> r.get())
	        .thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result").get("projects"))
            .thenApply(r -> StreamSupport.stream(r.spliterator(), false)
					.map(x -> Json.fromJson(x, Display.class))
					.collect(Collectors.toList()));
    }
    
}
package services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.*;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;

/**
 * Implements the functionality to fetch user profile info and user's last ten projects using freelencer API.
 * @author Abhishek Mittal
 */
@Singleton
public class UserProfileService {
	
	/**
	 * Web services client
	 */
    private final WSClient wsClient;

    
    /**
	 * Base url of Freelencer API
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
     */
    @Inject
    public UserProfileService(WSClient wsClient) {
      this.wsClient = wsClient;
    }
    
    /**
     * Sets base URL for Freelencer API
     * @param url
     */
    public void setBaseUrl(String url) {
  		this.base_url = url;
  	}
    
    /**
     * 
     *  Retrieves employer info and last ten projects for a given <code>id</code>.
     * First, the service retrieves a token for freelencer API. 
     * Then it gets employee's profile info and employee's ten last projects and constructs a model object with the two and returns it as output. All this operations are done asynchronously.
     * 
     * @param id: employer ID
     * @return a promise of UserProfileAndprojects which is the model that combines user profile info
     * and employer's ten last projects
     */
    public CompletionStage<UserProfileAndProjects> userProfle(String id) {
    	
    	
    	CompletionStage<Employer> userProfileFuture = getUserProfile(id);
    	
    	CompletionStage<List<Display>> userLastTenProjectsFuture = getUserLastTenDisplays(id);
    	
    	CompletionStage<UserProfileAndProjects> userProfileAndProjectsFuture = 
    			userProfileFuture.thenCombine(userLastTenProjectsFuture, 
    					(prof, tweets) -> new UserProfileAndProjects(prof,tweets));
    	    	    	
    	return userProfileAndProjectsFuture;
    }
    
    
    /**
     * Retrieves user profile info via Freelencer API
     * 
     * @param id Employer ID
     * @return a promise of Employer model
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
     * Interacts with Freelencer api to fetch 10 last projects based on given <code>id</code>.
     * 
     * @param id: Employer ID
     * @return promise of last ten projects as a list
     */
    public CompletionStage<List<Display>> getUserLastTenDisplays(String id){
		
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
    
    public List<String> queryStringTenDisplays(String id, String title) throws InterruptedException, ExecutionException{

		CompletableFuture<List<Display>> displayList = CompletableFuture
				.supplyAsync(() -> wsClient.url(base_url.concat(project_url).concat(id.concat(suffix))).addQueryParameter("limit","10"))
				.thenCompose(r -> r.get())
				.thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result").get("projects"))
				.thenApply(r -> StreamSupport.stream(r.spliterator(), false)
						.map(x -> Json.fromJson(x, Display.class))
						.collect(Collectors.toList()));
		
		List<String> display = new ArrayList<String>();
		
		int i=0;

        for (Display jsonNode : displayList.get()) {
   
            		if(jsonNode.getTitle().equals(title)){
        			String text = jsonNode.getPreview_description();
            		display.add(text);
            		}

			}
        return display;
		}
    
}
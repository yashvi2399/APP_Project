package services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;

import models.*;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
/**
 * 
 * Implements functionality of fetching 10 projects based on search phrase.
 * @author Tanvi Patel
 */
@Singleton
public class SearchProjectService {
	
	/**
	 * Web services client
	 */

	private final WSClient wsClient;
	
	
	/**
	 * Base URL of Freelencer API
	 */
	private String baseUrl="https://www.freelancer.com/api/projects/0.1/projects/";
	String query = "empty";
	String suffix = "&compact=false&job_details=true";
	
	/**
	 * @param wsClient Web Services client
	 */
	@Inject
	public SearchProjectService(WSClient wsClient) {
		this.wsClient = wsClient;
	}
	
	/**
	 * @param searchStrings list of phrases for which to retrieve 10 most recent projects 
	 * @return map where key is a search phrase and value is the associated list of 10 most recent projects that contain the phrase
	 */

	public CompletionStage<Map<String, List<Display>>> getTenDisplaysForKeyword(List<String> searchStrings) {


		return searchStrings
				.stream()
				.map(word -> queryTenDisplays(word))
				.reduce((a, b) -> a.thenCombine(b, (x, y) -> {
					x.putAll(y);
					return x;
				})).get();
	}
	

	/**
	 * Sets base url of Freelencer API
	 * @param url sets base url for Freelencer API
	 */
	
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	/**
	 * 
	 * Retrieves ten most recent projects with freelencer api.
	 * @param searchString search phrase for which to retrieve ten most recent projects, may contain spaces.
	 * 
	 * @return map with 1 key/value pair where key is the <code>searchString</code> 
	 * and value is the associated list of ten most recent projects that contain the 
	 * <code>searchString</code>
	 */
	public CompletionStage<Map<String, List<Display>>> queryTenDisplays(String searchString) {

		return CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl.concat("active?query=").concat(searchString.trim().replaceAll(" ", "%20")).concat(suffix))
						.addQueryParameter("limit","10"))
				.thenCompose(r -> r.get())
				.thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result").get("projects"))
				.thenApply(r -> StreamSupport.stream(r.spliterator(), false)
						.map(x -> Json.fromJson(x, Display.class))
						.collect(Collectors.toList()))
				.thenApply(r -> {
					Map<String, List<Display>> m = new LinkedHashMap<>();
					m.put(searchString, r);
					return m;
				});
	}
	
	/**
	 * @param searchString: search phrase for which to retrieve 250 most recent projects, may contain spaces.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<String> queryAllDisplays(String searchString) throws InterruptedException, ExecutionException{

		List<String> display = new ArrayList<String>();
		
		for(int i=0;i<5;i++) {
		
		final int offset = i;
		CompletableFuture<List<Display>> displayList = CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl.concat("active?query=").concat(searchString.trim().replaceAll(" ", "%20")).concat(suffix)).addQueryParameter("offset", String.valueOf(offset*50)))
				.thenCompose(r -> r.get())
				.thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result").get("projects"))
				.thenApply(r -> StreamSupport.stream(r.spliterator(), false)
						.map(x -> Json.fromJson(x, Display.class))
						.collect(Collectors.toList()));

        for (Display jsonNode : displayList.get()) {
   
            		String text = jsonNode.getPreview_description();
            		display.add(text);

			}
		}
        return display;
		}
	
	/**
	 * @param searchString: search phrase for which to retrieve ten most recent projects, may contain spaces.
	 * @param title project name
	 * @return list of project descriptions
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public List<String> queryStringTenDisplays(String searchString, String title) throws InterruptedException, ExecutionException{

		CompletableFuture<List<Display>> displayList = CompletableFuture
				.supplyAsync(() -> wsClient.url(baseUrl.concat("active?query=").concat(searchString.trim().replaceAll(" ", "%20")).concat(suffix)).addQueryParameter("limit","10"))
				.thenCompose(r -> r.get())
				.thenApply(r -> r.getBody(WSBodyReadables.instance.json()).get("result").get("projects"))
				.thenApply(r -> StreamSupport.stream(r.spliterator(), false)
						.map(x -> Json.fromJson(x, Display.class))
						.collect(Collectors.toList()));
		
		List<String> display = new ArrayList<String>();

        for (Display jsonNode : displayList.get()) {
   
            		if(jsonNode.getTitle().equals(title)){
        			String text = jsonNode.getPreview_description();
            		display.add(text);
            		}

			}
        return display;
		}
}
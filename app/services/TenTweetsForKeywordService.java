package services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import com.google.inject.Singleton;

import models.*;
import play.libs.Json;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
/**
 * 
 * Implements functionality of fetching 10 tweets based on search phrase.
 * @author Mayank Acharya
 * @version 1.0.0
 */

@Singleton
public class TenTweetsForKeywordService {
	
	/**
	 * Web services client
	 */

	private final WSClient wsClient;
	
	
	/**
	 * Base URL of Twitter API
	 */
	private String baseUrl="https://www.freelancer.com/api/projects/0.1/projects/";
	String query = "empty";
	String suffix = "&compact=false&job_details=true";
	
	/**
	 * Creates this service
	 * @param wsClient Web Services client
	 * @param twitterAuth Twitter Authentication service that provide auth token
	 */
	@Inject
	public TenTweetsForKeywordService(WSClient wsClient) {
		this.wsClient = wsClient;
	}
	
	/**
	 * Retrieves 10 most recent tweets for each of search phrases provided as input
	 * 
	 * @param searchStrings list of phrases for which to retrieve 10 most recent tweets  
	 * @return map where key is a search phrase and value is the associated 
	 * list of 10 most recent tweets that contain the phrase
	 */

	public CompletionStage<Map<String, List<Display>>> getTenTweetsForKeyword(List<String> searchStrings) {

		System.out.println("TEN TWEETS");
		return searchStrings
				.stream()
				.map(word -> queryTenTweets(word))
				.reduce((a, b) -> a.thenCombine(b, (x, y) -> {
					x.putAll(y);
					return x;
				})).get();
	}
	
	/**
	 * Sets base url of Twitter API
	 * @param url sets base url for Twitter API
	 */
	
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	/**
	 * 
	 * Retrieves ten most recent tweets with twitter api.
	 * 
	 * @param token access token for authenticating with Twitter API
	 * @param searchString search phrase for which to retrieve ten most recent tweets, may contain spaces.
	 * 
	 * @return map with 1 key/value pair where key is the <code>searchString</code> 
	 * and value is the associated list of ten most recent tweets that contain the 
	 * <code>searchString</code>
	 */
	public CompletionStage<Map<String, List<Display>>> queryTenTweets(String searchString) {

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
}
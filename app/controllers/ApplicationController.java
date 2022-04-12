package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.*;
import views.html.*;

/**
 * Implements controller that handles requests for searching tweets according to keywords
 * and displaying Tweeter's user profiles.
 *  
 * @author Nikita Baranov
 * @version 1.0.0
 *
 */
@Singleton
public class ApplicationController extends Controller {
	
	/**
	 * User Profile retrieval service
	 */
	private UserProfileService userProfileService;
	
	/**
	 * Tweets search service
	 */
	private TenTweetsForKeywordService tenTweetsForKeywordService;
	
	private WordStatService wordStatService;
	/**
	 * Form Factory for managing UI forms
	 */
	private FormFactory formFactory;
	
	/**
	 * Execution context that wraps execution pool
	 */
	private HttpExecutionContext ec;
	
	/**
	 * A list of searches entered by user
	 */
	private List<String> memory = new ArrayList<>();
	
	/**
	 * Creates a new application controller
	 * @param userProfileService         User profile retrieval service
	 * @param tenTweetsForKeywordService Tweets search service
	 * @param formFactory                Form Factory
	 * @param ec                         Execution context
	 */
	@Inject
	public ApplicationController(
			UserProfileService userProfileService,
			TenTweetsForKeywordService tenTweetsForKeywordService,
			FormFactory formFactory,
			HttpExecutionContext ec){
		
		this.userProfileService = userProfileService;
		this.tenTweetsForKeywordService = tenTweetsForKeywordService;
		this.formFactory = formFactory;
		this.ec = ec;
	}

	/**
	 * Renders home page
	 * @return promise of a result with a rendered home page.
	 */
	public CompletionStage<Result> index() {
		
		return CompletableFuture.supplyAsync(() -> {
			Form<String> searchForm = formFactory.form(String.class);
			memory.clear();
			return ok(index.render(searchForm, null));
		});
	}
	
	/**
	 * Handles tweet search based on keywords.
	 * 
	 * Retrieves a search phrase from a UI form. 
	 * Then, if the phrase is not empty, updates the list of searches with this new phrase 
	 * and calls tweet search service with the full history of previous and current search phrases.
	 * 
	 * Then renders a view with the result of all search phrases.
	 * 
	 * @return promise of a result with a rendered view of tweet searches.
	 */
	public CompletionStage<Result> search() {
				
		CompletionStage<Form<String>> searchFormFuture = 
			CompletableFuture.supplyAsync(() -> {
				
				Form<String> searchForm = formFactory.form(String.class).bindFromRequest();
				String searchString = searchForm.field("searchString").getValue().get().trim();
		
				if (!searchString.isEmpty()) {
					memory.add(0, searchString);
					System.out.println(searchString);
				}
				
				return searchForm;
			}, ec.current());
		
		CompletionStage<Map<String, List<Display>>> mapFuture = 
			searchFormFuture.thenCompose(r -> {
			
				if (!memory.isEmpty()) {
					System.out.println(memory);
					return tenTweetsForKeywordService.getTenTweetsForKeyword(memory);
				}
				return
					CompletableFuture.supplyAsync(() -> {
						return null;
					});
			});
		
		return searchFormFuture.thenCombine(mapFuture, (form, map) -> ok(index.render(form, map)));
	}

	/**
	 * Retrieves user profile info and user's last 10 tweets 
	 * and renders a view with this info.
	 * 
	 * @param userProfileId Twitter account ID
	 * @return promise of a result with a rendered view of user profile info
	 */
	public CompletionStage<Result> userProfile(String id) {
		
		return userProfileService
				.userProfle(id)
				.thenApplyAsync(r -> ok(userProfile.render(r)));
	}
	
	 public CompletionStage<Result> stats(String keyword, String title) {
		 
		 return tenTweetsForKeywordService
					.queryTenTweets(keyword)
					.thenApplyAsync(r -> ok(stats.render(keyword, title, r)));
	    }
	 
	 public CompletionStage<Result> statsUser(String id, String title) {
		 
		 return userProfileService
					.getUserLastTenTweets(id)
					.thenApplyAsync(r -> ok(statsUser.render(id, title, r)));
	    }
	 
	 public CompletionStage<Result> statsGlobal(String keyword) {
		 
		 return tenTweetsForKeywordService
					.queryAllTweets(keyword)
					.thenApplyAsync(r -> ok(statsGlobal.render(keyword, r)));
	    }
	 

	 public CompletionStage<Result> flesch(String keyword, String title) {
			 
			 return tenTweetsForKeywordService
						.queryTenTweets(keyword)
						.thenApplyAsync(r -> ok(flesch.render(keyword, title, r)));
		    }
	 
	 public CompletionStage<Result> fleschUser(String id, String title) {
		 
		 return userProfileService
					.getUserLastTenTweets(id)
					.thenApplyAsync(r -> ok(fleschUser.render(id, title, r)));
	    }
	 
public CompletionStage<Result> fleschGlobal(String keyword) {
		 
		 return tenTweetsForKeywordService
					.queryAllTweets(keyword)
					.thenApplyAsync(r -> ok(fleschGlobal.render(keyword, r)));
	    }
	
}
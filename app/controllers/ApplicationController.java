package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
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
 * Implements controller that handles requests for searching project according to keywords
 * and displaying employer profiles.
 * @author Tanvi Patel
 *
 */
@Singleton
public class ApplicationController extends Controller {
	
	/**
	 * User Profile retrieval service
	 */
	private UserProfileService userProfileService;
	
	/**
	 * projects search service
	 */
	private SearchProjectService searchProjectService;
	
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
	 * @param searchProjectService       projects search service
	 * @param formFactory                Form Factory
	 * @param ec                         Execution context
	 */
	@Inject
	public ApplicationController(
			UserProfileService userProfileService,
			SearchProjectService searchProjectService,
			FormFactory formFactory,
			HttpExecutionContext ec){
		
		this.userProfileService = userProfileService;
		this.searchProjectService = searchProjectService;
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
	 * Retrieves user profile info and user's last 10 projects 
	 * and renders a view with this info.
	 * 
	 * @param Id  employer ID
	 * @return promise of a result with a rendered view of user profile info
	 */
	public CompletionStage<Result> userProfile(String id) {
		
		return userProfileService
				.userProfle(id)
				.thenApplyAsync(r -> ok(userProfile.render(r)));
	}
	
	 /**
		 * @param keyword keyword searched
		 * @param title	project title
		 * @return completion Stage of wordStat on index 
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
	 public CompletionStage<Result> stats(String keyword, String title) throws InterruptedException, ExecutionException {
		 
		 String statistics = "";
    	 
		 List<String> display = searchProjectService.queryStringTenDisplays(keyword,title);
		 
		 for(String str: display) {
			 statistics = statistics + str;
		 }
		 
		 ArrayList<Stats> statsList = WordStatService.setStats(statistics);
		 
		 return CompletableFuture.completedFuture(ok(stats.render(keyword, title,statsList)));	

	    }
	 
	 /**
		 * @param id 	employer ID
		 * @param title Project Title
		 * @return completion Stage of wordStat on User profile
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
	 public CompletionStage<Result> statsUser(String id, String title) throws InterruptedException, ExecutionException{
		 
		 String statistics = "";
    	 
		 List<String> display = userProfileService.queryStringTenDisplays(id,title);
		 
		 for(String str: display) {
			 statistics = statistics + str;
		 }
		 
		 ArrayList<Stats> statsList = WordStatService.setStats(statistics);
		 
		 return CompletableFuture.completedFuture(ok(statsUser.render(id, title,statsList)));	
	    }
	 
	 /**
		 * @param keyword search phrase
		 * @return completion Stage of global wordStat
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
	 public CompletionStage<Result> statsGlobal(String keyword) throws InterruptedException, ExecutionException{
		 
		 String statistics = "";
    	 
		 List<String> display = searchProjectService.queryAllDisplays(keyword);
		 
		 for(String str: display) {
			 statistics = statistics + str;
		 }
		 
		 ArrayList<Stats> statsList = WordStatService.setStats(statistics);
		 
		 return CompletableFuture.completedFuture(ok(statsGlobal.render(keyword, statsList)));	

	    }
	 

	 /**
	 * @param keyword Search Phrase
	 * @param title   Project Title
	 * @return HTML page
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	 public CompletionStage<Result> flesch(String keyword, String title) throws InterruptedException, ExecutionException{
			 
	    	 String statistics = "";
	    	 
			 List<String> display = searchProjectService.queryStringTenDisplays(keyword,title);
			 
			 for(String str: display) {
				 statistics = statistics + str;
			 }
			 
			 double[] val = FleschCalculator.calculateScore(statistics);
			 
			 return CompletableFuture.completedFuture(ok(flesch.render(keyword, title,val[0], val[1])));	

		    }
	 

	 /**
	 * @param id		Employer ID
	 * @param title		Project Title
	 * @return	HTML page for employee
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	 public CompletionStage<Result> fleschUser(String id, String title) throws InterruptedException, ExecutionException{
		 
		 String statistics = "";
    	 
		 List<String> display = userProfileService.queryStringTenDisplays(id,title);
		 
		 for(String str: display) {
			 statistics = statistics + str;
		 }
		 
		 double[] val = FleschCalculator.calculateScore(statistics);
		 
		 return CompletableFuture.completedFuture(ok(fleschUser.render(id, title,val[0], val[1])));	

	    }
	 
	 /**
	     * @param keyword		Search Phrase
	     * @return				HTML page
	     * @throws InterruptedException
	     * @throws ExecutionException
	     */
    public CompletionStage<Result> fleschGlobal(String keyword) throws InterruptedException, ExecutionException {
		 
    	 String statistics = "";
    	 
		 List<String> display = searchProjectService.queryAllDisplays(keyword);
		 
		 for(String str: display) {
			 statistics = statistics + str;
		 }
		 
		 double[] val = FleschCalculator.calculateScore(statistics);
		 
		 return CompletableFuture.completedFuture(ok(fleschGlobal.render(keyword, val[0], val[1])));	
		 
	    }
	
}
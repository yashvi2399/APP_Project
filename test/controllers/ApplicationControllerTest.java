package controllers;

import models.*;

import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.data.Form.Field;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import services.SearchProjectService;
import services.UserProfileService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;


public class ApplicationControllerTest {
	
	
	private UserProfileService userProfileService = mock(UserProfileService.class);
	
	
	private SearchProjectService searchProjectService = mock(SearchProjectService.class);
	
	
	private FormFactory formFactory = mock(FormFactory.class);
	
	private UserProfileAndProjects userProfileAnddisplay = mock(UserProfileAndProjects.class);
	
	
	private Employer userProfile = mock(Employer.class);
	
	
	private Form<String> stringForm = mock(Form.class);

	private Field field = mock(Field.class);
	
	
	
	private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());
	
	
	private List<Display> display;
	
	Map<String, List<Display>> data = new HashMap<>();
	List<Display> dis;
	
	
	@Before
	public void setup(){
		
		Employer user1 = new Employer();
		user1.setId("61699735");

		Employer user2 = new Employer();
		user1.setId("24962412");
		
		Display project1 = new Display();
		project1.setTitle("Content Writer");
		
		Display project2 = new Display();
		project2.setTitle("Software Developer");
		
		display = new ArrayList<>();
		display.add(project1);
		display.add(project2);
	}

	
	@Test
	public void testIndex() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		Result result = controller.index().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
        assertThat(contentAsString(result).contains("Freelancelot"), is(equalTo(true)));
	}
	
	@Test
	public void testFlesch() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		when(searchProjectService.queryTenDisplays("writer"))
		.thenReturn(CompletableFuture.supplyAsync(() -> data));
		
		Result result = controller.flesch("writer","Need%20copy%20typist").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testFleschUser() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		when(userProfileService.getUserLastTenDisplays("61699735"))
		.thenReturn(CompletableFuture.supplyAsync(() -> dis));
		
		Result result = controller.fleschUser("61712479","Need%20copy%20typist").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testFleschGlobal() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		Result result = controller.fleschGlobal("writer").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testStats() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		when(searchProjectService.queryTenDisplays("writer"))
		.thenReturn(CompletableFuture.supplyAsync(() -> data));
		
		Result result = controller.stats("writer","Need%20copy%20typist").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testStatsUser() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		Result result = controller.statsUser("61712479","Need%20copy%20typist").toCompletableFuture().get();
		
		when(userProfileService.getUserLastTenDisplays("61699735"))
		.thenReturn(CompletableFuture.supplyAsync(() -> dis));
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testStatsGlobal() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		Result result = controller.statsGlobal("writer").toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
	@Test
	public void testUserProfile() throws InterruptedException, ExecutionException {
		
		ApplicationController controller = new ApplicationController(userProfileService, 
				searchProjectService, formFactory, ec);
		
		when(userProfileService.userProfle("61699735"))
			.thenReturn(CompletableFuture.supplyAsync(() -> userProfileAnddisplay));
		when(userProfileAnddisplay.getEmployer()).thenReturn(userProfile);
		when(userProfile.getUsername()).thenReturn("Mark");
		
		Result result = controller.index().toCompletableFuture().get();
		
		assertThat(result.status(), is(equalTo(OK)));
		assertThat(result.contentType().get(), is(equalTo("text/html")));
	}
	
}

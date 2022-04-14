package services;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;


public class ServiceTest extends WithApplication{
	
	 @Override
	    protected Application provideApplication() {
	        return new GuiceApplicationBuilder().build();
	    }

	@Test
	public void testRoute() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/responsive");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute1() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/globalstats/writer");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute3() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/globalflesch/writer");
        
        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute4() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/profile/15833016");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute5() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/skills/Mobile%20App%20Development");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute6() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/profile/stats/15833016/Ionic%20tech%20support%20--%202");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	
	@Test
	public void testRoute7() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/profile/flesch/15833016/Ionic%20tech%20support%20--%202");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	
	@Test
	public void testRoute8() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/stats/writer/Grant%20writer");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute9() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/flesch/writer/Grant%20writer");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	@Test
	public void testRoute10() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(400, result.status());
    }
	
	
}


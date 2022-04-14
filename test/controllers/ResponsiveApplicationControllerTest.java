package controllers;

import Utils.WebSocketTestClient;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.webjars.play.WebJarsUtil;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.test.Helpers;
import play.test.WithServer;
import services.SchedulingService;
import services.SearchProjectService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;



public class ResponsiveApplicationControllerTest extends WithServer {


    private static ActorSystem system;


    private final Materializer materializer = mock(Materializer.class);

   
    private final WebJarsUtil webJarsUtil = mock(WebJarsUtil.class);

  
    private AsyncHttpClient asyncHttpClient;

  
    private SearchProjectService searchProjectService = mock(SearchProjectService.class);


    private SchedulingService schedulingService = mock(SchedulingService.class);

 
    private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

  
    @Before
    public void setUpHttpContext() {
        asyncHttpClient = new DefaultAsyncHttpClient();
        system = ActorSystem.create();
    }

    @After
    public void clearHttpContext() throws IOException {
        asyncHttpClient.close();
    }

 
    @Test
    public void index_success() throws InterruptedException, ExecutionException {
        ResponsiveApplicationController controller = new ResponsiveApplicationController(
                searchProjectService,
                system,
                materializer,
                webJarsUtil,
                ec,
                schedulingService);

        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive";
        Http.RequestBuilder request = fakeRequest("GET", serverURL);
        Http.Context context = Helpers.httpContext(request.build());
        Http.Context.current.set(context);

        Result result = controller.index().toCompletableFuture().get();
        Http.Context.current.remove();

        assertThat(result.status(), is(equalTo(OK)));
        assertThat(result.contentType().get(), is(equalTo("text/html")));
    }

  
    @Test
    public void websocket_success() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
        String origin = serverURL;

        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);
        WebSocket webSocket = webSocketTestClient.call(serverURL, origin).get();

        assertThat(true, is(equalTo(webSocket.isOpen())));
    }

  
    @Test(expected = ExecutionException.class)
    public void websocket_wronOrigin_error() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);

        webSocketTestClient.call(serverURL, "ws://someFakeServer:19001/responsive/websocket").get();
    }
}
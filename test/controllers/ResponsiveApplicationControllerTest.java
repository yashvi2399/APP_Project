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
import services.TenTweetsForKeywordService;

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

/**
 * Tests the functionality of Responsive Application Controller.
 *
 * @author Nikita Baranov
 * @version 1.0.0
 */

public class ResponsiveApplicationControllerTest extends WithServer {

    /**
     * Actors System
     */
    private static ActorSystem system;

    /**
     * Mock Materializer
     */
    private final Materializer materializer = mock(Materializer.class);

    /**
     * Mock Component to supply Client side dependency
     */
    private final WebJarsUtil webJarsUtil = mock(WebJarsUtil.class);

    /**
     * Async Http Client for the WebSocket
     */
    private AsyncHttpClient asyncHttpClient;

    /**
     * Mock of tweet search service
     */
    private TenTweetsForKeywordService tenTweetsForKeywordService = mock(TenTweetsForKeywordService.class);

    /**
     * Mock Scheduling service
     */
    private SchedulingService schedulingService = mock(SchedulingService.class);

    /**
     * Execution context that encapsulates inside a Fork/Join pool.
     * This is a real object and not a mock because it is used to run async operations
     */
    private HttpExecutionContext ec = new HttpExecutionContext(ForkJoinPool.commonPool());

    /**
     * Initializes objects needed for tests before each unit test
     */
    @Before
    public void setUpHttpContext() {
        asyncHttpClient = new DefaultAsyncHttpClient();
        system = ActorSystem.create();
    }

    /**
     * Clears HttpContext to clear the session data
     *
     * @throws IOException TO handle failed I/O Exception
     */
    @After
    public void clearHttpContext() throws IOException {
        asyncHttpClient.close();
    }

    /**
     * Correct view rendering
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void index_success() throws InterruptedException, ExecutionException {
        ResponsiveApplicationController controller = new ResponsiveApplicationController(
                tenTweetsForKeywordService,
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
        assertThat(contentAsString(result).contains("TweetAnalytics Assignment 1 and 2"), is(equalTo(true)));
    }

    /**
     * Calls WebSocket using WebSocketTestClient with correct origin
     * expects http request to be upgraded and WebSocket to be opened
     *
     * @throws Exception
     */
    @Test
    public void websocket_success() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
        String origin = serverURL;

        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);
        WebSocket webSocket = webSocketTestClient.call(serverURL, origin).get();

        assertThat(true, is(equalTo(webSocket.isOpen())));
    }

    /**
     * Calls WebSocket using WebSocketTestClient with incorrect origin
     */
    @Test(expected = ExecutionException.class)
    public void websocket_wronOrigin_error() throws Exception {
        String serverURL = "ws://localhost:" + this.testServer.port() + "/responsive/websocket";
        WebSocketTestClient webSocketTestClient = new WebSocketTestClient(asyncHttpClient);

        webSocketTestClient.call(serverURL, "ws://someFakeServer:19001/responsive/websocket").get();
    }
}
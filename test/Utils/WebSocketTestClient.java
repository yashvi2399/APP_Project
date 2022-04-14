package Utils;

import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;

import java.util.concurrent.CompletableFuture;

public class WebSocketTestClient {

  
    private AsyncHttpClient client;

 
    public WebSocketTestClient(AsyncHttpClient c) {
        this.client = c;
    }

    public CompletableFuture<WebSocket> call(String url, String origin) {
        BoundRequestBuilder requestBuilder;
        if (origin != null) {
            requestBuilder = client.prepareGet(url).addHeader("Origin", origin);
        } else {
            requestBuilder = client.prepareGet(url);
        }
        WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().build();
        ListenableFuture<WebSocket> future = requestBuilder.execute(handler);
        return future.toCompletableFuture();
    }
}
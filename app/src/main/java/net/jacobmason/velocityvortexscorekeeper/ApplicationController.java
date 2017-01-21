package net.jacobmason.velocityvortexscorekeeper;

import android.app.Application;
import android.util.Log;

import org.java_websocket.WebSocket;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by jacob on 12/14/16.
 */

public class ApplicationController extends Application {
    private static ApplicationController instance;
    private StompClient stompClient;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ApplicationController get_instance() {
        return instance;
    }

    public StompClient getStompClient(String ipAddress) {
        if (stompClient == null || !stompClient.isConnected()) {
            // SockJS has a .../websocket path on the socket for clients that aren't running SockJS,
            // but can support WebSockets.
            String address = "ws://" + ipAddress + ":3486/scorekeeper/ws/websocket";
            Log.d("ApplicationController", "Creating Stomp Client at " + address);
            stompClient = Stomp.over(WebSocket.class, address);
            stompClient.connect();
        }
        return stompClient;
    }
}

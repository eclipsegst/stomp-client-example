package com.example.websocketapplication;

import android.app.Application;
import android.util.Log;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class TicTacToeApplication extends Application {
    private final String TAG = "TicTacToeApplication";
    private static TicTacToeApplication application;
    private StompClient stompClient;
    private Subscriptions subscriptions;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        initWebsocket();

        String topic = "topic";

    }

    public void initWebsocket() {
        Log.d("inx", "inx initWebsocket");

        // test ws: let socket = WebSocket(url: URL(string: "ws://echo.websocket.org/")!)

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.86.148:8090/ws/websocket");
//        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.86.148:8090/ws/websocket");
        stompClient.withClientHeartbeat(3000);
//        stompClient.withServerHeartbeat(500);
        stompClient.connect();
        subscriptions = new Subscriptions();

        Log.d("inx", "inx initWebsocket stompClient.isConnected(): " + stompClient.isConnected());

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });
    }

    public void closeConnection() {
        this.subscriptions.removeAll();
        this.stompClient.disconnect();
    }

    public StompClient getStompClient() {
        return stompClient;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public static TicTacToeApplication instance() {
        return application;
    }
}


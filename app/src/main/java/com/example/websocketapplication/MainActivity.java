package com.example.websocketapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference: https://github.com/NaikSoftware/StompProtocolAndroid
        // https://spring.io/guides/gs/messaging-stomp-websocket/
        // Reference: https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#websocket-intro
        // https://github.com/SayyedUmar/Stomp-Android-Client

        TextView textView = findViewById(R.id.hello_world_text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("inx", "inx onClick");
                StompClient stompClient = TicTacToeApplication
                        .instance()
                        .getStompClient();

                Log.d(TAG, "inx stompClient.isConnected():" + stompClient.isConnected());

                if (stompClient.isConnected()) {

                    Disposable disposable1 = TicTacToeApplication.instance()
                            .getStompClient().topic("/topic/tictactoe").doOnError(throwable -> System.out.println("inx onError:" + throwable.getMessage())).subscribe(topicMessage -> {
                                Log.d(TAG, "inx Disposable tictactoe topicMessage: " + topicMessage);
                            });

                    Disposable disposable2 = TicTacToeApplication.instance()
                            .getStompClient().topic("/topic/news").doOnError(throwable -> System.out.println("inx onError:" + throwable.getMessage())).subscribe(topicMessage -> {
                                Log.d(TAG, "inx Disposable news topicMessage: " + topicMessage);
                            });

                    Disposable disposable3 = TicTacToeApplication.instance()
                            .getStompClient().topic("/topic/greeting").doOnError(throwable -> System.out.println("inx onError:" + throwable.getMessage())).subscribe(topicMessage -> {
                                Log.d(TAG, "inx Disposable greeting topicMessage: " + topicMessage);
                            });

                    Disposable disposable4 = TicTacToeApplication.instance()
                            .getStompClient().topic("/topic/chat").doOnError(throwable -> System.out.println("inx onError:" + throwable.getMessage())).subscribe(topicMessage -> {
                                Log.d(TAG, "inx Disposable chat topicMessage: " + topicMessage);
                            });

                    Log.d(TAG, "inx stompClient.send");

                    stompClient.send("/topic/tictactoe").doOnError(throwable -> {
                        Log.d(TAG, "inx /topic/tictactoe message: " + throwable.getMessage());
                    }).subscribe(() -> {
                        Log.d(TAG, "inx /topic/tictactoe subscribe result: ");
                    });

                    stompClient.send("/topic/news", "break news!!!").doOnError(throwable -> {
                        Log.d(TAG, "inx /topic/news message: " + throwable.getMessage());
                    }).subscribe(() -> {
                        Log.d(TAG, "inx /topic/news subscribe result: ");
                    });

                    stompClient.send("/topic/greeting").doOnError(throwable -> {
                        Log.d(TAG, "inx /topic/greeting message: " + throwable.getMessage());
                    }).subscribe(() -> {
                        Log.d(TAG, "inx /topic/greeting subscribe result: ");
                    });

                    stompClient.send("/topic/chat", "{\"userId\": \"id_123\", \"message\": \"hello, i'm id_123\"}").doOnError(throwable -> {
                        Log.d(TAG, "inx /topic/chat message: " + throwable.getMessage());
                    }).subscribe(() -> {
                        Log.d(TAG, "inx /topic/chat subscribe result: ");
                    });
                } else {
                    Log.d(TAG, "inx stompClient.connect()");
                    try {
                        stompClient.connect();
                    } catch (Exception e) {
                        Log.e(TAG, "inx connect error:" + e.getMessage());
                    }
                }
            }
        });

        TicTacToeApplication.instance().getSubscriptions().addSubscription("/tictactoe/", topicMessage -> {
            Log.d("inx", "inx subscribe");
            System.out.println("inx payload: " + topicMessage.getPayload());
        });
    }

}
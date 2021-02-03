package com.yato2050.mybotbuilder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button hostButton;
    EditText userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hostButton = findViewById(R.id.hostButton);
        userToken = findViewById(R.id.userToken);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //fetching token from editable Text on app
                String token = userToken.getText().toString();
                //making bot online using token
                DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
                //command
                api.addMessageCreateListener(event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        event.getChannel().sendMessage("Pong!").thenAccept(message -> {
                            // Attach a listener directly to the message
                            message.addReactionAddListener(reactionEvent -> {
                                if (reactionEvent.getEmoji().equalsEmoji("👎")) {
                                    reactionEvent.deleteMessage();
                                }
                            }).removeAfter(30, TimeUnit.MINUTES);
                        });
                    }
                });
            }
        });
    }
}
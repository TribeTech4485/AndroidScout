package com.tribetech4485.androidscout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpenScreen extends AppCompatActivity {

    private Button standScoutButton;
    private Button pitScoutButton;

    private TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        // Initizalize the objects
        standScoutButton = (Button) findViewById(R.id.standScoutButton);
        pitScoutButton = (Button) findViewById(R.id.pitScoutButton);

        messageText = (TextView) findViewById(R.id.messageText);
        messageText.setText("");

        // Call this after initializing all the objects
        setClickListeners();
    }

    private void setClickListeners(){
        standScoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                messageText.setText("");
            }
        });

        pitScoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PitScout.class));
                messageText.setText("");
            }
        });
    }
}

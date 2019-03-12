package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

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

        String scoutNumberPath = "ScoutNumber";

        boolean exists = false;
        try {
            FileInputStream inputStream = openFileInput(scoutNumberPath);
            exists = true;
            inputStream.close();
        } catch(Exception ex) {
            exists = false;
        }

        if(!exists){
            String numberString = "";
            for (int i = 0; i < 6; i++) {
                Integer a = getRandomNumberInRange(0, 9);
                numberString += a.toString();
            }
            try {
                FileOutputStream fileWrite = openFileOutput(scoutNumberPath, Context.MODE_PRIVATE);
                fileWrite.write(numberString.getBytes());
                fileWrite.close();
            } catch (Exception ex) {
                messageText.setText("Write error: " + ex.getMessage());
            }
        }

       try {
            FileInputStream fileRead = openFileInput(scoutNumberPath);
            String numberString = "";
            int content;
            while ((content = fileRead.read()) != -1) {
                // convert to char and display it
                numberString += (char) content;
            }
            fileRead.close();

            messageText.setText("Scouting ID: " + numberString);

        } catch (Exception ex) {
            messageText.setText("Read error: " + ex.getMessage());
        }



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

    private static int getRandomNumberInRange ( int min, int max){
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min.");
        }
        Random s = new Random();
        return s.nextInt(9);

    }
}

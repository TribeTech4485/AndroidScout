package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private Button appendDataButton;
    private Button uploadConnectButton;

    private TextView messageText;

    private EditText teamNumberText;
    private EditText teamNameText;
    private EditText teamScaleRatingText;
    private EditText teamSwitchRatingText;
    private EditText teamOtherInfoText;

    private PackageManager m;

    private String teamDataPath = "TeamData.list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        teamNumberText = (EditText) findViewById(R.id.teamNumberText);
        teamNameText = (EditText) findViewById(R.id.teamNameText);
        teamScaleRatingText = (EditText) findViewById(R.id.teamScaleRatingText);
        teamSwitchRatingText = (EditText) findViewById(R.id.teamSwitchRatingText);
        teamOtherInfoText = (EditText) findViewById(R.id.teamOtherInfoText);

        messageText = (TextView) findViewById(R.id.messageText);

        appendDataButton = (Button) findViewById(R.id.appendToDataButton);
        uploadConnectButton = (Button) findViewById(R.id.uploadConnectButton);

        m = getPackageManager();

        uploadConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConnectAndSendActivity.class));
            }
        });

        appendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTeamData();
            }
        });
    }

    private void saveTeamData() {
        String output = "";

        output += "!start!\n";
        output += "Team Number:" + teamNumberText.getText().toString() + "\n";
        output += "Team Name:" + teamNameText.getText().toString() + "\n";
        output += "Team Scale Rating:" + teamScaleRatingText.getText().toString() + "\n";
        output += "Team Switch Rating:" + teamSwitchRatingText.getText().toString() + "\n";
        output += "Team Other Info:" + teamOtherInfoText.getText().toString() + "\n";

        FileOutputStream writeStream;
        try {
            writeStream = openFileOutput(teamDataPath, Context.MODE_APPEND);
            writeStream.write(output.getBytes());
            writeStream.close();
            clearInputs();
            setMessage(readDataFile(), false);
        } catch (Exception ex) {
            setMessage(ex.getMessage(), true);
        }
    }

    private void clearInputs() {
        teamNumberText.setText("");
        teamNameText.setText("");
        teamScaleRatingText.setText("");
        teamSwitchRatingText.setText("");
        teamOtherInfoText.setText("");
    }

    private void setMessage(String message, boolean error) {
        if (error) messageText.setTextColor(Color.RED);
        else messageText.setTextColor(Color.WHITE);

        messageText.setText(message);
    }

    private String readDataFile() {
        FileInputStream readStream;
        String readString = "";
        try {
            readStream = openFileInput(teamDataPath);

            int content;
            while ((content = readStream.read()) != -1) {
                // convert to char and display it
                readString += (char) content;
            }
        } catch (Exception ex) {
            setMessage(ex.getMessage(), true);
        }
        return readString;
    }
}

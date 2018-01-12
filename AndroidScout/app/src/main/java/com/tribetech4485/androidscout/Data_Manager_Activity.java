package com.tribetech4485.androidscout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;

public class Data_Manager_Activity extends AppCompatActivity {

    private Button deleteButton;
    private TextView fileText;

    private String teamDataPath = "TeamData.list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__manager_);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        fileText = (TextView) findViewById(R.id.fileText);

        fileText.setText(readDataFile());
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

    private void setMessage(String message, boolean error) {
        if (error) fileText.setTextColor(Color.RED);
        else fileText.setTextColor(Color.WHITE);
        fileText.setText(message);
    }
}

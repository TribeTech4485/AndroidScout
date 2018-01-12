package com.tribetech4485.androidscout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Data_Manager_Activity extends AppCompatActivity {

    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__manager_);

        deleteButton = (Button) findViewById(R.id.deleteButton);

    }
}

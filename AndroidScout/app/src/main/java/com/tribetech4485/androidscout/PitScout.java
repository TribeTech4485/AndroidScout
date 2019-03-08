package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PitScout extends AppCompatActivity {

    // Gesture Variables
//    private static final String DEBUG_TAG = "Gestures";
//    private GestureDetector mDetector;

//    private static final int SWIPE_MIN_DISTANCE = 120;
//    private static final int SWIPE_MAX_OFF_PATH = 250;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 600;


    // Interface Objects
    private Button appendDataButton;
    private Button uploadConnectButton;
    private Button dataManageButton;
    private TextView messageText;

    // Text boxes
    private EditText teamNumberText;
    private EditText teamNameText;
    private EditText teamPitInfoText;

    private EditText numHatchesText;
    private EditText numCargoText;
    private EditText numCyclesPerMatch;

    // Check boxes
    private CheckedTextView sandstormClimbDownCheckedTextView;
    private CheckedTextView sandstormAutoCheckedTextView;
    private CheckedTextView sandstormManualCheckedTextView;
    private CheckedTextView sandstormCameraCheckedTextView;
    private CheckedTextView hatchCollectionCheckedTextView;
    private CheckedTextView cargoCollectionCheckedTextView;
    private CheckedTextView liftCheckedTextView;

    private Spinner climbedHeightSpinner;
    private Spinner cargoLevelSpinner;
    private Spinner hatchLevelSpinner;
    private Spinner playTypeSpinner;
    private Spinner startingPositionSpinner;
    private Spinner driveTypeSpinner;
    private Spinner gearTypeSpinner;

    private String teamDataPath = "TeamData.list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pit_scout);

        // Setup the text boxes
        teamNumberText = (EditText) findViewById(R.id.teamNumberText);
        teamNameText = (EditText) findViewById(R.id.teamNameText);

        // Number text boxes
        numHatchesText = (EditText) findViewById(R.id.hatchesNumberPicker);
        numCargoText = (EditText) findViewById(R.id.cargoNumberPicker);
        numCyclesPerMatch = (EditText) findViewById(R.id.cyclesPerMatchNumberPicker);

        // Sandstorm CheckedTextViewes
        sandstormClimbDownCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormRunCheckedTextView);
        sandstormAutoCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormClimbDownCheckedTextView);
        sandstormManualCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormManualCheckedTextView);
        sandstormCameraCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormCameraCheckedTextView);
        hatchCollectionCheckedTextView = (CheckedTextView) findViewById(R.id.hatchCollectionCheckedTextView);
        cargoCollectionCheckedTextView = (CheckedTextView) findViewById(R.id.cargoCollectionCheckedTextView);
        liftCheckedTextView = (CheckedTextView) findViewById(R.id.liftCheckedTextView);

        // Spinner
        List<String> climbedHeightLevels = new ArrayList<String>();
        climbedHeightLevels.add("None");
        climbedHeightLevels.add("First");
        climbedHeightLevels.add("Second");
        climbedHeightLevels.add("Third");
        climbedHeightSpinner = (Spinner) findViewById(R.id.climbedHeightSpinner);
        ArrayAdapter<String> climbedHeightAdapter = new ArrayAdapter<String> (this, R.layout.spinner_layout, climbedHeightLevels);
        climbedHeightAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        climbedHeightSpinner.setAdapter(climbedHeightAdapter);

        List<String> cargoLevels = new ArrayList<String>();
        cargoLevels.add("None");
        cargoLevels.add("Bottom");
        cargoLevels.add("Middle");
        cargoLevels.add("Top");
        cargoLevelSpinner = (Spinner) findViewById(R.id.cargoLevelSpinner);
        ArrayAdapter<String> cargoLevelAdapter = new ArrayAdapter<String> (this, R.layout.spinner_layout, cargoLevels);
        cargoLevelAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        cargoLevelSpinner.setAdapter(cargoLevelAdapter);

        List<String> hatchLevels = new ArrayList<String>();
        hatchLevels.add("None");
        hatchLevels.add("Bottom");
        hatchLevels.add("Middle");
        hatchLevels.add("Top");
        hatchLevelSpinner = (Spinner) findViewById(R.id.hatchLevelSpinner);
        ArrayAdapter<String> hatchLevelAdapter = new ArrayAdapter<String> (this, R.layout.spinner_layout, hatchLevels);
        hatchLevelAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        hatchLevelSpinner.setAdapter(hatchLevelAdapter);

        List<String> playTypes = new ArrayList<String>();
        playTypes.add("No Data");
        playTypes.add("Cargo runner");
        playTypes.add("Hatch runner");
        playTypes.add("Cargo/Hatch runner");
        playTypes.add("Defense");
        playTypes.add("Ship specific runner");
        playTypes.add("Rocket specific runner");
        playTypeSpinner = (Spinner) findViewById(R.id.playTypeSpinner);
        ArrayAdapter<String> playTypeAdapter = new ArrayAdapter<String> (this, R.layout.spinner_layout, playTypes);
        playTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        playTypeSpinner.setAdapter(playTypeAdapter);

        List<String> startingPositions = new ArrayList<String>();
        startingPositions.add("No Data");
        startingPositions.add("None");
        startingPositions.add("Left");
        startingPositions.add("Middle");
        startingPositions.add("Right");
        startingPositionSpinner = (Spinner) findViewById(R.id.startingPositionSpinner);
        ArrayAdapter<String> startingPositionAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, startingPositions);
        startingPositionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        startingPositionSpinner.setAdapter(startingPositionAdapter);

        List<String> driveTypes = new ArrayList<String>();
        driveTypes.add("No Data");
        driveTypes.add("Mecanum");
        driveTypes.add("Tank");
        driveTypes.add("Tank using treads");
        driveTypes.add("Swerve");
        driveTypes.add("Omni-directional");
        driveTypes.add("Hybrid drive (Tank, Omni)");
        driveTypes.add("Hybrid drive (Tank, Mecanum)");
        driveTypes.add("Hybrid drive (Omni, Mecanum)");
        driveTypeSpinner = (Spinner) findViewById(R.id.driveTypeSpinner);
        ArrayAdapter<String> driveTypeAdapter =  new ArrayAdapter<String> (this, R.layout.spinner_layout, driveTypes);
        driveTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        driveTypeSpinner.setAdapter(driveTypeAdapter);

        List<String> gearTypes = new ArrayList<String>();
        gearTypes.add("No Data");
        gearTypes.add("Push");
        gearTypes.add("Speed");
        gearTypes.add("Precision");
        gearTypes.add("Climbing (???)");
        gearTypeSpinner = (Spinner) findViewById(R.id.gearTypeSpinner);
        ArrayAdapter<String> gearTypeAdapter = new ArrayAdapter<String> (this, R.layout.spinner_layout, driveTypes);
        gearTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        gearTypeSpinner.setAdapter(gearTypeAdapter);


        // Comment box at the bottom of the activity
        teamPitInfoText = (EditText) findViewById(R.id.teamOtherInfoText);

        teamPitInfoText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        //
        // Setup the Buttons at the bottom
        appendDataButton = (Button) findViewById(R.id.appendToDataButton);
        uploadConnectButton = (Button) findViewById(R.id.uploadConnectButton);
        dataManageButton = (Button) findViewById(R.id.dataManagerButton);

        setClickListeners();
    }

    private void setClickListeners() {
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

        dataManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Data_Manager_Activity.class));
            }
        });

        sandstormClimbDownCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormClimbDownCheckedTextView.setChecked(!sandstormClimbDownCheckedTextView.isChecked());
            }
        });
        sandstormAutoCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormAutoCheckedTextView.setChecked(!sandstormAutoCheckedTextView.isChecked());
            }
        });
        sandstormManualCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormManualCheckedTextView.setChecked(!sandstormManualCheckedTextView.isChecked());
            }
        });
        sandstormCameraCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormCameraCheckedTextView.setChecked(!sandstormCameraCheckedTextView.isChecked());
            }
        });
        hatchCollectionCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchCollectionCheckedTextView.setChecked(!hatchCollectionCheckedTextView.isChecked());
            }
        });
        cargoCollectionCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoCollectionCheckedTextView.setChecked(!cargoCollectionCheckedTextView.isChecked());
            }
        });
        liftCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liftCheckedTextView.setChecked(!liftCheckedTextView.isChecked());
            }
        });
    }
    private void saveTeamData() {
        String output = "";

        output += "!start!\n";
        output += "Pit Scout Comments:" + teamPitInfoText.getText().toString() + "\n";

        FileOutputStream writeStream;
        try {
            writeStream = openFileOutput(teamDataPath, Context.MODE_APPEND);
            writeStream.write(output.getBytes());
            writeStream.close();
            clearInputs();
            makeToast("Saved Team Data");
            //setMessage(readDataFile(), false);
        } catch (Exception ex) {
            setMessage(ex.getMessage(), true);
        }
    }

    private void clearInputs() {
        teamNumberText.setText("");
        teamNameText.setText("");
        teamPitInfoText.setText("");
    }

    private void makeToast(String toastedMessage) {
        Context context = getApplicationContext();
        CharSequence text = toastedMessage;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void setMessage(String message, boolean error) {
        if (error) messageText.setTextColor(Color.RED);
        else messageText.setTextColor(Color.WHITE);

        messageText.setText(message);
    }
}
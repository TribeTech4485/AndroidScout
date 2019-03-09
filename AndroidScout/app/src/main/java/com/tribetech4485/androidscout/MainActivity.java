/*
    TODO: Update for 2019 game once released
 */

package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    // Gesture Variables
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetector mDetector;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 600;
    //

    // Interface Objects
    private Button appendDataButton;
    private Button uploadConnectButton;
    private Button dataManageButton;

    private CheckedTextView sandstormRunCheckedTextView;
    private CheckedTextView sandstormAutoCheckedTextView;
    private CheckedTextView sandstormManualCheckedTextView;
    private CheckedTextView sandstormCameraCheckedTextView;
    private CheckedTextView sandstormClimbedDownCheckedTextView;
    private CheckedTextView connectionLossCheckedTextView;
    private CheckedTextView powerLossCheckedTextView;
    private CheckedTextView liftCheckedTextView;

    private NumberPicker spaceHatchesNumberPicker;
    private NumberPicker rocketCargoNumberPicker;
    private NumberPicker shipCargoNumberPicker;

    private Spinner driveTypeSpinner;
    private Spinner gearTypeSpinner;
    private Spinner hatchLevelSpinner;
    private Spinner cargoLevelSpinner;
    private Spinner playTypeSpinner;
    private Spinner startPositionSpinner;

    private TextView messageText;

    private Spinner climbedHeightSpinner;

    private EditText teamNumberText;
    private EditText teamNameText;
    private EditText teamOtherInfoText;
    private EditText matchNumberText;

    private ImageView imageView;
    //

    private PackageManager m;

    private String teamDataPath = "TeamData.list";
    private String skyrimQouteText = "";

    MediaPlayer imageEasterEggMp;
    MediaPlayer connectionMp;
    MediaPlayer otherInfo;
    MediaPlayer teamNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetector(this,this);
        // Set the gesture detector as the double tap
        // listener.
        //mDetector.setOnDoubleTapListener(this);


        // Setup the interface objects
        // Setup the DriveBase selection drop-down list
        List<String> driveBaseTypes = new ArrayList<String>();
        driveBaseTypes.add("No Data");
        driveBaseTypes.add("Mecanum");
        driveBaseTypes.add("Tank");
        driveBaseTypes.add("Omni-directional");
        driveBaseTypes.add("Swerve");
        driveTypeSpinner = (Spinner) findViewById(R.id.driveTypeSpinner);
        ArrayAdapter<String> driveBaseTypesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, driveBaseTypes);
        driveBaseTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //driveBaseTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driveTypeSpinner.setAdapter(driveBaseTypesAdapter);
        ////

        // Setup the interface objects
        // Setup the gearType selection drop-down list
        List<String> gearType = new ArrayList<String>();
        gearType.add("No Data");
        gearType.add("Push");
        gearType.add("Speed");
        gearType.add("Precision");
        gearType.add("Climb????");
        gearTypeSpinner = (Spinner) findViewById(R.id.gearTypeSpinner);
        ArrayAdapter<String> gearTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, gearType);
        gearTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //gearTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gearTypeSpinner.setAdapter(gearTypeAdapter);
        ////

        // Setup the HatchLevelValue selection drop-down list
        List<String> hatchLevelValue = new ArrayList<String>();
        hatchLevelValue.add("None");
        hatchLevelValue.add("First");
        hatchLevelValue.add("Second");
        hatchLevelValue.add("Third");
        hatchLevelSpinner = (Spinner) findViewById(R.id.hatchLevelSpinner);
        ArrayAdapter<String> hatchLevelValueAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, hatchLevelValue);
        hatchLevelValueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //hatchLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hatchLevelSpinner.setAdapter(hatchLevelValueAdapter);

        // Setup the CargoLevelValue selection drop-down list
        List<String> cargoLevelValue = new ArrayList<String>();
        cargoLevelValue.add("None");
        cargoLevelValue.add("First");
        cargoLevelValue.add("Second");
        cargoLevelValue.add("Third");
        cargoLevelSpinner = (Spinner) findViewById(R.id.cargoLevelSpinner);
        ArrayAdapter<String> cargoLevelValueAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, cargoLevelValue);
        cargoLevelValueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //cargoLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cargoLevelSpinner.setAdapter(cargoLevelValueAdapter);

        // Setup the StartingPosition selection drop-down list
        List<String> startingPosition = new ArrayList<String>();
        startingPosition.add("No Data");
        startingPosition.add("Left");
        startingPosition.add("Middle");
        startingPosition.add("Right");
        startPositionSpinner = (Spinner) findViewById(R.id.startingPositionSpinner);
        ArrayAdapter<String> startingPositionAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, startingPosition);
        startingPositionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //startingPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startPositionSpinner.setAdapter(startingPositionAdapter);

        // Setup the playType selection drop-down list
        List<String> playType = new ArrayList<String>();
        playType.add("No Data");
        playType.add("Cargo/Hatch (Rocket and Ship) Runner");
        playType.add("Defense");
        playType.add("Rocket Specific Runner");
        playType.add("Ship Specific Runner");
        playType.add("Other(Explain in Comments)");
        playTypeSpinner = (Spinner) findViewById(R.id.playTypeSpinner);
        ArrayAdapter<String> playTypeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, playType);
        playTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //playTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playTypeSpinner.setAdapter(playTypeAdapter);

        // Setup the interface objects
        // Setup the ClimbedValue selection drop-down list
        List<String> climbedHeightValue = new ArrayList<String>();
        climbedHeightValue.add("None");
        climbedHeightValue.add("First");
        climbedHeightValue.add("Second");
        climbedHeightValue.add("Third");
        climbedHeightSpinner = (Spinner) findViewById(R.id.climbedHeightSpinner);
        ArrayAdapter<String> climbedHeightValueAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, climbedHeightValue);
        climbedHeightValueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //climbedHeightValueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        climbedHeightSpinner.setAdapter(climbedHeightValueAdapter);
        ////




        // Setup the text boxes
        teamNumberText = (EditText) findViewById(R.id.teamNumberText);
        teamNameText = (EditText) findViewById(R.id.teamNameText);
        teamOtherInfoText = (EditText) findViewById(R.id.teamOtherInfoText);
        matchNumberText = (EditText) findViewById(R.id.matchNumberText);

        teamOtherInfoText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        //
        // Setup the Buttons at the bottom
        appendDataButton = (Button) findViewById(R.id.appendToDataButton);
        uploadConnectButton = (Button) findViewById(R.id.uploadConnectButton);
        dataManageButton = (Button) findViewById(R.id.dataManagerButton);
        //

        // Auto and Connection loss objects
        sandstormRunCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormRunCheckedTextView);
        connectionLossCheckedTextView = (CheckedTextView) findViewById(R.id.connectionLossCheckedTextView);
        powerLossCheckedTextView = (CheckedTextView) findViewById(R.id.powerLossCheckedTextView);
        //

        // Game specific objects - Updated for every game (currently setup for FIRST Power-up)
        sandstormAutoCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormClimbDownCheckedTextView);
        sandstormManualCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormManualCheckedTextView);
        sandstormCameraCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormCameraCheckedTextView);
        sandstormClimbedDownCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormClimbedDownCheckedTextView);
        liftCheckedTextView = (CheckedTextView) findViewById(R.id.liftCheckedTextView);

        spaceHatchesNumberPicker = (NumberPicker) findViewById(R.id.spaceHatchesNumberPicker);
        rocketCargoNumberPicker = (NumberPicker) findViewById(R.id.cargoNumberPicker);
        shipCargoNumberPicker = (NumberPicker) findViewById(R.id.shipCargoNumberPicker);
        spaceHatchesNumberPicker.setMax(20);
        rocketCargoNumberPicker.setMax(12);
        shipCargoNumberPicker.setMax(8);
        //

        imageView = (ImageView) findViewById(R.id.imageView);

        messageText = (TextView) findViewById(R.id.messageTextView);

        m = getPackageManager();

        zeroNumberPickers();

        setClickListeners();

        easterEggs();

        MediaPlayer mp = MediaPlayer.create(this, R.raw.get_ready);
        mp.start();

        imageEasterEggMp = MediaPlayer.create(this, R.raw.its_monsoon_season1);
        otherInfo = MediaPlayer.create(this, R.raw.bonus_round1);
        connectionMp = MediaPlayer.create(this, R.raw.what_are_you_doing);
        teamNumber = MediaPlayer.create(this, R.raw.dooh);
    }

    private void setClickListeners(){
        uploadConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConnectAndSendActivity.class));
            }
        });

        appendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean power = powerLossCheckedTextView.isChecked();
                boolean connection = connectionLossCheckedTextView.isChecked();
                Integer two = spaceHatchesNumberPicker.getValue();
                Integer four = rocketCargoNumberPicker.getValue();
                Integer seven = shipCargoNumberPicker.getValue();
                String team = teamNumberText.getText().toString();
                String skyrimName = teamNameText.getText().toString();

                // Check for Easter egg conditions
                if(team.matches("4485")) {
                    if (two.equals(2)) {
                        if (four.equals(4)) {
                            if (seven.equals(7)) {
                                if (power == true) {
                                    if (connection == false) {
                                        makeToast("You are the true User, welcome.");

                                    }
                                }
                            }
                        }
                    }
                }else if(skyrimName.contentEquals("I used to be an adventurer like you")) {
                        skyrimQouteText = "until I took and arrow to the knee";
                }

                if(team.isEmpty()){
                    makeToast("You must have a Team Number to Save Data!");
                    teamNumber.start();
                } else saveTeamData();
            }
        });

        dataManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Data_Manager_Activity.class));
            }
        });

        sandstormRunCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormRunCheckedTextView.setChecked(!sandstormRunCheckedTextView.isChecked());
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

        sandstormClimbedDownCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sandstormClimbedDownCheckedTextView.setChecked(!sandstormClimbedDownCheckedTextView.isChecked());
            }
        });

        liftCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liftCheckedTextView.setChecked(!liftCheckedTextView.isChecked());
            }
        });

        powerLossCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerLossCheckedTextView.setChecked(!powerLossCheckedTextView.isChecked());
                if (powerLossCheckedTextView.isChecked()) connectionLossCheckedTextView.setChecked(true);
            }
        });
        connectionLossCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean power = powerLossCheckedTextView.isChecked();

                if(power==true){
                        connectionMp.start();
                } else {
                    connectionLossCheckedTextView.setChecked(!connectionLossCheckedTextView.isChecked());
                }
            }
        });

        teamOtherInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherInfo.start();
            }
        });
    }

    private void easterEggs(){

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageEasterEggMp.start();
            }
        });
    }

    private void zeroNumberPickers(){
        spaceHatchesNumberPicker.setValue(0);
        rocketCargoNumberPicker.setValue(0);
        shipCargoNumberPicker.setValue(0);
    }


    private void saveTeamData() {
        String output = "";

        output += "!start!\n";
        output += "Team Number:" + teamNumberText.getText().toString() + "\n";
        output += "Team Name:" + teamNameText.getText().toString() + "\n";
        output += "Match Number:" + matchNumberText.getText().toString() + "\n";
        output += "Sandstorm Run:" + sandstormRunCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Auto:" + sandstormAutoCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Camera:" + sandstormCameraCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Manual:" + sandstormManualCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Climb Down:" + sandstormClimbedDownCheckedTextView.isChecked() + "\n";
        output += "Hatches:" + spaceHatchesNumberPicker.getValue() + "\n";
        output += "Rocket Cargo:" + rocketCargoNumberPicker.getValue() + "\n";
        output += "Ship Cargo:" + shipCargoNumberPicker.getValue() + "\n";
        output += "Height Climbed:" + climbedHeightSpinner.getSelectedItem() +"\n";
        output += "Hatch Level:" + hatchLevelSpinner.getSelectedItem() +"\n";
        output += "Cargo Level:" + cargoLevelSpinner.getSelectedItem() +"\n";
        output += "Play Type:" + playTypeSpinner.getSelectedItem() +"\n";
        output += "Start Position:" + startPositionSpinner.getSelectedItem() +"\n";
        output += "Has Lift:" + liftCheckedTextView.isChecked() + "\n";
        output += "Drive Type:" + driveTypeSpinner.getSelectedItem() + "\n";
        output += "Gear Type:" + gearTypeSpinner.getSelectedItem() + "\n";
        output += "Team Other Info:" + teamOtherInfoText.getText().toString() + "\n";
        //easter egg, line will be empty if parameters are not met
        output += skyrimQouteText + "\n";
        output += "!end!\n";


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
        sandstormRunCheckedTextView.setChecked(false);
        sandstormAutoCheckedTextView.setChecked(false);
        sandstormManualCheckedTextView.setChecked(false);
        sandstormCameraCheckedTextView.setChecked(false);
        sandstormClimbedDownCheckedTextView.setChecked(false);
        zeroNumberPickers();
        powerLossCheckedTextView.setChecked(false);
        connectionLossCheckedTextView.setChecked(false);
        driveTypeSpinner.setSelection(0);
        gearTypeSpinner.setSelection(0);
        climbedHeightSpinner.setSelection(0);
        cargoLevelSpinner.setSelection(0);
        hatchLevelSpinner.setSelection(0);
        playTypeSpinner.setSelection(0);
        startPositionSpinner.setSelection(0);
        teamOtherInfoText.setText("");
        matchNumberText.setText("");
        liftCheckedTextView.setChecked(false);
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

    // Show notification
    private void makeToast(String toastedMessage) {
        Context context = getApplicationContext();
        CharSequence text = toastedMessage;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    // Gestures -----
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);

        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());

        try {
            if (Math.abs(event1.getY() - event2.getY()) > SWIPE_MAX_OFF_PATH) return false;
            // Right to left swipe
            if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) onLeftSwipe();
            // Left to right swipe
            else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) onRightSwipe();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }


    // Functions for gestures
    private void onLeftSwipe() {
        startActivity(new Intent(getApplicationContext(), ConnectAndSendActivity.class));
    }
    private void onRightSwipe() {
        startActivity(new Intent(getApplicationContext(), Data_Manager_Activity.class));
    }
}

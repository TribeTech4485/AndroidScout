/*
    TODO: Update for 2019 game once released
 */

package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
    private CheckedTextView connectionLossCheckedTextView;
    private CheckedTextView powerLossCheckedTextView;

    private NumberPicker spaceHatchesNumberPicker;
    private NumberPicker rocketCargoNumberPicker;
    private NumberPicker shipCargoNumberPicker;

    private Spinner driveTypeSpinner;

    private TextView messageText;

    private Spinner climbedHeightSpinner;

    private EditText teamNumberText;
    private EditText teamNameText;
    private EditText teamOtherInfoText;
    //

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
        driveBaseTypes.add(" ");
        driveBaseTypes.add("Push");
        driveBaseTypes.add("Speed");
        driveBaseTypes.add("Shifting");
        driveBaseTypes.add("Mecanum");
        driveBaseTypes.add("Tank");
        driveBaseTypes.add("Omni-dircetional");
        driveTypeSpinner = (Spinner) findViewById(R.id.driveTypeSpinner);
        ArrayAdapter<String> driveBaseTypesAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, driveBaseTypes);
        driveBaseTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_custom);
        //driveBaseTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driveTypeSpinner.setAdapter(driveBaseTypesAdapter);
        ////

        // Setup the interface objects
        // Setup the ClimbedValue selection drop-down list
        List<String> climbedHeightValue = new ArrayList<String>();
        climbedHeightValue.add("0");
        climbedHeightValue.add("1");
        climbedHeightValue.add("2");
        climbedHeightValue.add("3");
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
        sandstormAutoCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormAutoCheckedTextView);
        sandstormManualCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormManualCheckedTextView);
        sandstormCameraCheckedTextView = (CheckedTextView) findViewById(R.id.sandstormCameraCheckedTextView);

        spaceHatchesNumberPicker = (NumberPicker) findViewById(R.id.spaceHatchesNumberPicker);
        rocketCargoNumberPicker = (NumberPicker) findViewById(R.id.rocketCargoNumberPicker);
        shipCargoNumberPicker = (NumberPicker) findViewById(R.id.shipCargoNumberPicker);
        //

        messageText = (TextView) findViewById(R.id.messageTextView);

        m = getPackageManager();

        setClickListeners();
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
                saveTeamData();
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
                connectionLossCheckedTextView.setChecked(!connectionLossCheckedTextView.isChecked());
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
        output += "Sandstorm Run:" + sandstormRunCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Auto:" + sandstormAutoCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Camera:" + sandstormCameraCheckedTextView.isChecked() + "\n";
        output += "Sandstorm Manual:" + sandstormManualCheckedTextView.isChecked() + "\n";
        output += "Hatches:" + spaceHatchesNumberPicker.getValue() + "\n";
        output += "Rocket Cargo:" + rocketCargoNumberPicker.getValue() + "\n";
        output += "Ship Cargo:" + shipCargoNumberPicker.getValue() + "\n";
        output += "Drive Type:" + driveTypeSpinner.getSelectedItem() + "\n";
        output += "Team Other Info:" + teamOtherInfoText.getText().toString() + "\n";
        output += "Height Climbed:" + climbedHeightSpinner.getSelectedItem() +"\n";
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
        zeroNumberPickers();
        powerLossCheckedTextView.setChecked(false);
        connectionLossCheckedTextView.setChecked(false);
        driveTypeSpinner.setSelection(0);
        climbedHeightSpinner.setSelection(0);
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

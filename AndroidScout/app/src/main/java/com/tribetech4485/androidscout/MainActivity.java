package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.travijuu.numberpicker.library.NumberPicker;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetector mDetector;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 600;

    private Button appendDataButton;
    private Button uploadConnectButton;
    private Button dataManageButton;

    private CheckedTextView autoRunCheckedTextView;
    private CheckedTextView autoSwitchCheckedTextView;
    private CheckedTextView powerUpBoostCheckedTextView;
    private CheckedTextView powerUpForceCheckedTextView;
    private CheckedTextView powerUpLevitateCheckedTextView;
    private CheckedTextView climbedCheckedTextView;
    private CheckedTextView connectionLossCheckedTextView;

    private NumberPicker switchNumberOfCratesNumberPicker;
    private NumberPicker scaleNumberOfCratesNumberPicker;
    private NumberPicker numberOfPowerUpCratesNumberPicker;
    private NumberPicker numberOfCratesCollectedNumberPicker;

    private Spinner driveTypeSpinner;

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


        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetector(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);


        teamNumberText = (EditText) findViewById(R.id.teamNumberText);
        teamNameText = (EditText) findViewById(R.id.teamNameText);
        teamOtherInfoText = (EditText) findViewById(R.id.teamOtherInfoText);

        appendDataButton = (Button) findViewById(R.id.appendToDataButton);
        uploadConnectButton = (Button) findViewById(R.id.uploadConnectButton);
        dataManageButton = (Button) findViewById(R.id.dataManagerButton);

        autoRunCheckedTextView = (CheckedTextView) findViewById(R.id.autoRunCheckedTextView);
        autoSwitchCheckedTextView = (CheckedTextView) findViewById(R.id.autoSwitchCheckedTextView);
        powerUpBoostCheckedTextView = (CheckedTextView) findViewById(R.id.powerUpBoostCheckedTextView);
        powerUpForceCheckedTextView = (CheckedTextView) findViewById(R.id.powerUpForceCheckedTextView);
        powerUpLevitateCheckedTextView = (CheckedTextView) findViewById(R.id.powerUpLevitateCheckedTextView);
        climbedCheckedTextView = (CheckedTextView) findViewById(R.id.climbedCheckedTextView);
        connectionLossCheckedTextView = (CheckedTextView) findViewById(R.id.connectionLossCheckedTextView);

        driveTypeSpinner = (Spinner) findViewById(R.id.driveTypeSpinner);

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

        autoRunCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRunCheckedTextView.setChecked(!autoRunCheckedTextView.isChecked());
            }
        });

        autoSwitchCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSwitchCheckedTextView.setChecked(!autoSwitchCheckedTextView.isChecked());
            }
        });

        powerUpBoostCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerUpBoostCheckedTextView.setChecked(!powerUpBoostCheckedTextView.isChecked());
            }
        });

        powerUpForceCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerUpForceCheckedTextView.setChecked(!powerUpForceCheckedTextView.isChecked());
            }
        });

        powerUpLevitateCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerUpLevitateCheckedTextView.setChecked(!powerUpLevitateCheckedTextView.isChecked());
            }
        });

        climbedCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                climbedCheckedTextView.setChecked(!climbedCheckedTextView.isChecked());
            }
        });
        connectionLossCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionLossCheckedTextView.setChecked(!connectionLossCheckedTextView.isChecked());
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

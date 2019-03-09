package com.tribetech4485.androidscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Data_Manager_Activity extends AppCompatActivity {

    private Button moveToExtButton;
    private Button deleteButton;
    private TextView fileText;

    private String teamDataPath = "TeamData.list";

    MediaPlayer deleteMp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__manager_);

        moveToExtButton = (Button) findViewById(R.id.moveToExtButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        fileText = (TextView) findViewById(R.id.fileText);

        fileText.setText(readDataFile());

        moveToExtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExternalStorageWriteable()) {
                    completeDialog("External Storage is un-writable!", "External Storage");
                    return;
                }
                if (moveToExternalStorage()) {
                    completeDialog("Successfully wrote file to external storage", "Moving Data");
                } else {
                    completeDialog("Could not write file to external storage", "Moving Data");
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
                //deleteFile();
                setMessage(readDataFile(), false);
            }
        });

        deleteMp = MediaPlayer.create(this, R.raw.try_again);

        listeners();
    }

    private String readDataFile() {
        if (!checkFileExists()) return "";
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

    private void listeners(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMp.start();
                deleteFile();
            }
        });
    }

    private void setMessage(String message, boolean error) {
        if (error) fileText.setTextColor(Color.RED);
        else fileText.setTextColor(Color.WHITE);
        fileText.setText(message);
    }

    private void deleteFile() {
        if (!checkFileExists()) return;
        File dir = getFilesDir();
        File file = new File(dir, teamDataPath);
        boolean deleted = file.delete();
        if (deleted) {
            completeDialog("Successfully Deleted", "Deleting Data");
        } else {
            completeDialog("Failed to Delete", "Deleting Data");
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Delete?").setTitle("Deleting Data");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int w) {
                dialog.dismiss();
                deleteFile();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int w) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void completeDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int w) {
                dialog.dismiss();
            }
        });
    }

    private boolean checkFileExists() {
        try {
            File dir = getFilesDir();
            File file = new File(dir, teamDataPath);
            if (file.exists()) return true;
        } catch (Exception ex) {
        }
        return false;
    }

    // Moving data to external storage
    private boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean moveToExternalStorage() {
        String root = Environment.getExternalStorageDirectory().toString();
        File extFile = new File(root + "/" + teamDataPath);
        FileOutputStream writeStream;
        try {
            writeStream = openFileOutput(extFile.getName(), Context.MODE_PRIVATE);
            writeStream.write(readDataFile().getBytes());
            writeStream.close();
        } catch (Exception ex) {
            setMessage(ex.getMessage(), true);
            return false;
        }

        if (extFile.exists()) return true;
        else return false;
    }

    private boolean checkExtFileExists() {
        String root = Environment.getExternalStorageDirectory().toString();
        File extFile = new File(root + teamDataPath);
        return extFile.exists();
    }
}
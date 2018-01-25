package com.tribetech4485.androidscout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class ConnectAndSendActivity extends AppCompatActivity {
    private String teamDataPath = "TeamData.list";

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    private TextView logText;

    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_and_send);

        logText = (TextView) findViewById(R.id.logText);

        connected = connectToDevice();
        showConnected();
    }

    private void showConnected() {
        if (connected) setLogMessage("Device Connected!", false);
        else setLogMessage("Device Disconnected", false);
    }

    private boolean connectToDevice() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                //(device.getName().equals("4485-M8") || device.getAddress().equals("00:1A:7D:DA:71:13"))
                if(device.getName().equals("raspberrypi") || device.getAddress().equals("B8:27:EB:F9:A0:15")) //Note, you will need to change this to match the name and address of your device
                {
                    setLogMessage("Found Specified Device: " + device.getAddress().toString(), false);
                    Log.e("4485-M8",device.getName());
                    mmDevice = device;
                    return true;
                }
            }
        }
        setLogMessage("Could Not Find Specified Device.", false);
        return false;
    }

    private boolean sendBtMsg(String msg2send){
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID
        try {
            if (mmSocket != null) if (mmSocket.isConnected()) mmSocket.close();

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()){
                mmSocket.connect();
            }

            String msg = msg2send;
            //msg += "\n";
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

            msg = "#";
            mmOutputStream.write(msg.getBytes());

            mmSocket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            setLogMessage("Error Sending: " + e.getMessage(), true);
            return false;
        }
        return true;
    }

    private void setLogMessage(String message, boolean error) {
        if (error) logText.setTextColor(Color.RED);
        else logText.setTextColor(Color.WHITE);

        logText.setText(message);
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
            setLogMessage(ex.getMessage(), true);
        }
        return readString;
    }

    // Send data with button press
    public void sendDataBtn(View view) {
        String dataFileContents = readDataFile();
        if (dataFileContents == "") setLogMessage("File Contents Empty! Collect some team data, then send it.", false);
        else if (!connected) setLogMessage("Not connected to server!", true);
        else if (connected) {
            for (int i = 0; i < 3; i++) {
                if (sendBtMsg(dataFileContents)) break;
            }
        }
    }

    public void connectBtn(View view) {
        if (connected) setLogMessage("Already Connected to Device.", false);
        else {
            connected = connectToDevice();
            showConnected();
        }
    }
}
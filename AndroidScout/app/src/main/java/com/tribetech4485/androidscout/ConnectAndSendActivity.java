package com.tribetech4485.androidscout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

// TODO: rewrite bluetooth code

public class ConnectAndSendActivity extends AppCompatActivity {
    private String teamDataPath = "TeamData.list";

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    private TextView logText;
    private EditText deviceNameText;
    private EditText deviceAddrText;

    boolean connected = false;


    // Saved MAC addresses and names
    String deviceName = "";
    String deviceAddress = "";

    String addressStorePath = "DEVMACADDRESS";
    String nameStorePath = "DEVNAME";

    // Data rcv
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String rcvText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_and_send);

        logText = (TextView) findViewById(R.id.logText);
        deviceNameText = (EditText) findViewById(R.id.nameText);
        deviceAddrText = (EditText) findViewById(R.id.addressText);

        updateAddrDataContent();

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
                if(device.getName().equals(deviceName) || device.getAddress().equals(deviceAddress)) //Note, you will need to change this to match the name and address of your device
                {
                    setLogMessage("Found Specified Device: " + device.getAddress().toString(), false);
                    Log.e("4485-M8",device.getName());
                    mmDevice = device;
                    logText.setText("Testing Connection....");
                    if (sendBtMsg("!")) return true;
                    return false;

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

    private void beginListeningForData() {

        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            rcvText = data;
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    private void setLogMessage(String message, boolean error) {
        if (error) logText.setTextColor(Color.RED);
        else logText.setTextColor(Color.WHITE);

        logText.setText(message);
    }

    // Show notification
    private void makeToast(String toastedMessage) {
        Context context = getApplicationContext();
        CharSequence text = toastedMessage;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean checkFileExists(String path) {
        try {
            File dir = getFilesDir();
            File file = new File(dir, path);
            if (file.exists()) return true;
        } catch (Exception ex) {
        }
        return false;
    }

    private String readDataFile(String path) {
        if (!checkFileExists(path)) return "";
        FileInputStream readStream;
        String readString = "";
        try {
            readStream = openFileInput(path);

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

    private void saveData(String path, String content) {
        FileOutputStream writeStream;
        try {
            writeStream = openFileOutput(path, Context.MODE_PRIVATE);
            writeStream.write(content.getBytes());
            writeStream.close();
            //setMessage(readDataFile(), false);

            makeToast("Updated Device!");
        } catch (Exception ex) {
        }
    }

    private void updateAddrDataContent() {
        if (checkFileExists(addressStorePath)) deviceAddress = readDataFile(addressStorePath);
        else deviceAddress = "AA:AA:AA:AA:AA:AA";

        if (checkFileExists(nameStorePath)) deviceName = readDataFile(nameStorePath);
        else deviceName = "Device Name";

        deviceAddrText.setText(deviceAddress);
        deviceNameText.setText(deviceName);
    }

    // Send data with button press
    public void sendDataBtn(View view) {
        String dataFileContents = readDataFile(teamDataPath);
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

    public void saveAddrDataBtn(View view) {
        deviceName = deviceNameText.getText().toString();
        deviceAddress = deviceAddrText.getText().toString();
        saveData(nameStorePath, deviceName);
        saveData(addressStorePath, deviceAddress);

        updateAddrDataContent();
    }
}
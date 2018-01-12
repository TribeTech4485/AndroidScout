package com.tribetech4485.androidscout;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.data;

public class ConnectAndSendActivity extends AppCompatActivity {
    private TextView statusText;
    private Button bluetoothOnButton;
    private Button bluetoothOffButton;
    private Button discoverableButton;
    private Button discoverButton;

    private static final int REQUEST_ENABLE_BT=1;

    ProgressDialog progressDialog;
    BluetoothAdapter bluetoothAdapter;

    public ArrayList<BluetoothDevice> devices = new ArrayList<>();
    public DeviceListAdapter deviceListAdapter;
    private ListView listViewDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_and_send);

        statusText = (TextView) findViewById(R.id.statusText);
        bluetoothOnButton = (Button) findViewById(R.id.bluetoothOnButton);
        bluetoothOffButton = (Button) findViewById(R.id.bluetoothOffButton);
        discoverableButton = (Button) findViewById(R.id.discoverableButton);
        discoverButton = (Button) findViewById(R.id.discoverButton);
        listViewDevices = (ListView) findViewById(R.id.listView);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Scanning...");
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (bluetoothAdapter == null) {
            showUnsupported();
        } else {

            bluetoothOnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
                }
            });
            bluetoothOffButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothAdapter.disable();
                    checkBtEnabled();
                    showDisabled();
                }
            });
            discoverableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        makeDiscoverable();
                        checkBtEnabled();
                    } catch (Exception ex) {
                        statusText.setTextColor(Color.RED);
                        statusText.setText(ex.getMessage());
                    }
                }
            });
            discoverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    checkBTPermissions();

                    bluetoothAdapter.startDiscovery();
                    IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(broadcastReceiver, discoverDevicesIntent);
                }
            });

        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
                deviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, devices);
                listViewDevices.setAdapter(deviceListAdapter);
                statusText.setText(statusText.getText() + "\n" + Integer.toString(devices.size()));
            }
        }
    };

    private void checkBTPermissions() {
        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionsCheck != 0) {
                this.requestPermissions(String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            }
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_ENABLE_BT) {
            checkBtEnabled();
        }
    }

    private void checkBtEnabled() {
        if (bluetoothAdapter.isEnabled()) {
            showEnabled();
        } else {
            showDisabled();
        }
    }
    private void showEnabled() {
        statusText.setText("Bluetooth is On");
        statusText.setTextColor(Color.BLUE);
        bluetoothOffButton.setEnabled(true);
        bluetoothOnButton.setEnabled(false);
    }
    private void showDisabled() {
        statusText.setText("Bluetooth is Off");
        statusText.setTextColor(Color.RED);
        bluetoothOffButton.setEnabled(false);
        bluetoothOnButton.setEnabled(true);
    }
    private void showUnsupported() {
        statusText.setText("Bluetooth is unsupported on this device.");
        statusText.setTextColor(Color.RED);
    }

    private void makeDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }
}

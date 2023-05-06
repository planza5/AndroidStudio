package com.plm.btp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 111;
    private static final String ESP32BT = "PabloBT";
    private static String TAG = "PABLO2";
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            Log.d(TAG, "No Bluetooth");
        } else {
            Log.d(TAG, "Bluetooth existe");
            Log.d(TAG, "Bt enabled=" + bluetoothAdapter.isEnabled());

            startScan();

        }

    }

    private void startScan() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Problems BLUETOOTH_SCAN");
            this.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Problems permission ACCESS_FINE_LOCATION");
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        Log.d(TAG, "Scanning...");
        bluetoothAdapter.getBluetoothLeScanner().startScan(scan);
    }

    private boolean stopScan = false;

    private ScanCallback scan = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (!stopScan) {
                String deviceName = result.getScanRecord().getDeviceName();

                if (deviceName != null && deviceName.equals("PabloBT")) {
                    Log.d(TAG, "PabloBT found...tryng connect in 3 sec");
                    stopScan = true;
                    stopScan();

                    connectDevice(result.getDevice());
                }
            }
        }
    };


    private void connectDevice(BluetoothDevice device) {
        Log.d(TAG, "Connecting device....");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Problems permission BLUETOOTH_CONNECT");
            this.requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
        }

        try {
            BluetoothSocket s = device.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());
            s.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BluetoothGatt server = device.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                Log.d(TAG, "onConnectionStateChange...");

                if (BluetoothProfile.STATE_CONNECTED == newState) {
                    Log.d(TAG, "CONNECTED!!!");
                } else {
                    Log.d(TAG, "CONNECTION FAILED!!!");
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void stopScan(){
        Log.d(TAG,"Stopping...");
        stopScan=true;
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scan);
    }

    @Override
    protected void onDestroy() {
        stopScan();
        super.onDestroy();
    }
}
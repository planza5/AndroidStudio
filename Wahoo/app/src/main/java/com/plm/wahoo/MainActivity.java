package com.plm.wahoo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements PermisionsCallback {

    PermisionsManager pm = new PermisionsManager(this);
    private boolean scanPermission;
    private boolean connectPermission;
    private boolean locationPermission;
    private Button button;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    String alias = device.getAlias();
                    String name = device.getName();
                    Log.d("PABLO", alias + ", " + name);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        button = findViewById(R.id.connectButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationPermission && scanPermission && connectPermission) {
                    BluetoothDevice btDevice = getDevice("Wahoo CADENCE EAAD");
                    readBt(btDevice, UUID.randomUUID());
                }
            }
        });

        pm.requestPermissions();
    }

    public void readBt(BluetoothDevice device, UUID MY_UUID) {
        BluetoothSocket socket = null;
        InputStream inputStream = null;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                bytes = inputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d("PABLO",readMessage);
                // Procesar el mensaje leído
            }
        } catch (IOException connectException) {
            connectException.printStackTrace();
            // Manejar la excepción
        } finally {
            // Cerrar los recursos
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace(); }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException closeException) { /* ... */ }
            }
        }
    }


    private BluetoothDevice getDevice(String deviceName) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals(deviceName)){
                    return device;
                }
            }
        }

        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        pm.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void scan(){
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();

        if(adapter!=null && ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN)== PackageManager.PERMISSION_GRANTED){
            adapter.startDiscovery();
        }else{

        }
    }

    @Override
    public synchronized void scanBtResult(boolean result) {
        scanPermission = result;
    }

    @Override
    public synchronized void locationBtResult(boolean result) {
        locationPermission = result;
    }

    @Override
    public synchronized void connectBtResult(boolean result) {
        connectPermission = result;
    }
}
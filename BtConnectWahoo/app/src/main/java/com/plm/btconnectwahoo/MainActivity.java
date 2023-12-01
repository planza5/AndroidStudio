package com.plm.btconnectwahoo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void readBt(BluetoothDevice device, UUID MY_UUID) {
        BluetoothSocket socket = null;
        InputStream inputStream = null;

        try {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
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
                // Procesar el mensaje leído
            }
        } catch (IOException connectException) {
            // Manejar la excepción
        }  finally {
            // Cerrar los recursos
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException closeException) { /* ... */ }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException closeException) { /* ... */ }
            }
        }
    }

}
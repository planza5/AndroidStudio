package com.plm.wisignal;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private List<String> wifiNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Solicita permiso si es necesario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }

        // Crea ArrayAdapter y configura ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiNames);
        ListView lvWifiSignals = findViewById(R.id.lvWifiSignals);
        lvWifiSignals.setAdapter(adapter);

        // Registra el BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        // Inicia el escaneo
        iniciarEscaneoWifi();
    }

    // BroadcastReceiver para recibir resultados de escaneo
    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                actualizarListaWifi();
            }
        }
    };

    // Inicia el escaneo Wi-Fi
    private void iniciarEscaneoWifi() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            wifiManager.startScan();
        }
    }

    // Actualiza la lista de nombres Wi-Fi y notifica al adaptador
    private void actualizarListaWifi() {
        List<ScanResult> listaWifi = wifiManager.getScanResults();

        wifiNames.clear();
        for (ScanResult sr : listaWifi) {
            wifiNames.add(sr.SSID + " - " + sr.level);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver); // No olvides anular el registro del BroadcastReceiver
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarEscaneoWifi();
        }
    }
}

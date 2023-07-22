package com.plm.wisignal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 10000; // Delay en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else {
            runnable = new Runnable() {
                public void run() {
                    iniciarEscaneoWifi(MainActivity.this);
                    handler.postDelayed(runnable, delay);
                }
            };
            handler.postDelayed(runnable, delay);
        }
    }

    private void iniciarEscaneoWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Asegúrate de tener el permiso antes de llamar a startScan() y getScanResults()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No hay permiso, regresa sin iniciar el escaneo
            return;
        }

        wifiManager.startScan();
        List<ScanResult> listaWifi = wifiManager.getScanResults();
        Collections.sort(listaWifi, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult o1, ScanResult o2) {
                return Integer.compare(o2.level, o1.level);
            }
        });

        // Lista para los nombres de las redes
        List<String> wifiNames = new ArrayList<>();

        int limit = Math.min(10, listaWifi.size()); // Limita a 10 o al tamaño de la lista si es menor
        for (int i = 0; i < limit; i++) {
            ScanResult resultadoEscaneo = listaWifi.get(i);
            wifiNames.add(resultadoEscaneo.SSID + " - " + resultadoEscaneo.level);
        }

        // Crea un ArrayAdapter y llena el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiNames);
        ListView lvWifiSignals = findViewById(R.id.lvWifiSignals);
        lvWifiSignals.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runnable = new Runnable() {
                    public void run() {
                        iniciarEscaneoWifi(MainActivity.this);
                        handler.postDelayed(runnable, delay);
                    }
                };
                handler.postDelayed(runnable, delay);
            } else {
                // El usuario rechazó los permisos
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Para evitar memory leaks
    }
}

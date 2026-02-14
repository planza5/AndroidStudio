package com.plm.iavideo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestCameraPermissions();
    }

    private void requestCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permiso no otorgado, solicitarlos
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permisos ya otorgados, proceder con la funcionalidad de la cámara
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permisos otorgados, iniciar la cámara
                startCamera();
            } else {
                // Permisos denegados, manejar la negativa
                handlePermissionDenial();
            }
        }
    }

    private void startCamera() {
        // Método para iniciar la cámara una vez los permisos están otorgados
    }

    private void handlePermissionDenial() {

    }
}

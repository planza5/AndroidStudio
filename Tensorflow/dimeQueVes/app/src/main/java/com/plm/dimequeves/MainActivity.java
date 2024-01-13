package com.plm.dimequeves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.util.Size;

public class MainActivity extends AppCompatActivity implements CameraCallback{
    private CameraHandler ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ch = new CameraHandler(this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 111);
        }else{
            ch.openCamera(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               ch.openCamera(this);
            } else {
                Log.e("PABLO","No se pudo abrir la camara por permisos");
            }
        }
    }

    @Override
    public void onCameraOpen(CameraHandler ch) {
        try {
            Size[] sizes=ch.getSizesCamera(this);

            for(Size size:sizes){
                Log.d("PABLO","w="+size.getWidth()+", "+size.getHeight());
            }
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCameraError() {
        Log.d("PABLO","w="+size.getWidth()+", "+size.getHeight());

    }

    @Override
    public void onCameraClosed() {

    }
}

package com.plm.dimequeves;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class CameraHandler {
    private CameraDevice cameraDevice;
    private CameraCallback cameraCallback;

    public CameraHandler(CameraCallback cameraCallback){
        this.cameraCallback = cameraCallback;
    }

    public Size[] getSizesCamera(Context context) throws CameraAccessException {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = manager.getCameraIdList()[0]; // ID de la cámara, ej: cámara trasera

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        return map.getOutputSizes(ImageFormat.JPEG);
    }

    public  void openCamera(Context context){
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try {
            cameraId = manager.getCameraIdList()[0];



            manager.openCamera(cameraId, new CameraDevice.StateCallback() {


                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    // Cámara abierta correctamente
                    // Aquí debes continuar con la configuración de tu sesión de captura
                    CameraHandler.this.cameraDevice=cameraDevice;
                    cameraCallback.onCameraOpen(CameraHandler.this);
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    cameraDevice.close();
                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int error) {
                    cameraDevice.close();
                }
            }, null); // Añade un Handler aquí si estás trabajando en un hilo diferente al principal

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


}

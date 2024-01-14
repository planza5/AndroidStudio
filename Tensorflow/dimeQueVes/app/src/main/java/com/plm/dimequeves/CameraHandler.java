package com.plm.dimequeves;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.core.ImageCaptureException;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.view.SurfaceView;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class CameraHandler {
    private final Activity activity;
    private final CameraCallback callback;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public CameraHandler(MainActivity activity) {
        this.activity=activity;
        this.callback=(CameraCallback) activity;
        this.cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
    }

    public void takePhoto() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(activity), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
                ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                byte[] data = new byte[buffer.capacity()];
                buffer.get(data);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                callback.onNewPhoto(bitmap);

                imageProxy.close();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                callback.onCameraError(exception.getMessage());
            }
        });

    }

    public void configureCamera(PreviewView previewView) throws Exception{
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // El permiso no ha sido concedido, solicitarlo
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 111);
        } else {
            cameraProviderFuture.addListener(() -> {
                try {
                    // Ahora puedes usar ProcessCameraProvider
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                 // Asegúrate de unbind todos los casos de uso antes de volver a enlazarlos
                    cameraProvider.unbindAll();
                    // Vincular los casos de uso
                    bindCameraUseCases(cameraProvider, previewView);

                } catch (ExecutionException | InterruptedException e) {
                    // Manejo de excepciones
                }
            }, ContextCompat.getMainExecutor(activity));
        }
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider, PreviewView previewView) {
        // Configuración de ImageCapture
        imageCapture = new ImageCapture.Builder()
                .setTargetResolution(new Size(320, 240))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3) // o RATIO_4_3 dependiendo de tus necesidades
                .build();


        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Selección de la cámara (trasera por defecto)
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        // Vinculando los casos de uso al ciclo de vida
        cameraProvider.bindToLifecycle((LifecycleOwner) activity, cameraSelector, preview, imageCapture);
        callback.onCameraConfigured();
    }


}

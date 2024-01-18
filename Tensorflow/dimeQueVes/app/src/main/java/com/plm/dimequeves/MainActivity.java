package com.plm.dimequeves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plm.dimequeves.ia.Recognition;
import com.plm.dimequeves.ia.Yolov5TFLiteDetector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CameraCallback{
    private CameraHandler ch;
    private SurfaceView surfaceView;
    private PreviewView previevView;
    private Button takePhotoButton;
    private Yolov5TFLiteDetector detector;
    private TextView defsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defsTextView=findViewById(R.id.defsTextView);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                try {
                    ch.takePhoto();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        previevView = findViewById(R.id.previewView);
        previevView.setScaleType(PreviewView.ScaleType.FILL_CENTER); // Otras opciones: FILL_START, FILL_END, etc.

        surfaceView = findViewById(R.id.overlayImageView);

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                ch=new CameraHandler(MainActivity.this);

                try {
                    ch.configureCamera(previevView);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
            // Implementa los métodos necesarios aquí
        });

        //IA
        detector=new Yolov5TFLiteDetector();
        
        detector.setModelFile("yolov5s-int8");
        detector.initialModel(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PABLO","Permisos concedidos cámara");
                try {
                    ch.configureCamera(previevView);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                Log.e("PABLO","No se pudo abrir la camara por permisos");
            }
        }
    }



    @Override
    public void onCameraError(String message) {
        Log.d("PABLO","error="+message);
        takePhotoButton.setEnabled(false);
    }


    @Override
    public void onNewPhoto(Bitmap bitmap) {
        Log.d("PABLO","New Photo");
        Canvas canvas = surfaceView.getHolder().lockCanvas();

        if (canvas != null) {
            canvas.drawBitmap(bitmap, 0, 0, null); // x, y son las coordenadas
            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }



        ArrayList<Recognition> res = detector.detect(bitmap);

        StringBuffer buffer=new StringBuffer();

        for(Recognition rec:res){
            String name=rec.getLabelName();
            RectF location = rec.getLocation();
            Log.d("PABLO","Veo objeto "+name);
            buffer.append(name).append(" ").append((int)(rec.getConfidence()*100)+"%").append(" ");
        }

        defsTextView.setText(buffer.toString());
    }

    @Override
    public void onCameraConfigured() {
        Log.d("PABLO","Camera configured");
        takePhotoButton.setEnabled(true);
    }
}

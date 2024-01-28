package com.plm.poseia;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CameraCallback{
    private CameraHandler ch;
    private SurfaceView surfaceView;
    private PreviewView previevView;
    private Button takePhotoButton;
    private TextView defsTextView;
    private MovenetPoseModelHandler handler;

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

        handler=new MovenetPoseModelHandler();

        try {
            handler.prepareModel(this,"movenet-pose.tflite");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        Paint p = new Paint();
        p.setColor(Color.RED); // Establece el color del borde del rectángulo
        p.setStyle(Paint.Style.FILL); // Establece el estilo para dibujar solo el borde
        p.setStrokeWidth(2); // Establece el ancho del borde

        Canvas canvas = surfaceView.getHolder().lockCanvas();

        if (canvas != null) {
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();

            float modelScaleX = (float) canvasWidth / 320;
            float modelScaleY = (float) canvasHeight / 320;

            Matrix matrix = new Matrix();

            // Calcular el factor de escala para ajustar el Bitmap al Canvas
            float scaleWidth = ((float) canvasWidth) / bitmap.getWidth();
            float scaleHeight = ((float) canvasHeight) / bitmap.getHeight();

            // Configurar el Matrix con el factor de escala
            matrix.setScale(scaleWidth, scaleHeight);
            canvas.drawBitmap(bitmap, matrix, null); // x, y son las coordenadas


            //IA


            try {
                handler.setImageInput(bitmap);
                float[][][][] result=handler.detect();

                for(int i=0;i<result[0][0].length;i++){
                    float x=result[0][0][i][0] * canvasWidth;
                    float y=result[0][0][i][1] * canvasHeight;
                    //float confidence=result[i][2];
                    canvas.drawCircle(y-10,x-10,20,p);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }



            surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void onCameraConfigured() {
        Log.d("PABLO","Camera configured");
        takePhotoButton.setEnabled(true);
    }
}

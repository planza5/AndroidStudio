package com.plm.sumaia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            loadModel("modelo.tflite");
            Log.d("PABLO",doInference(4,7)+"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Interpreter tflite;



    private void loadModel(String name) throws IOException {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd(name);
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            MappedByteBuffer tfliteModel = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
            tflite = new Interpreter(tfliteModel);
        } catch (IOException e) {
            Log.e("PABLO5", "Error al cargar el modelo.", e);
        }
    }


    private float doInference(float number1, float number2) {
        float[][] input = new float[1][2];
        input[0][0] = number1;
        input[0][1] = number2;

        float[][] output = new float[1][1];

        tflite.run(input, output);

        return output[0][0]; // La suma predicha
    }

}
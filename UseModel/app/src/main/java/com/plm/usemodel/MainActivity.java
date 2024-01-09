package com.plm.usemodel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.tensorflow.lite.Interpreter;


public class MainActivity extends AppCompatActivity {
    private MappedByteBuffer model;
    final float IMAGE_MEAN_X = 103.939f;
    final float IMAGE_MEAN_Y = 116.779f;
    final float IMAGE_MEAN_Z = 123.68f;

    final int IMAGE_SIZE_X = 224;
    final int IMAGE_SIZE_Y = 224;
    final int DIM_BATCH_SIZE = 1;
    final int DIM_PIXEL_SIZE = 3;
    final int NUM_BYTES_PER_CHANNEL = 4;
    final int NUM_CLASS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Manage().test();

        try {
            //Obtenemos el modelo
            Interpreter tflite = new Interpreter(getModel());
            //Obtenemos la imagen
            ByteBuffer imgData = convertBitmapToByteBuffer(R.drawable.hourglass);


            float[][] labelProbArray = new float[1][NUM_CLASS];
            tflite.run(imgData,labelProbArray);

            int predictedClass=argMax(labelProbArray[0]);
            Log.d("PABLO","He visto un "+Classification.getLabel(this,predictedClass));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int argMax(float[] array) {
        int maxIndex = 0;
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    public MappedByteBuffer getModel() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("resnet50.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer model = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        return model;
    }

    private ByteBuffer convertBitmapToByteBuffer(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap=Bitmap.createScaledBitmap(bitmap,224,224, true);
        ByteBuffer byteBuffer;
        int sizeImage = 224; // Tamaño típico para MobileNetV2
        byteBuffer = ByteBuffer.allocateDirect(4 * sizeImage * sizeImage * 3); // 4 bytes por píxel (float), 3 canales (RGB)
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[sizeImage * sizeImage];

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < sizeImage; ++i) {
            for (int j = 0; j < sizeImage; ++j) {
                final int val = intValues[pixel++];
                // Sustrae los valores específicos de cada canal de color
                byteBuffer.putFloat((((val >> 16) & 0xFF) - 103.939f) ); // R
                byteBuffer.putFloat((((val >> 8) & 0xFF) - 116.779f) );  // G
                byteBuffer.putFloat(((val & 0xFF) - 123.68f) );          // B
            }
        }
        return byteBuffer;
    }



}
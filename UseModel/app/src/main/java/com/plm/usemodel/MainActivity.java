package com.plm.usemodel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.tensorflow.lite.Interpreter;


public class MainActivity extends AppCompatActivity {
    private MappedByteBuffer model;
    private EditText editText;

    final float IMAGE_MEAN_X = 103.939f;
    final float IMAGE_MEAN_Y = 116.779f;
    final float IMAGE_MEAN_Z = 123.68f;

    final int IMAGE_SIZE_X = 224;
    final int IMAGE_SIZE_Y = 224;
    final int IMAGE_SIZE = 224;
    final int DIM_BATCH_SIZE = 1;
    final int DIM_PIXEL_SIZE = 3;
    final int NUM_BYTES_PER_CHANNEL = 4;
    final int NUM_CLASS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.prediction);

        try {
            //Obtenemos el modelo
            Interpreter tflite = new Interpreter(getModel("mobilenet_v1_1.0_224_quant.tflite"));
            int count=tflite.getInputTensorCount();
            int [] s=tflite.getInputTensor(0).shape();
            //Obtenemos la imagen
            ByteBuffer imgData = convertBitmapToByteBuffer(R.drawable.hourglass);

            int res[]=tflite.getOutputTensor(0).shape();
            byte[][] labelProbArray = new byte[1][NUM_CLASS];
            tflite.run(imgData,labelProbArray);

            int predictedClass=argMax(labelProbArray[0]);
            editText.setText(Classification.getLabel(this,predictedClass));

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

    public int argMax(byte[] array) {
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


    public MappedByteBuffer getModel(String path) throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(path);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer model = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        return model;
    }

    private ByteBuffer convertBitmapToByteBuffer(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap=Bitmap.createScaledBitmap(bitmap,IMAGE_SIZE_X,IMAGE_SIZE_Y, true);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 * IMAGE_SIZE * IMAGE_SIZE * 3); // 1 byte por píxel (byte), 3 canales (RGB)
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[IMAGE_SIZE * IMAGE_SIZE];

        // Suponiendo que 'bitmap' es tu imagen redimensionada
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            byteBuffer.put((byte) ((val >> 16) & 0xFF)); // R
            byteBuffer.put((byte) ((val >> 8) & 0xFF));  // G
            byteBuffer.put((byte) (val & 0xFF));         // B
        }
        return byteBuffer;
    }



}
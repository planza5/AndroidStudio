package com.plm.usemodel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

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
    final int NUM_CLASS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //Obtenemos el modelo
            Interpreter tflite = new Interpreter(getModel());
            //Obtenemos la imagen
            ByteBuffer imgData = getImageData(R.drawable.gato);


            float[][] labelProbArray = new float[1][NUM_CLASS];
            tflite.run(imgData,labelProbArray);

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public ByteBuffer getImageData(int id){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        bitmap= Bitmap.createScaledBitmap(bitmap,IMAGE_SIZE_X,IMAGE_SIZE_Y,false);
        int[] intValues = new int[IMAGE_SIZE_X * IMAGE_SIZE_Y];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        ByteBuffer imgData =
                ByteBuffer.allocateDirect(
                        DIM_BATCH_SIZE
                                * IMAGE_SIZE_X
                                * IMAGE_SIZE_Y
                                * DIM_PIXEL_SIZE
                                * NUM_BYTES_PER_CHANNEL);
        imgData.rewind();

        // Float model.
        int pixel=0;
        for (int i = 0; i < IMAGE_SIZE_X; ++i) {
            for (int j = 0; j < IMAGE_SIZE_Y; ++j) {
                int pixelValue = intValues[pixel++];

                int pixelValue1 = (pixelValue >> 16) & 0xFF;
                int pixelValue2 = (pixelValue >> 8) & 0xFF;
                int pixelValue3 = (pixelValue) & 0xFF;

                if(pixel<100)
                    System.out.println(pixel+": "+pixelValue3+", "+pixelValue2+", "+pixelValue1);

                float pixelValue1f = pixelValue1 - IMAGE_MEAN_X;
                float pixelValue2f = pixelValue2 - IMAGE_MEAN_Y;
                float pixelValue3f = pixelValue3 - IMAGE_MEAN_Z;


                imgData.putFloat(pixelValue1f);
                imgData.putFloat(pixelValue2f);
                imgData.putFloat(pixelValue3f);

                System.out.println();
            }
        }

        // Quantized model.
        /*pixel=0;
        for (int i = 0; i < IMAGE_SIZE_X; ++i) {
            for (int j = 0; j < IMAGE_SIZE_Y; ++j) {
                int pixelValue = intValues[pixel++];
                imgData.put((byte) ((pixelValue >> 16) & 0xFF));
                imgData.put((byte) ((pixelValue >> 8) & 0xFF));
                imgData.put((byte) (pixelValue & 0xFF));
            }
        }*/

        return imgData;
    }
}
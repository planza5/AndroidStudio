package com.plm.imagestyle;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ModelUtil {
    private static int IMAGE_SIZE=256;

    public void predict(Context context, String modelFileName, Bitmap bitmap){
        //Cargamos modelo
        Interpreter.Options options=new Interpreter.Options();
        Interpreter interpreter = null;

        try {
            interpreter = new Interpreter(loadModelFile(context, modelFileName), options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(bitmap);

        ImageProcessor imageProcessor=new ImageProcessor.Builder().
                add(new ResizeOp(IMAGE_SIZE,IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR)).
                add(new NormalizeOp(0.0f,1.0f)).
                build();

        imageProcessor.process(tensorImage);

        float [][][][] output=new float[1][1][1][100];
        interpreter.run(tensorImage.getBuffer(),output);

        System.out.println();
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFileNameInAssets) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFileNameInAssets);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}

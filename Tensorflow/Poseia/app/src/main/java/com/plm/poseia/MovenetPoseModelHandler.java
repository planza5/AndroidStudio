package com.plm.poseia;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MovenetPoseModelHandler {
    private Interpreter interpreter;
    private Interpreter.Options options = new Interpreter.Options();
    private Bitmap bitmap;

    public MovenetPoseModelHandler(){

    }

    public void prepareModel(Context context,String assetFileName) throws Exception{
        ByteBuffer bb=cargarModeloDesdeAssets(context,assetFileName);
        interpreter =  new Interpreter(bb, options);
    }

    public void setImageInput(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    public float[][][][] detect() throws Exception{
        if(bitmap==null){
            throw new Exception("Debes suministrar una imagen");
        }

        if(interpreter==null){
            throw new Exception("Debes suministrar un modelo");
        }

        TensorImage tensorImage=new TensorImage(DataType.FLOAT32);

        ImageProcessor processor=new ImageProcessor.Builder()
                            .add(new ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR)).build();

        tensorImage.load(bitmap);
        processor.process(tensorImage);

        float[][][][] output=new float[1][1][17][3];

        interpreter.run(tensorImage.getBuffer(),output);

        return output;
    }

    public ByteBuffer cargarModeloDesdeAssets(Context context,String nombreModelo) throws IOException {
        InputStream inputStream = context.getAssets().open(nombreModelo);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int size;
        while ((size = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, size);
        }
        byte[] modeloBytes = byteArrayOutputStream.toByteArray();
        ByteBuffer modeloByteBuffer = ByteBuffer.allocateDirect(modeloBytes.length);
        modeloByteBuffer.put(modeloBytes);
        return modeloByteBuffer;
    }
}

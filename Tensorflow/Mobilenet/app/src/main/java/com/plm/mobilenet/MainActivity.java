package com.plm.mobilenet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ModelHelper mh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ModelHelper().printInfo(this,"yolo_v5.tflite");

    }

    private void detectObjects(){
        try{
            Bitmap bitmap=FileUtils.getBitmap(this,R.drawable.street);
            File modelFile= FileUtils.getFile(this, "yolo_v5.tflite");
            new ModelHelper().detectObjects(this,modelFile,bitmap);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private void classifyImage(){
        mh=new ModelHelper();

        try {
            Bitmap bitmap=FileUtils.getBitmap(this,R.drawable.buitre);
            File fileModel= FileUtils.getFile(this,"mobilenet_v1_1.0_224_quant.tflite");
            List<String> categories=FileUtils.loadLabelList(this,"labels_mobilenet_quant_v1_224.txt");

            mh.classify(this, fileModel, bitmap, categories);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






}
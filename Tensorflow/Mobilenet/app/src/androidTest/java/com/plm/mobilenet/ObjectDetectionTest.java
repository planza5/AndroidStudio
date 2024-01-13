package com.plm.mobilenet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ObjectDetectionTest {

    @Test
    public void detectObjectTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap bitmap=FileUtils.getBitmap(appContext,R.drawable.street);


        Yolov5TFLiteDetector detector=new Yolov5TFLiteDetector();
        detector.setModelFile("yolov5s-int8");
        detector.initialModel(appContext);

        ArrayList<Recognition> res = detector.detect(bitmap);
        
        for(Recognition rec:res){
            String name=rec.getLabelName();
            RectF location = rec.getLocation();
        }
    }
}
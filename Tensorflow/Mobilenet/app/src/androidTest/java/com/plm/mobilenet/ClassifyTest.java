package com.plm.mobilenet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ClassifyTest {
    @Test
    public void classifyImageTest() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap bitmap=FileUtils.getBitmap(appContext,R.drawable.buitre);
        try {
            File modelFile= FileUtils.getFile(appContext,"mobilenet_v1_1.0_224_quant.tflite");
            List<String> labels=FileUtils.loadLabelList(appContext,"labels_mobilenet_quant_v1_224.txt");
            List<String[]> categories = new ModelHelper().classify(appContext, modelFile, bitmap, labels);

            Log.d("PABLO",categories.get(0)[0]);
            Log.d("PABLO",categories.get(0)[1]);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
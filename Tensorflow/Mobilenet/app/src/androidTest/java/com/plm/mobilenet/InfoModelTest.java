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
public class InfoModelTest {
    @Test
    public void modelInfoTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new ModelHelper().printInfo(appContext,"yolo_v5.tflite");
    }

}
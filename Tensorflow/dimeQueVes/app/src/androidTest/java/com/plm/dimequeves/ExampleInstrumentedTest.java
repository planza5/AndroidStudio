package com.plm.dimequeves;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.util.Log;
import android.util.Size;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest implements CameraCallback{
    private CameraHandler ch;
    private Context appContext;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.plm.dimequeves", appContext.getPackageName());
        ch = new CameraHandler(this);
    }

    @Override
    public void onCameraOpen(CameraHandler ch) {

    }

    @Override
    public void onCameraError() {

    }

    @Override
    public void onCameraClosed() {

    }
}
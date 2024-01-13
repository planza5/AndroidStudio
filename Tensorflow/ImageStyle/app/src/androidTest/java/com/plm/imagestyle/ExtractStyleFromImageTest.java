package com.plm.imagestyle;

import android.content.Context;

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
public class ExtractStyleFromImageTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.plm.imagestyle", appContext.getPackageName());

        ModelUtil modelUtil = new ModelUtil();
        modelUtil.predict(appContext, "prediccion de estilo.tflite", FileUtils.getBitmap(appContext,R.drawable.las_meninas));
    }
}
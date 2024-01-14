package com.plm.dimequeves;

import android.graphics.Bitmap;

public interface CameraCallback {
    public void onCameraError(String message);
    public void onNewPhoto(Bitmap bitmap);
    public void onCameraConfigured();
}

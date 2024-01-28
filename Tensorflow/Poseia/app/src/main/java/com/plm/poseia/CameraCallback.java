package com.plm.poseia;

import android.graphics.Bitmap;

public interface CameraCallback {
    public void onCameraError(String message);
    public void onNewPhoto(Bitmap bitmap);
    public void onCameraConfigured();
}

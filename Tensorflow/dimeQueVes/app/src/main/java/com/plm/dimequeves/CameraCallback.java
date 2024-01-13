package com.plm.dimequeves;

public interface CameraCallback {
    public void onCameraOpen(CameraHandler cameraHandler);
    public void onCameraError();
    public void onCameraClosed();
}

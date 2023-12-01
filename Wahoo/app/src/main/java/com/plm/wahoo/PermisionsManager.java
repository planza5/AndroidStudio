package com.plm.wahoo;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class PermisionsManager{
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private final static int MY_PERMISSIONS_REQUEST_SCAN = 2;
    private final static int MY_PERMISSIONS_REQUEST_CONNECT = 3;
    private boolean grantedLocation=false;
    private boolean grantedScan=false;
    private boolean grantedConnect=false;

    private final MainActivity activity;

    public PermisionsManager(MainActivity activity){
        this.activity=activity;
    }

    public void requestPermissionLocation() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos al usuario
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            activity.locationBtResult(true);
        }
    }

    public void requestPermissionScanBt() {
        //Pedimos permiso para scan
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.BLUETOOTH_SCAN)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos al usuario
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    MY_PERMISSIONS_REQUEST_SCAN);
        } else {
            activity.scanBtResult(true);
        }
    }

    public void requestPermissionConnectBt() {
        //Pedimos permiso para connect
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos al usuario
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    MY_PERMISSIONS_REQUEST_CONNECT);
        } else {
            activity.connectBtResult(true);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                activity.locationBtResult(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            }case MY_PERMISSIONS_REQUEST_CONNECT:{
                activity.connectBtResult(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            }case MY_PERMISSIONS_REQUEST_SCAN:{
                activity.scanBtResult(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    public void requestPermissions() {
        requestPermissionLocation();
        requestPermissionScanBt();
        requestPermissionConnectBt();
    }
}

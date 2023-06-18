package com.plm.createwalls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements VoiceInterface{

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private VoiceManager vs;
    private AppController ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ac = new AppController(this);
        vs = new VoiceManager(this, this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            vs.startListening();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                vs.startListening();
            } else {
                // Show a dialog explaining why this app needs this permission
                // or gracefully degrade the app functionality
            }
        }
    }

    @Override
    public void newText(String text) {
        ac.processCommand(text);
    }

    @Override
    public void readyForSpeech() {

    }

    @Override
    public void error(int error) {
        Log.d("PABLO",Integer.toString(error));
    }
}

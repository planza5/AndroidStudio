package com.plm.twp;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SpeechDelegateListener{
    private SpeechDelegate speechDelegate;
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = findViewById(R.id.responseText);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }else{
            speechDelegate = new SpeechDelegate(this,this);
        }


        speechDelegate.startListening();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            speechDelegate = new SpeechDelegate(this, this);
        } else {
            responseText.setText("Permiso de grabación denegado");
            finish();
        }
    }
    @Override
    public void onSpeechError(int error,String errorMessage) {
        responseText.setText(errorMessage + " "+error);
    }

    @Override
    public void onSpeechResults(String results) {
        responseText.setText(results);
//        speechDelegate.startListening();
    }

    @Override
    public void onSpeechStart() {
        System.out.println("speech start");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechDelegate.destroy();
    }
}

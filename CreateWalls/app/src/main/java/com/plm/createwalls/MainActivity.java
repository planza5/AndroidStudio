package com.plm.createwalls;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
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
            vs.startListening(1000);
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
                vs.startListening(1000);
            } else {
                Log.d("PABLO","Show a dialog explaining why this app needs this permission");
            }
        }
    }

    @Override
    public void newText(String text) {
        ac.processCommand(text);
        vs.startListening(1000);
    }

    @Override
    public void readyForSpeech() {
    }

    @Override
    public void error(int error) {
        logError(error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vs != null) {
            vs.destroy();
        }
    }

    public void logError(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Error de audio";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Error del cliente";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Permisos insuficientes";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Error de red";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Tiempo de espera de red excedido";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No se encontraron coincidencias";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "El reconocedor está ocupado";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "Error del servidor";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "Tiempo de espera de habla excedido";
                break;
            default:
                message = "Error desconocido";
                break;
        }
        Log.e("PABLO", message);
    }

}

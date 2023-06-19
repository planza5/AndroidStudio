package com.plm.createwalls;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceManager implements RecognitionListener {

    private TextToSpeech tts;
    private SpeechRecognizer sr;
    private VoiceInterface listener;

    public VoiceManager(Context context, VoiceInterface listener) {
        this.listener = listener;
        setupTts(context);
        setupSr(context);
    }


    public void startListening(long delay) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sr.startListening(createRecognizerIntent());
    }

    private Intent createRecognizerIntent() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        return recognizerIntent;
    }

    public void setupTts(Context context){
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("es", "ES"));
                }else{
                    Log.d("TTS","Error en setup");
                }
            }
        });
    }

    public void setupSr(Context context) {
        sr = SpeechRecognizer.createSpeechRecognizer(context);
        sr.setRecognitionListener(this);
    }

    public void saySomething(String something) {
        // TODO: Make the TTS say something
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {
        listener.error(i);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String dictatedText = matches.get(0);
            listener.newText(dictatedText);
        } else {
            listener.error(Ctes.SPEECH_ERROR_NO_MATCHES);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    public void destroy() {
        if (sr != null) {
            sr.destroy();
        }
        if (tts != null) {
            tts.shutdown();
        }
    }
}

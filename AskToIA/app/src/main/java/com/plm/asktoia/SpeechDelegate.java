package com.plm.asktoia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechDelegate implements RecognitionListener {
    public void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "MY_APP"
        );
    }

    public void stop() {
        textToSpeech.stop();
    }

    public interface SpeechDelegateListener {
        void onSpeechError(String errorMessage);
        void onSpeechResults(String results);
        void onSpeechStart();

        void endText();

        void startText();
    }

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private SpeechDelegateListener listener;

    public SpeechDelegate(Context context, SpeechDelegateListener listener) {
        this.listener = listener;
        setupSpeechRecognizer(context);
    }

    public void setupSpeechRecognizer(Context context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
    }

    public void setupTextToSpeech(Context context){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("es", "ES"));

                    textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            listener.startText();
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            listener.endText();
                        }

                        @Override
                        public void onError(String utteranceId) {
                            listener.endText();
                        }
                    });
                }
            }
        });



    }

    public void startListening() {
        speechRecognizer.startListening(createRecognizerIntent());
    }

    private Intent createRecognizerIntent() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        return recognizerIntent;
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

        if(textToSpeech !=null){
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
    }

    @Override
    public void onBeginningOfSpeech() {
       listener.onSpeechStart();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        String errorMessage = Ctes.getError(error);
        listener.onSpeechError(errorMessage);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        if (matches != null && !matches.isEmpty()) {
            String dictatedText = matches.get(0);
            listener.onSpeechResults(dictatedText);
        } else {
            listener.onSpeechError("No se encontraron coincidencias");
        }

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


}

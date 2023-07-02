package com.plm.twp;

public interface SpeechDelegateListener {
    void onSpeechError(int error,String errorMessage);
    void onSpeechResults(String results);
    void onSpeechStart();
}

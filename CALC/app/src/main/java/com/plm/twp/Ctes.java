package com.plm.twp;

import android.content.Context;
import android.speech.SpeechRecognizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Ctes {
    private static final String FILE_NAME = "apikey.txt";

    public static String getError(int errorCode) {
        String errorMessage = "Unknown error";

        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                errorMessage = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorMessage = "Client-side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorMessage = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorMessage = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorMessage = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorMessage = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorMessage = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorMessage = "Server-side error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorMessage = "No speech input";
                break;
            default:
                errorMessage = "Unknown error";
                break;
        }

        return errorMessage;
    }



        public static String readApiKey(Context context) throws Exception{
            File file = new File(context.getFilesDir(), FILE_NAME);

            if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    return stringBuilder.toString();
                }

            return null;
        }

        public static void saveApiKey(Context context, String apiKey) throws Exception{
            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(apiKey.getBytes());
            outputStream.close();
        }

    public static String API_KEY;


}

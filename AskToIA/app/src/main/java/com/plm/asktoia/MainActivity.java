package com.plm.asktoia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements SpeechDelegate.SpeechDelegateListener, ChatGPTApiClient.ChatGPTApiClientListener {

    private static SpeechDelegate speechDelegate;

    private static ChatGPTApiClient chatGPTApiClient = null;
    private Button startListeningButton;

    private Button postTextChatGPTButton;
    private Button readTextSpeechButton;
    private EditText requestText;
    private TextView responseText;

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postTextChatGPTButton = findViewById(R.id.startPostChatGPTButton);
        startListeningButton = findViewById(R.id.startListeningButton);
        readTextSpeechButton = findViewById(R.id.readResponseButton);

        requestText = findViewById(R.id.requestText);
        responseText = findViewById(R.id.responseText);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            setupSpeechDelegate();
        }

        setupChatGPTApiClient();

        speechDelegate.setupTextToSpeech(this);
        speechDelegate.setupSpeechRecognizer(this);

        startListeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechDelegate.startListening();
                progressBar1.setVisibility(View.VISIBLE);
                responseText.setText("");
            }
        });

        postTextChatGPTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextToChatGPT(requestText.getText().toString());
            }
        });

        readTextSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = responseText.getText().toString();
                speechDelegate.speak(text);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechDelegate.destroy();
    }

    private void setupSpeechDelegate() {
        speechDelegate = new SpeechDelegate(this, this);
    }

    private void setupChatGPTApiClient(){
        chatGPTApiClient = new ChatGPTApiClient(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupSpeechDelegate();
        } else {
            responseText.setText("Permiso de grabación denegado");
            finish();
        }
    }


    @Override
    public void onSpeechResults(String text) {
        requestText.setText(text);
        setEnabled(true,true,true,false,false);
    }

    @Override
    public void onSpeechStart() {
        setEnabled(false,false,false,true,false);
    }

    @Override
    public void onSpeechError(String errorMessage) {
        setEnabled(true,true,true,true,true);
    }

    private void sendTextToChatGPT(String inputText) {
        if (inputText.isEmpty()) {
            responseText.setText("Texto vacío");
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.GONE);
        startListeningButton.setEnabled(false);

        new SendTextToChatGPTTask(this).execute(inputText);
    }

    @Override
    public void requestingChatGPT() {
        setEnabled(false,false,false,false,true);
    }

    @Override
    public void resultsChatGPT() {
        setEnabled(true,true,true,false,false);
    }

    @Override
    public void onErrorChatGPT() {
        setEnabled(true,true,true,false,false);
    }


    private static class SendTextToChatGPTTask extends AsyncTask<String, Void, String> {

        private final MainActivity mainActivity;
        private WeakReference<MainActivity> activityReference;

        SendTextToChatGPTTask(MainActivity mainActivity) {
            activityReference = new WeakReference<>(mainActivity);
            this.mainActivity = mainActivity;
        }

        @Override
        protected String doInBackground(String... params) {
            String inputText = params[0];
            return chatGPTApiClient.sendTextToChatGPT(inputText);
        }

        @Override
        protected void onPostExecute(String result) {
            mainActivity.setEnabled(true,true,true,false,false);
        }
    }

    private void setEnabled(boolean b1, boolean b2, boolean b3, boolean p1, boolean p2){
        startListeningButton.setEnabled(b1);
        postTextChatGPTButton.setEnabled(b2);
        readTextSpeechButton.setEnabled(b3);
        progressBar1.setVisibility(p1?View.VISIBLE:View.GONE);
        progressBar2.setVisibility(p2?View.VISIBLE:View.GONE);
    }

}

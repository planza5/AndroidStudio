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

public class MainActivity extends AppCompatActivity implements SpeechDelegate.SpeechDelegateListener {

    private SpeechDelegate speechDelegate;
    private Button startListeningButton;

    private Button startPostButtonn;
    private EditText requestText;
    private TextView responseText;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startPostButtonn = findViewById(R.id.startPostButton);
        startListeningButton = findViewById(R.id.startListeningButton);
        requestText = findViewById(R.id.requestText);
        responseText = findViewById(R.id.responseText);
        progressBar = findViewById(R.id.progressBar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            setupSpeechDelegate();
        }

        startListeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechDelegate.startListening();
                progressBar.setVisibility(View.VISIBLE);
                responseText.setText("");
            }
        });

        startPostButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextToChatGPT(requestText.getText().toString());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechDelegate.release();
    }

    private void setupSpeechDelegate() {
        speechDelegate = new SpeechDelegate(this, this);
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
        sendTextToChatGPT(text);
    }

    @Override
    public void onSpeechError(String errorMessage) {
        responseText.setText(errorMessage);
        progressBar.setVisibility(View.GONE);
    }

    private void sendTextToChatGPT(String inputText) {
        if (inputText.isEmpty()) {
            responseText.setText("Texto vacío");
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        new SendTextToChatGPTTask(this).execute(inputText);
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
            ChatGPTApiClient apiClient = new ChatGPTApiClient();
            return apiClient.sendTextToChatGPT(inputText);
        }

        @Override
        protected void onPostExecute(String result) {
            mainActivity.responseText.setText(result);
            mainActivity.progressBar.setVisibility(View.GONE);
            mainActivity.startListeningButton.setEnabled(true);
        }
    }

}

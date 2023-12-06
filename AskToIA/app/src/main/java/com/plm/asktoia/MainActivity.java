package com.plm.asktoia;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

    private CheckBox sendDirectlyToChatGPTCheckbox;
    private boolean reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API_KEY
        try{
            if(Ctes.API_KEY==null){
                Ctes.API_KEY=Ctes.readApiKey(this);

                if(Ctes.API_KEY==null){
                    askUserForString(new StringInputCallback() {
                        @Override
                        public void onInputReceived(String input) {
                            Ctes.API_KEY=input;
                            try {
                                Ctes.saveApiKey(MainActivity.this,Ctes.API_KEY);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }




        postTextChatGPTButton = findViewById(R.id.startPostChatGPTButton);
        startListeningButton = findViewById(R.id.startListeningButton);
        readTextSpeechButton = findViewById(R.id.readResponseButton);
        sendDirectlyToChatGPTCheckbox= findViewById(R.id.sendDirectlyToChatGPTCheckbox);

        requestText = findViewById(R.id.requestText);
        responseText = findViewById(R.id.responseText);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        setupSpeechDelegate();
        setupChatGPTApiClient();

        speechDelegate.setupTextToSpeech(this);
        speechDelegate.setupSpeechRecognizer(this);

        setEnabled(true,false,false,false, false);

        startListeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechDelegate.startListening();
                setEnabled(false,false,false,true,false);
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
                if(!reading){
                    String text = responseText.getText().toString();
                    speechDelegate.speak(text);
                }
                else{
                    speechDelegate.stop();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            readTextSpeechButton.setText("Leer Respuesta");
                        }
                    });
                    reading = false;
                }
            }
        });

        /*requestText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(requestText.getText().length()>0){
                    requestText.setEnabled(true);
                }else{
                    requestText.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        /*
        byte[] bites=Ctes.API_KEY.getBytes();

        for(byte b:bites){
            System.out.print(b+",");
        }*/
    }

    public void apiKeyAvailable(){

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
        setEnabled(true,true,false,false,false);

        if(sendDirectlyToChatGPTCheckbox.isChecked()){
           sendTextToChatGPT(text);
        }
    }

    @Override
    public void onSpeechStart() {
        //setEnabled(true,false,false,false,false);
    }

    @Override
    public void endText() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readTextSpeechButton.setText("Leer Respuesta");
            }
        });
        reading = false;
    }

    @Override
    public void startText() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readTextSpeechButton.setText("Parar de Leer");
            }
        });

        reading=true;
    }

    @Override
    public void onSpeechError(String errorMessage) {
        setEnabled(true,false,false,false,false);
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
            String result=chatGPTApiClient.sendTextToChatGPT(inputText);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mainActivity.setEnabled(true,true,true,false,false);

            MainActivity activity = activityReference.get();

            if (activity == null || activity.isFinishing())
                return;

            System.out.println(result);
            // update the responseText TextView with the result
            activity.responseText.setText(result);
            activity.responseText.setText(result);
        }
    }

    private void setEnabled(boolean b1, boolean b2, boolean b3, boolean p1, boolean p2){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startListeningButton.setEnabled(b1);
                postTextChatGPTButton.setEnabled(true);
                readTextSpeechButton.setEnabled(b3);
                progressBar1.setVisibility(p1?View.VISIBLE:View.GONE);
                progressBar2.setVisibility(p2?View.VISIBLE:View.GONE);
            }
        });
    }


    interface StringInputCallback {
        void onInputReceived(String input);
    }

    public void askUserForString(final StringInputCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese una cadena");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                callback.onInputReceived(userInput);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

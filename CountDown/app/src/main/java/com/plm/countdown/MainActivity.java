package com.plm.countdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;

public class MainActivity extends AppCompatActivity{
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void setupTimerIntent() {
        int seconds=10;
        String message="pablo";
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER).putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds).putExtra(AlarmClock.EXTRA_SKIP_UI, true);

        ComponentName activity = intent.resolveActivity(getPackageManager());

        if (activity != null) {
            startActivity(intent);
        }
    }

    @Override
   private void


}
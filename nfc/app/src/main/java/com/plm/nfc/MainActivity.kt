package com.plm.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.app.PendingIntent
import android.nfc.NdefMessage
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {
    private  lateinit var nfcAdapter: NfcAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("PABLO","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)


        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }

                for(message in messages){
                    for(record in message.records){
                        val bytearray=record.payload
                        Log.d("PABLO", String(bytearray))
                    }
                }
            }




        }

        Log.d("PABLO","OnNewIntent")
    }


}
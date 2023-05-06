package com.plm.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.app.PendingIntent
import androidx.activity.result.contract.ActivityResultContracts


class MainActivityCopy : AppCompatActivity(), NfcAdapter.ReaderCallback {
    private  lateinit var nfcAdapter: NfcAdapter


    public override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("PABLO","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setupNfc()
    }

    private fun setupNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if(nfcAdapter!=null){
            nfcAdapter.enableReaderMode(
                this,
                this::onTagDiscovered,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B,
                null
            )
        }
    }

    public override fun onNewIntent(intent: Intent?) {
        Log.d("PABLO","OnNewIntent")
        super.onNewIntent(intent)
    }

    override fun onTagDiscovered(tag: Tag) {
        Log.d("PABLO","OnTagDiscovered")

        for(t in tag?.techList){
            Log.d("PABLO",t)
        }
    }


}
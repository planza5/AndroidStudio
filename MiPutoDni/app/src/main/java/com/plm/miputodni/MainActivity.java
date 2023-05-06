package com.plm.miputodni;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.Security;

import de.tsenger.androsmex.mrtd.DG1_Dnie;
import de.tsenger.androsmex.mrtd.DG2;
import es.gob.jmulticard.jse.provider.DnieKeyStore;
import es.gob.jmulticard.jse.provider.DnieProvider;
import es.gob.jmulticard.jse.provider.MrtdKeyStoreImpl;


public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {
    private static String CAN="860288";
    private NfcAdapter _myNfcAdapter;
    ImageView imagedni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        imagedni = findViewById(R.id.imagedni);
        enableReaderMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }




    private void getData(Tag tag){
        try {
            final DnieProvider provider = new DnieProvider();
            Security.insertProviderAt(provider,1);

            KeyStore keyStore = new DnieKeyStore(new MrtdKeyStoreImpl(CAN, tag), provider);
            keyStore.load(null,null);

            DG2 data2 = ((DnieKeyStore)keyStore).getDatagroup2();

            J2kStreamDecoder j2k = new J2kStreamDecoder();
            ByteArrayInputStream bis = new ByteArrayInputStream(data2.getImageBytes());
            Bitmap photo=j2k.decode(bis);

            if(photo!=null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imagedni.setImageBitmap(photo);
                    }
                });

            }else{
                Log.d("PABLO","photo null");
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.d("PABLO","onTagDiscovered");
        getData(tag);
    }


    private void enableReaderMode ()
    {
        _myNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        _myNfcAdapter.setNdefPushMessage(null, this);
        _myNfcAdapter.setNdefPushMessageCallback(null, this);

        Bundle options = new Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 1000);
        _myNfcAdapter.enableReaderMode(this,
                this,
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK 	|
                        NfcAdapter.FLAG_READER_NFC_A 	|
                        NfcAdapter.FLAG_READER_NFC_B    |
                        NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                options);
    }
}
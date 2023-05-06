package com.plm.miputaip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String ip=null;
        TextView tv=(TextView)findViewById(R.id.txtip);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String ip=getIp();
                Log.d("PABLO",ip);

                runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tv.setText(ip==null?"ERROR":ip);
                    }
                });
            }
        }).start();


    }

    protected String getIp(){
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        }catch(Exception ex){
            return null;
        }
    }
}
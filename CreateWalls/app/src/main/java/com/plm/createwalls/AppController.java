package com.plm.createwalls;

import android.content.Context;
import android.util.Log;

public class AppController{
    private final Context context;

    public AppController(Context context){
        this.context=context;
    }


    public void processCommand(String text) {
        Log.d("PABLO",text);
    }
}

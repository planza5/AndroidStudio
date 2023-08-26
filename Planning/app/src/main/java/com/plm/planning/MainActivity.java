package com.plm.planning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.os.Bundle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Task> tasks=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        CustomView myDrawingView = new CustomView(this);
        setContentView(myDrawingView);

        try {
            tasks.add(new Task("28/08/2023","30/08/2023"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
package com.plm.planning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.os.Bundle;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        CustomView myDrawingView = new CustomView(this);
        setContentView(myDrawingView);

        try {
            Model.getTasks().add( new Task("31/08/2023","06/09/2023"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
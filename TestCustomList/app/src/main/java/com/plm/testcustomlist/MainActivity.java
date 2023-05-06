package com.plm.testcustomlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.plm.customlist.CustomAdapter;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customAdapter = CustomAdapter.makeCustomAdapter(this,R.id.list_view,R.id.list_item);
        customAdapter.add("Hol");
    }
}
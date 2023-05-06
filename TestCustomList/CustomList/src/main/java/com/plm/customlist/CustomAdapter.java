package com.plm.customlist;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<List<String>> {
    private static CustomAdapter customAdapter;

    private CustomAdapter(@NonNull Context context, int resourceTaskList) {
        super(context, resourceTaskList);
    }

    public static CustomAdapter makeCustomAdapter(@NonNull Activity activity, int listItemId, int listViewId){
        ListView listView = activity.findViewById(listViewId);
        customAdapter=new CustomAdapter(activity, listItemId);
        listView.setAdapter(customAdapter);

        return customAdapter;
    }

    public void addItem(List<String> values){
        customAdapter.add(values);
    }
}

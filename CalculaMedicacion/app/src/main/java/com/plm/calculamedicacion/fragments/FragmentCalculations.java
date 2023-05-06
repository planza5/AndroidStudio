package com.plm.calculamedicacion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plm.calculamedicacion.R;
import com.plm.calculamedicacion.Person;
import com.plm.calculamedicacion.auto.AutoListViewManager;

public class FragmentCalculations extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_config,container,false);

        AutoListViewManager lm=new AutoListViewManager(Person.class,"name","surname","age","dead");
        return lm.getListView(vg,R.id.listview);
    }
}

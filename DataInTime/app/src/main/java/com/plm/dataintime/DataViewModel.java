package com.plm.dataintime;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataViewModel extends ViewModel {
    private final List<Data> mDataSet = new ArrayList<>();

    public List<Data> getDataSet() {
        return mDataSet;
    }
}

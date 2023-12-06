package com.plm.dataintime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Data>> data = new MutableLiveData<>();

    public void setData(List<Data> newData) {
        data.setValue(newData);
    }

    public LiveData<List<Data>> getData() {
        return data;
    }
}

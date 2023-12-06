package com.plm.dataintime.fragment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.plm.dataintime.Data;
import com.plm.dataintime.R;
import com.plm.dataintime.SharedViewModel;

import java.util.List;

public class FragmentDataInTime extends Fragment {
    private List<Data> dataInTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dataInTime=sharedViewModel.getData().getValue();

        DataView dataView = new DataView(getContext(),dataInTime);

        return dataView;
    }
}

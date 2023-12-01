package com.plm.dataintime.fragment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plm.dataintime.Data;
import com.plm.dataintime.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FragmentDataList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_list, container, false);    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.data_list_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        try {
            List<Data> myDataset=new ArrayList();
            myDataset.add(new Data("14/10/1968",69.13f,3410f,4940f, 68f));
            myDataset.add(new Data("15/11/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));
            myDataset.add(new Data("13/12/1968",1f,1f,1f, 1f));

            RecyclerView.Adapter mAdapter = new MyAdapter(myDataset);
            recyclerView.setAdapter(mAdapter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

}

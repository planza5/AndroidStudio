package com.plm.dataintime.fragment1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plm.dataintime.Data;
import com.plm.dataintime.DataManager;
import com.plm.dataintime.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentDataList extends Fragment {

    private List<Data> myDataSet;

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


        myDataSet = DataManager.loadData(getContext());
        RecyclerView.Adapter mAdapter = new MyAdapter(myDataSet);
        recyclerView.setAdapter(mAdapter);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Inflate and set the layout for the dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.form_layout, null);
                builder.setView(dialogView);

                // Configura los EditText aquí si es necesario

                // Agrega botones de acción para el diálogo
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
                        Date date = null;

                        try {
                            date = Data.getSdf().parse(editTextDate.getText().toString());
                        } catch (ParseException e) {
                           alert("La fecha no es válida");
                        }

                        EditText editTextFev1Cvf = dialogView.findViewById(R.id.editTextFev1Cvf);
                        float fev1Cvf = 0;

                        try{
                            fev1Cvf=Float.parseFloat(editTextFev1Cvf.getText().toString());
                        }catch (NumberFormatException nfe){
                            alert("fev1Cvf no es válido");
                        }


                        EditText editTextFev1 = dialogView.findViewById(R.id.editTextFev1);
                        int fev1 = 0;

                        try {
                            fev1=Integer.parseInt(editTextFev1.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }


                        EditText editTextFev1Tpc = dialogView.findViewById(R.id.editTextFev1Tpc);
                        int fev1Tpc = 0;

                        try {
                            fev1Tpc=Integer.parseInt(editTextFev1Tpc.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }


                        EditText editTextCvf = dialogView.findViewById(R.id.editTextCvf);
                        int cvf = 0;

                        try {
                            cvf = Integer.parseInt(editTextCvf.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }



                        EditText editTextCvfTpc = dialogView.findViewById(R.id.editTextCvfTpc);
                        int cvfTpc = 0;

                        try {
                            cvfTpc = Integer.parseInt(editTextCvfTpc.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }


                        EditText editTextFev2575 = dialogView.findViewById(R.id.editTextFev2575);
                        int fev2575 = 0;

                        try {
                            fev2575 = Integer.parseInt(editTextFev2575.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }

                        EditText editTextFev2575Tpc = dialogView.findViewById(R.id.editTextFev2575Tpc);
                        int fev2575Tpc = 0;

                        try {
                            fev2575Tpc = Integer.parseInt(editTextFev2575Tpc.getText().toString());
                        } catch (NumberFormatException e) {
                            alert("a");
                        }


                        try {
                            Data newData = new Data(date, fev1Cvf, fev1, fev1Tpc, cvf, cvfTpc, fev2575, fev2575Tpc);
                            myDataSet.add(newData);
                            mAdapter.notifyDataSetChanged();
                            DataManager.saveData(getContext(),myDataSet);
                        } catch (NumberFormatException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    private void alert(String message) {
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

}

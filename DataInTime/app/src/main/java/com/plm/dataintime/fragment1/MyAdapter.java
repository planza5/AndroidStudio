package com.plm.dataintime.fragment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plm.dataintime.Data;
import com.plm.dataintime.R;

import java.text.BreakIterator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Data> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtFev1;
        public TextView txtFev;
        public TextView txtFev1Cvf;
        public TextView txtCvf;
        // Define más vistas si es necesario

        public MyViewHolder(View v) {
            super(v);
            txtDate = v.findViewById(R.id.date);
            txtFev1 = v.findViewById(R.id.fev1);
            txtFev= v.findViewById(R.id.fev);
            txtCvf = v.findViewById(R.id.cvf);
            txtFev1Cvf = v.findViewById(R.id.fev1Cvf);
        }
    }


    // Constructor adecuado para el dataset
    public MyAdapter(List<Data> myDataset) {
        mDataset = myDataset;
    }

    // Crea nuevas vistas (invocado por el layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Reemplaza el contenido de una vista (invocado por el layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtDate.setText(mDataset.get(position).getStringDate());
        holder.txtFev1Cvf.setText("FEV1/CVF: "+Float.toString(mDataset.get(position).getFev1Cvf()));
        holder.txtFev1.setText("FEV1: "+Float.toString(mDataset.get(position).getFev1()));
        holder.txtCvf.setText("CVF: "+Float.toString(mDataset.get(position).getCvf()));
        holder.txtFev.setText("FEV: "+Float.toString(mDataset.get(position).getFev()));
    }

    // Retorna el tamaño de tu dataset (invocado por el layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


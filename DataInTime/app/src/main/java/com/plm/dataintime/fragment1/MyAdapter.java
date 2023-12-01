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
    private List<Data> myDataSet;
    private OnItemClickListener listener;
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtFev1Cvf;
        public TextView txtFev1;
        public TextView txtFev1Tpc;
        public TextView txtFev_25_75;

        public TextView txtFev_25_75_Tpc;
        public TextView txtCvf;
        public TextView txtCvfTpc;

        // Define más vistas si es necesario

        public MyViewHolder(View v) {
            super(v);
            txtDate = v.findViewById(R.id.date);
            txtFev1 = v.findViewById(R.id.fev1);
            txtFev1Tpc = v.findViewById(R.id.fev1Tpc);
            txtFev_25_75= v.findViewById(R.id.fev_25_75);
            txtFev_25_75_Tpc= v.findViewById(R.id.fev_25_75_Tpc);
            txtCvf = v.findViewById(R.id.cvf);
            txtCvfTpc = v.findViewById(R.id.cvfTpc);
            txtFev1Cvf = v.findViewById(R.id.fev1Cvf);
        }
    }


    // Constructor
    public MyAdapter(List<Data> myDataSet, OnItemClickListener listener) {
        this.myDataSet = myDataSet;
        this.listener = listener;
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

        holder.txtFev1Cvf.setText(Float.toString(mDataset.get(position).getFev1Cvf()));

        holder.txtFev1.setText(Integer.toString(mDataset.get(position).getFev1()));
        holder.txtFev1Tpc.setText(Integer.toString(mDataset.get(position).getFev1Tpc())+"%");

        holder.txtCvf.setText(Integer.toString(mDataset.get(position).getCvf()));
        holder.txtCvfTpc.setText(Integer.toString(mDataset.get(position).getCvfTpc())+"%");

        holder.txtFev_25_75.setText(Integer.toString(mDataset.get(position).getFev2575()));
        holder.txtFev_25_75_Tpc.setText(Integer.toString(mDataset.get(position).getFev2575Tpc())+"%");
    }

    // Retorna el tamaño de tu dataset (invocado por el layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


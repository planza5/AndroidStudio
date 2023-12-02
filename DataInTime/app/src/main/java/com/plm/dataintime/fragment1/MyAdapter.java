package com.plm.dataintime.fragment1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.plm.dataintime.Data;
import com.plm.dataintime.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Data> mDataSet;
    private OnItemClickListener listener;
    private int selectedPos = RecyclerView.NO_POSITION; // Inicialmente, no hay selección

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

        public void bind(final Data item, final int position, final OnItemClickListener listener) {
            // Configura tus vistas aquí

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }


    // Constructor
    public MyAdapter(List<Data> myDataSet, OnItemClickListener listener) {
        this.mDataSet = myDataSet;
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
        holder.txtDate.setText(mDataSet.get(position).getStringDate());

        holder.txtFev1Cvf.setText(Float.toString(mDataSet.get(position).getFev1Cvf()));

        holder.txtFev1.setText(Integer.toString(mDataSet.get(position).getFev1()));
        holder.txtFev1Tpc.setText(Integer.toString(mDataSet.get(position).getFev1Tpc())+"%");

        holder.txtCvf.setText(Integer.toString(mDataSet.get(position).getCvf()));
        holder.txtCvfTpc.setText(Integer.toString(mDataSet.get(position).getCvfTpc())+"%");

        holder.txtFev_25_75.setText(Integer.toString(mDataSet.get(position).getFev2575()));
        holder.txtFev_25_75_Tpc.setText(Integer.toString(mDataSet.get(position).getFev2575Tpc())+"%");

        Data item = mDataSet.get(position);
        holder.bind(item, position, listener);

        holder.itemView.setSelected(selectedPos==position);

        holder.itemView.setSelected(selectedPos == position);
        if (holder.itemView.isSelected()) {
            // Cambia el aspecto para el estado seleccionado
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Ejemplo: fondo gris para seleccionado
        } else {
            // Aspecto normal
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); // Fondo transparente para no seleccionado
        }
    }

    // Retorna el tamaño de tu dataset (invocado por el layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    // Método para establecer la posición seleccionada
    public void setSelectedPos(int position) {
        if (selectedPos == position) {
            // Si el mismo elemento se selecciona nuevamente, deselecciona
            notifyItemChanged(selectedPos);
            selectedPos = RecyclerView.NO_POSITION;
        } else {
            // Cambio de selección
            int oldSelectedPos = selectedPos;
            selectedPos = position;
            notifyItemChanged(oldSelectedPos);
            notifyItemChanged(selectedPos);
        }
    }

    public Data getSelectedItem() {
        if (selectedPos != RecyclerView.NO_POSITION && selectedPos < mDataSet.size()) {
            return mDataSet.get(selectedPos);
        }
        return null;
    }

}


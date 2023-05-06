package com.plm.encaja;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



public class ChangePanelDimensionsDialog extends DialogFragment {
    private Double w;
    private Double h;
    public interface ChangePanelDimensionsDialogListener {
        void onPanelDimensionsChanged(double newWidth, double newHeight);
    }

    private ChangePanelDimensionsDialogListener listener;

    public ChangePanelDimensionsDialog(ChangePanelDimensionsDialogListener listener, Double w, Double h) {
        this.listener = listener;
        this.w=w;
        this.h=h;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_panel_dimensions, null);

        EditText widthInput = view.findViewById(R.id.width_input);
        EditText heightInput = view.findViewById(R.id.height_input);

        if(w!=null){
            widthInput.setText(Double.toString(w));
        }

        if(h!=null){
            heightInput.setText(Double.toString(h));
        }

        builder.setView(view)
                .setTitle("Cambiar dimensiones del panel")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double newWidth = Double.parseDouble(widthInput.getText().toString());
                        double newHeight = Double.parseDouble(heightInput.getText().toString());
                        listener.onPanelDimensionsChanged(newWidth, newHeight);
                    }
                })
                .setNegativeButton("Cancelar", null);

        return builder.create();
    }
}

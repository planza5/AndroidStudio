package com.plm.encaja;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ItemDimensionDialog extends DialogFragment {

    public interface ItemDimensionDialogListener {
        void onNewItem(ItemRectangle item);
        void onModifiedItem(ItemRectangle before,ItemRectangle after);
    }

    private ItemDimensionDialogListener listener;
    private ItemRectangle item;
    private NumberPicker numberUnit; // Nuevo widget

    public ItemDimensionDialog(ItemDimensionDialogListener listener, ItemRectangle item) {
        this.listener = listener;
        this.item = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_item_dimension, null);

        final EditText editTextWidth = view.findViewById(R.id.edit_width);
        final EditText editTextHeight = view.findViewById(R.id.edit_height);

        // Obtener una referencia al widget NumberPicker
        numberUnit = view.findViewById(R.id.number_unit);
        numberUnit.setMinValue(1);
        numberUnit.setMaxValue(99);
        if (item != null) {
            numberUnit.setValue(item.getUnits());
            editTextWidth.setText(String.valueOf(item.getDimension().getWidth()));
            editTextHeight.setText(String.valueOf(item.getDimension().getHeight()));
        }

        builder.setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int unit = numberUnit.getValue();

                        double width;
                        double height;

                        try {
                            width = Double.parseDouble(editTextWidth.getText().toString());
                            height = Double.parseDouble(editTextHeight.getText().toString());
                        }catch(Exception ex){
                            Utils.extracted(ItemDimensionDialog.this.getContext(),"Valor numérico no válido");
                            return;
                        }

                        if(item==null){
                            item = new ItemRectangle(unit, new Location(null,null),new Dimension(width, height));
                            listener.onNewItem(item);
                        }else{
                            ItemRectangle copy=item.copy();
                            item.setDimension(new Dimension(width,height));
                            item.setUnits(unit);
                            listener.onModifiedItem(copy,item);
                        }


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ItemDimensionDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }


}

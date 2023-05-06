package com.plm.tontipasswords;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.plm.tontipasswords.model.TontiReg;

import org.w3c.dom.Text;

import java.util.List;

public class RegAdapter extends ArrayAdapter<TontiReg> {
    private IRegEvents callback;
    private int selected=-1;
    private CustomFilter filter;

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }
    }

    public RegAdapter(@NonNull Context context, int resource, IRegEvents callback) {
        super(context, resource);
        this.callback=callback;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter();
        }

        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tonti_reg,parent, false);
        }



        TontiReg reg=getItem(position);

        EditText editName=convertView.findViewById(R.id.name);
        EditText editUser=convertView.findViewById(R.id.user);
        EditText editPassword=convertView.findViewById(R.id.password);
        EditText editComments=convertView.findViewById(R.id.comments);

        editName.setText(reg.name);
        editUser.setText(reg.user);
        editPassword.setText(reg.password);
        editComments.setText(reg.comments);

        addEvent2(convertView,editName,0,position);
        addEvent2(convertView,editUser,1,position);
        addEvent2(convertView,editPassword,2,position);
        addEvent2(convertView,editComments,3,position);


        return convertView;
    }



    public void addEvent2(View parent,final EditText edit, final int field, final int position){
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean in) {
                if(!in){
                    callback.changed(position,field,edit.getText().toString());
                    parent.setBackground(getContext().getDrawable(R.drawable.border_normal));
                }else{
                    parent.setBackground(getContext().getDrawable(R.drawable.border_selected));
                }
            }
        });
    }
}

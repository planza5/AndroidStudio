package com.plm.tontipasswords;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.plm.tontipasswords.model.TontiApp;
import com.plm.tontipasswords.model.TontiReg;
import com.plm.tontipasswords.utils.GsonUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements IRegEvents{
    private TontiApp app;
    private RegAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppData(this);

        adapter=new RegAdapter(this, R.layout.tonti_reg, this);

        for(TontiReg reg:app.tontiRegs){
            adapter.add(reg);
        }


        ListView list=findViewById(R.id.list);
        list.setAdapter(adapter);


    }


    protected void getAppData(Context context){
        try {
            app=GsonUtils.loadAll(new File(context.getFilesDir(),"test.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changed(int position, int field, String newField) {

        String oldField = null;

        switch(field){
            case 0:{
                oldField = app.tontiRegs.get(position).name;
                app.tontiRegs.get(position).name = newField;
                break;
            }
            case 1:{
                oldField = app.tontiRegs.get(position).user;
                app.tontiRegs.get(position).user = newField;
                break;
            }
            case 2:{
                oldField = app.tontiRegs.get(position).password;
                app.tontiRegs.get(position).password = newField;
                break;
            }
            case 3:{
                oldField = app.tontiRegs.get(position).comments;
                app.tontiRegs.get(position).comments = newField;
                break;
            }
        }

        if(!newField.equals(oldField)){
            Log.d("POLLAS2",newField);

            try {
                GsonUtils.saveAll(app,new File(this.getFilesDir(),"test.json"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selected(int position) {
            Log.d("POLLAS2",position+"");
            ListView list=findViewById(R.id.list);
    }

    public void remove(View v){
        Log.d("POLLAS2","r");
    }

    public void add(View v){
        Log.d("POLLAS2","a");
        TontiReg tr=new TontiReg();
        adapter.add(tr);
        app.tontiRegs.add(tr);

        try{
            GsonUtils.saveAll(app,new File(this.getFilesDir(),"test.json"));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }
}
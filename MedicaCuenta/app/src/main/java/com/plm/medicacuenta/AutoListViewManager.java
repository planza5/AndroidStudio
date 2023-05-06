package com.plm.medicacuenta;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AutoListViewManager {
    private String FILENAME="myfilename.json";
    private final String[] fields;
    private final Class theclass;
    private List list;
    private View view;

    public AutoListViewManager(Class theclass, String... fields){
        this.fields=fields;
        this.theclass=theclass;
        list = loadObjects();
    }


    public List loadObjects(){
        //TODO
        //cambiar por la obtencion a traves de JSON

        List list=new ArrayList();


        return list;
    }

    public void saveObjects(){
        //TODO
    }

    public void setList(List list){
        this.list=list;
    }

    public ListView getListView(ViewGroup parent,int id){
        MyAdapter adapter=new MyAdapter(parent);
        ListView listView=new ListView(parent.getContext());
        listView.setAdapter(adapter);

        return listView;
    }
    
    public void setView(View view){
        this.view=view;
    }

    public void setupListView(int listId, ViewGroup container) {
        ListView listView=container.findViewById(listId);
        MyAdapter adapter=new MyAdapter(listView);
        listView.setAdapter(adapter);
    }


    class MyAdapter extends BaseAdapter{
        private final View view;

        public MyAdapter(View view){
            this.view=view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LinearLayout ll=new LinearLayout(parent.getContext());
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.HORIZONTAL);

            Object obj=getItem(position);

            for(String fieldname:fields){
                try{
                    Field field=theclass.getDeclaredField(fieldname);
                    field.setAccessible(true);

                    if(field.getType()==boolean.class){
                        CheckBox cb=new CheckBox(parent.getContext());
                        cb.setChecked(field.getBoolean(obj));
                        ll.addView(cb);
                    }else{
                        TextView tv=new TextView(parent.getContext());
                        tv.setText(field.get(obj).toString());
                        tv.setWidth(700);
                        ll.addView(tv);
                    }

                    field.setAccessible(false);
                }catch(Exception ex){
                    ex.printStackTrace();
                }



            }

            return ll;
        }
    }



}

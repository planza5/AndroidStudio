package com.plm.encaja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemDimensionAdapter extends ArrayAdapter<ItemRectangle> {

    private final Context context;
    private final List<ItemRectangle> items;

    public ItemDimensionAdapter(Context context, List<ItemRectangle> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text1);
        ItemRectangle tr=getItem(position);
        String locx=tr.getLocation().getX()==null?"?":Double.toString(tr.getLocation().getX());
        String locy=tr.getLocation().getY()==null?"?":Double.toString(tr.getLocation().getY());

        textView.setText(tr.getUnits()+" de "+
                tr.getDimension().getWidth()+" x "+
                tr.getDimension().getHeight()+ " situado en "+
                locx+
                ","+
                locy);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getUnits();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public List<ItemRectangle> getItems() {
        return new ArrayList<>(this.items);
    }

    public void setItems(List<ItemRectangle> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

}

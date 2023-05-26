package com.plm.tt4;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> itemList;
    private final OnItemListener listener;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    interface OnItemListener{
        public void onClickDone(Item item);
        public void onClickMenu(Item item);
        public void onClickNext(Item item);

        public void onClickText(Item currentItem);

        void onClickMenuUp(int position);

        void onClickMenuDown(int position);

        void onClickDelete(int position);
    }



    public ItemAdapter(List<Item> itemList, OnItemListener listener) {
        this.itemList = itemList;
        this.listener=listener;
        sortItems(getItemList());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.itemDone.setChecked(item.isDone());

        //holder.itemDone.setVisibility(item.getChilds().isEmpty()?View.VISIBLE:View.INVISIBLE);
        holder.itemDone.setEnabled(item.getChilds().isEmpty());
        holder.itemDone.setAlpha(item.getChilds().isEmpty()?1f:0.2f);
        //ordenamos


        if(!item.getChilds().isEmpty()){
            holder.itemName.setTypeface(null, Typeface.BOLD);
            holder.itemName.setTextSize(15);
        }else{
            holder.itemName.setTypeface(null, Typeface.NORMAL);
            holder.itemName.setTextSize(14);
        }

        holder.itemName.setText(item.getName());
        holder.currentItem=itemList.get(position);


        holder.itemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, holder.getAdapterPosition());
            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public Item currentItem;
        CheckBox itemDone;
        TextView itemName;
        ImageButton itemMenu;
        ImageButton itemArrow;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDone = itemView.findViewById(R.id.itemDone);
            itemName = itemView.findViewById(R.id.itemName);
            itemMenu = itemView.findViewById(R.id.itemMenu);
            itemArrow = itemView.findViewById(R.id.itemArrow);

            itemArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickNext(currentItem);
                }
            });

            itemDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickDone(currentItem);
                }
            });

            itemMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickMenu(currentItem);
                }
            });

            itemName.setOnClickListener(new View.OnClickListener() {
                private long lastClickTime = 0;

                @Override
                public void onClick(View view) {
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < 300) { // Verifica si el intervalo entre clics es menor a 300 milisegundos (ajusta según tus necesidades)
                        listener.onClickText(currentItem);
                    }
                    lastClickTime = clickTime;
                }
            });

        }
    }

    public boolean areAllChildsDone(Item item) {
        for(Item i:item.getChilds()){
            if(!i.isDone()){
                return false;
            }
        }

        return true;
    }

    private void showPopupMenu(View anchorView, final int position) {
        PopupMenu popupMenu = new PopupMenu(anchorView.getContext(), anchorView);
        popupMenu.inflate(R.menu.item_popup_menu); // Infla el menú desde un archivo XML

        // Configura los clics de los elementos del menú
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Maneja los clics de los elementos del menú
                switch (item.getItemId()) {
                    case R.id.menu_item_up:
                        listener.onClickMenuUp(position);
                        return true;
                    case R.id.menu_item_down:
                        listener.onClickMenuDown(position);
                        return true;
                    case R.id.menu_item_delete:
                        listener.onClickDelete(position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Muestra el menú emergente
        popupMenu.show();
    }


    public void sortItems(List<Item> list){
       Collections.sort(list, new Comparator<Item>() {
           @Override
           public int compare(Item t1, Item t2) {
               if (t1.getChilds().isEmpty() && t2.getChilds().isEmpty()) {
                   return 0;
               } else if (t1.getChilds().isEmpty() && !t2.getChilds().isEmpty()) {
                   return +1;
               }

               return -1;
           };
       });
    }
}


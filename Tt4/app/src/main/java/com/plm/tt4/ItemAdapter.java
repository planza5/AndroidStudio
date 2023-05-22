package com.plm.tt4;

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

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

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

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList, OnItemListener listener) {
        this.itemList = itemList;
        this.listener=listener;
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
        holder.itemName.setText(item.getName());
        holder.currentItem=itemList.get(position);
        // handle itemMenu and itemArrow clicks here

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
                @Override
                public void onClick(View view) {
                    listener.onClickText(currentItem);
                }
            });
        }
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

}


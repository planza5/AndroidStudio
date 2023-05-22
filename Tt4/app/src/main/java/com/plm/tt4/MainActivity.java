package com.plm.tt4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemListener {
    private Item currrentItem=null;

    private RecyclerView itemRecyclerView;
    private List<Item> items; // tu lista de items
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgBtnBack = findViewById(R.id.img_btn_back);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currrentItem!=null && currrentItem.getParent()!=null){
                    onClickNext(currrentItem.getParent());
                }
            }
        });

        ImageButton imgBtnPlus = findViewById(R.id.img_btn_plus);
        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currrentItem!=null){
                    new Item(currrentItem,"New...",false);
                    itemAdapter.notifyDataSetChanged();
                    modifiedModel();
                }
            }
        });

        getSupportActionBar().setTitle("");

        //TODO: lo tiene que obtener del json
        currrentItem=GsonUtils.loadItemFromFile(this);


        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter=new ItemAdapter(currrentItem.getChilds(),this);
        itemRecyclerView.setAdapter(itemAdapter);
    }

    // reemplaza este método con tu código que obtiene la lista de items
    private List<Item> getItems() {
        Item root=new Item();

        Item i1=new Item(root,"Child 1",false);
        new Item(root,"Child 2", true);

        new Item(i1,"Subchild1",true);
        return root.getChilds();
    }


    @Override
    public void onClickDone(Item item) {
        item.setDone(!item.isDone());
        modifiedModel();
    }

    @Override
    public void onClickMenu(Item item) {
        //TODO popup
    }

    @Override
    public void onClickNext(Item item) {
        currrentItem = item;
        itemAdapter.setItemList(item.getChilds());
        itemAdapter.notifyDataSetChanged();

        //averigua titulo
        StringBuffer title=new StringBuffer(item.getName()==null?"":item.getName());

        Item currentItem=item.getParent();

        while(currentItem!=null && currentItem.getParent()!=null){
                title.insert(0, currentItem.getName()+" > ");
                currentItem=currentItem.getParent();
        }

        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onClickText(final Item currentItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item");

        final EditText input = new EditText(this);
        input.setText(currentItem.getName());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                currentItem.setName(newName);
                itemAdapter.notifyDataSetChanged();
                hideSoftKeyboard(input);
                modifiedModel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideSoftKeyboard(input);
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        final AlertDialog dialog = builder.create();

        // Show the soft keyboard when the dialog is shown
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                input.requestFocus();
                input.selectAll();
                showSoftKeyboard(input);
            }
        });

        dialog.show();
    }

    @Override
    public void onClickMenuUp(int position) {
        if(position>0){
            Item p1=itemAdapter.getItemList().get(position-1);
            Item p2=itemAdapter.getItemList().get(position);
            itemAdapter.getItemList().set(position-1,p2);
            itemAdapter.getItemList().set(position,p1);
            itemAdapter.notifyDataSetChanged();
            modifiedModel();
        }
    }

    @Override
    public void onClickMenuDown(int position) {
        if(position<itemAdapter.getItemCount()-1){
            Item p1=itemAdapter.getItemList().get(position);
            Item p2=itemAdapter.getItemList().get(position+1);
            itemAdapter.getItemList().set(position,p2);
            itemAdapter.getItemList().set(position+1,p1);
            itemAdapter.notifyDataSetChanged();
            modifiedModel();
        }
    }

    @Override
    public void onClickDelete(int position) {
        itemAdapter.getItemList().remove(position);
        itemAdapter.notifyDataSetChanged();
    }

    private void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void modifiedModel(){
        GsonUtils.saveItemToFile(this,getGrandParent());
    }

    private Item getGrandParent() {
        Item grandParent=currrentItem;

        while(grandParent.getParent()!=null){
            grandParent=grandParent.getParent();
        }

        return grandParent;
    }


}

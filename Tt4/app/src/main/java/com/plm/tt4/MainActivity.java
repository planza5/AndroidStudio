package com.plm.tt4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.OnItemListener {

    private static final int READ_REQUEST_CODE = 2;
    private Item currentItem =null;

    private RecyclerView itemRecyclerView;
    private List<Item> items; // tu lista de items
    private ItemAdapter itemAdapter;
    private TextView pathTxt;
    private static final int WRITE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgBtnBack = findViewById(R.id.img_btn_back);
        ImageButton imgBtnExport = findViewById(R.id.img_btn_export);
        ImageButton imgBtnImport = findViewById(R.id.img_btn_import);

        pathTxt = findViewById(R.id.pathTxt);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem !=null && currentItem.getParent()!=null){
                    onClickNext(currentItem.getParent());
                }
            }
        });

        ImageButton imgBtnPlus = findViewById(R.id.img_btn_plus);
        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentItem !=null){
                    new Item(currentItem,"New...",false);
                    modifiedModel();
                    itemAdapter.notifyDataSetChanged();

                }
            }
        });

        imgBtnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                intent.putExtra(Intent.EXTRA_TITLE, "data.json");
                startActivityForResult(intent, WRITE_REQUEST_CODE);
            }
        });

        imgBtnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });

// Manejo de la respuesta del Intent



        //getSupportActionBar().setTitle("");

        //TODO: lo tiene que obtener del json
        currentItem =GsonUtils.loadItemFromFile(this, Ctes.ITEM_FILE_NAME);


        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter=new ItemAdapter(currentItem.getChilds(),this);
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


    private void modifyItemsUntilRoot(Item item){
        if(item!=null && item.getParent()!=null){
            if(itemAdapter.areAllChildsDone(item.getParent())){
                item.getParent().setDone(true);
            }else{
                item.getParent().setDone(false);
            }

        modifyItemsUntilRoot(item.getParent());
        }


    }

    @Override
    public void onClickDone(Item item) {
        item.setDone(!item.isDone());
        modifyItemsUntilRoot(item);
        modifiedModel();
    }

    @Override
    public void onClickMenu(Item item) {
        //TODO popup
    }

    @Override
    public void onClickNext(Item item) {
        currentItem = item;
        itemAdapter.sortItems(item.getChilds());
        itemAdapter.setItemList(item.getChilds());
        itemAdapter.notifyDataSetChanged();

        //averigua titulo
        StringBuffer title=new StringBuffer(item.getName()==null?"":item.getName());

        Item currentItem=item.getParent();

        while(currentItem!=null && currentItem.getParent()!=null){
            title.insert(0, currentItem.getName()+" > ");
            currentItem=currentItem.getParent();
        }


        pathTxt.setText(title.toString());
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
        if(position>0 && itemAdapter.getItemList().get(position-1).getChilds().isEmpty()==itemAdapter.getItemList().get(position).getChilds().isEmpty()){
            Item p1=itemAdapter.getItemList().get(position-1);
            Item p2=itemAdapter.getItemList().get(position);
            itemAdapter.getItemList().set(position-1,p2);
            itemAdapter.getItemList().set(position,p1);
            //itemAdapter.sortItems(itemAdapter.getItemList());
            itemAdapter.notifyDataSetChanged();
            modifiedModel();
        }
    }

    @Override
    public void onClickMenuDown(int position) {
        if(position<itemAdapter.getItemCount()-1 && itemAdapter.getItemList().get(position+1).getChilds().isEmpty()==itemAdapter.getItemList().get(position).getChilds().isEmpty()){
            Item p1=itemAdapter.getItemList().get(position);
            Item p2=itemAdapter.getItemList().get(position+1);
            itemAdapter.getItemList().set(position,p2);
            itemAdapter.getItemList().set(position+1,p1);
            itemAdapter.sortItems(itemAdapter.getItemList());
            itemAdapter.notifyDataSetChanged();
            modifiedModel();
        }
    }

    @Override
    public void onClickDelete(int position) {
        itemAdapter.getItemList().remove(position);
        modifiedModel();
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
        GsonUtils.saveItemToFile(this,getGrandParent(), Ctes.ITEM_FILE_NAME);
    }

    private Item getGrandParent() {
        Item grandParent= currentItem;

        while(grandParent.getParent()!=null){
            grandParent=grandParent.getParent();
        }

        return grandParent;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode,resultCode,resultData);

        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                String str=uri.toString();
                GsonUtils.saveItemToFile(this,getGrandParent(),resultData.getData().getEncodedPath());
                // Puedes abrir el archivo para escribir con uri.
                // Escribir el JSON de los Items en este archivo.
            }
        }else  if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                currentItem = GsonUtils.loadItemFromFile(this,uri.getEncodedPath());
                modifiedModel();
                itemAdapter.notifyDataSetChanged();
                // Puedes abrir el archivo para leer con uri.
                // Leer el JSON de los Items de este archivo.
            }
        }
    }

}

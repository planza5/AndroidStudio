package com.plm.encaja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemDimensionDialog.ItemDimensionDialogListener, ChangePanelDimensionsDialog.ChangePanelDimensionsDialogListener {

    private DrawingPanel drawingPanel;
    private ListView listView;
    private Button buttonAdd;
    private Button buttonRemove;
    private Button buttonEdit;

    private int selectedPosition = -1;

    private ItemDimensionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el panel de dibujo
        FrameLayout drawingPanelContainer = findViewById(R.id.drawing_panel_container);
        drawingPanel = new DrawingPanel(this, 244, 60);
        drawingPanelContainer.addView(drawingPanel);

        // Crear datos de ejemplo para el ListView
        List<ItemRectangle> items = new ArrayList<>();
        items.add(new ItemRectangle(1, new Location(null,null),new Dimension(10, 20)));
        items.add(new ItemRectangle(2, new Location(null,null),new Dimension(30, 40)));
        items.add(new ItemRectangle(3, new Location(null,null),new Dimension(50, 60)));

        // Inicializar el ListView
        listView = findViewById(R.id.list_view);
        adapter = new ItemDimensionAdapter(this, items);
        listView.setAdapter(adapter);

        // Agregar controlador de eventos para la selección de un elemento del ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Desmarcar el elemento previamente seleccionado
                if (selectedPosition != -1) {
                    View previousSelectedItem = listView.getChildAt(selectedPosition - listView.getFirstVisiblePosition());

                    if(previousSelectedItem!=null) {
                        previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                // Marcar el elemento seleccionado actualmente
                selectedPosition = position;
                view.setBackgroundColor(Color.LTGRAY);
            }
        });

        // Inicializar los botones
        buttonAdd = findViewById(R.id.button_add);
        buttonRemove = findViewById(R.id.button_remove);
        buttonEdit = findViewById(R.id.button_edit);

        // Agregar controladores de eventos para los botones
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDimensionDialog dialog = new ItemDimensionDialog(MainActivity.this, null);
                dialog.show(getSupportFragmentManager(), "Añadir");
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition>=0) {
                    adapter.remove(adapter.getItem(selectedPosition));
                    adapter.notifyDataSetChanged();
                    selectedPosition = -1;

                    try {
                        repack();
                    } catch (NoSpaceException e) {
                        //do noyhing
                    }
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition>=0) {
                    ItemDimensionDialog dialog = new ItemDimensionDialog(MainActivity.this, adapter.getItem(selectedPosition));
                    dialog.show(getSupportFragmentManager(), "Editar");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            repack();
        } catch (NoSpaceException e) {
            throw new RuntimeException(e);
        }
    }

    private void repack() throws NoSpaceException{
        // Obtener la lista de rectángulos del adaptador
        ArrayList<ItemRectangle> rectanglesToPack = expandRectangles(new ArrayList<>(adapter.getItems()));

        // Llamar a la función packRectangles con los parámetros necesarios
        ArrayList<ItemRectangle> packedRectangles = null;

        try {
            packedRectangles = PackingSolver.packRectangles(rectanglesToPack, drawingPanel.getTotalWidth(), drawingPanel.getTotalHeight());
            // Actualizar la lista de rectángulos en el adaptador con las nuevas ubicaciones
            //adapter.setItems(packedRectangles);

            // Notificar al adaptador que los datos han cambiado para actualizar la vista
            adapter.notifyDataSetChanged();

            // Actualizar el panel de dibujo con los rectángulos empaquetados
            drawingPanel.setRectangles(packedRectangles);
            drawingPanel.invalidate();
        } catch (NoSpaceException e) {
            Utils.extracted(this,"Las superficies no caben en "+drawingPanel.getTotalWidth()+" x "+drawingPanel.getTotalHeight());
        }

    }

    @Override
    public void onNewItem(ItemRectangle item) {
        adapter.add(item);
        try {
            repack();
        } catch (NoSpaceException e) {
            adapter.remove(item);
        }
    }

    @Override
    public void onModifiedItem(ItemRectangle before,ItemRectangle after) {
        adapter.notifyDataSetChanged();

        try {
            repack();
        } catch (NoSpaceException e) {
            after=before;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.m1){
            ChangePanelDimensionsDialog dialog = new ChangePanelDimensionsDialog(this, drawingPanel.getTotalWidth(), drawingPanel.getTotalHeight());
            dialog.show(getSupportFragmentManager(), "change_dimensions");

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPanelDimensionsChanged(double newWidth, double newHeight) {
        drawingPanel.setTotalWidth(newWidth);
        drawingPanel.setTotalHeight(newHeight);
    }

    private ArrayList<ItemRectangle> expandRectangles(List<ItemRectangle> inputRectangles) {
        ArrayList<ItemRectangle> expandedRectangles = new ArrayList<>();
        for (ItemRectangle rectangle : inputRectangles) {
            for (int i = 0; i < rectangle.getUnits(); i++) {
                expandedRectangles.add(new ItemRectangle(1,rectangle.getLocation(),rectangle.getDimension()));
            }
        }
        return expandedRectangles;
    }

}

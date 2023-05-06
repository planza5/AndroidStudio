package com.plm.medicacuenta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.plm.medicacuenta.beans.*;
import com.plm.medicacuenta.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PABLO";
    private static String startDate="10/02/2023";
    private static String endDate="23/02/2023";
    private List<Medicamento> medicamentos=new ArrayList();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startEdit.addTextChangedListener(new TextChangedAdapter() {
            @Override
            public void afterTextChanged(String text) {
                processDates(text, binding.endEdit.getText().toString());
            }
        });

        binding.endEdit.addTextChangedListener(new TextChangedAdapter() {
            @Override
            public void afterTextChanged(String text) {
                processDates(binding.startEdit.getText().toString(), text);
            }
        });

        binding.startEdit.setText("05/01/2023");
        binding.endEdit.setText("26/01/2023");

        medicamentos.add(new MedicamentoPorDia("Micofelanato (2+2)",4));
        medicamentos.add(new MedicamentoPorDia("Advagraf (2)",2));
        medicamentos.add(new MedicamentoPorDia("Omeoprazol (1)",1));
        medicamentos.add(new MedicamentoPorDia("Enalapril (1)",1));
        medicamentos.add(new MedicamentoAlterno("Prednisona (1 - 0.5)",1f, 0.5f));
        medicamentos.add(new MedicamentoPorDia("Ostine (1+1)",1));

        medicamentos.add(new MedicamentoPorDiasSemana("Azitromizina (LXV)","LXV",1));
        medicamentos.add(new MedicamentoPorDia("Septrin (1)", 1));

        medicamentos.add(new MedicamentoPorDia("Zinc (1+1)",2));
        medicamentos.add(new MedicamentoPorDia("Atozet (1)",1));

        medicamentos.add(new MedicamentoPorDia("Magnesioboi (2+2)",6));
        medicamentos.add(new MedicamentoUnoPorCadaNumDias("Test",1,7));

        processDates(startDate,endDate);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        processDates(binding.startEdit.toString(),binding.endEdit.toString());
    }

    private void processDates(String startDate, String endDate){
        if(TimeUtils.areValidDates(startDate,endDate)){
            List<MedicamentoResult> results=new ArrayList<>();

            for(Medicamento one:medicamentos){;
                results.add(new MedicamentoResult(one.getName(), one.calculateTotalAmount(startDate,endDate)));
            }

            AutoListViewManager listViewManager=new AutoListViewManager(MedicamentoResult.class,"name","quantity");
            listViewManager.setList(results);
            listViewManager.setupListView(binding.theList.getId(),binding.getRoot());
        }
    }
}
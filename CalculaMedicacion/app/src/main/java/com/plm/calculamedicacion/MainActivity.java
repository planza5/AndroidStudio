package com.plm.calculamedicacion;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.plm.calculamedicacion.auto.AutoListViewManager;
import com.plm.calculamedicacion.auto.AutoListFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private List<Medicamento> medicamentos=new ArrayList();
    private ViewPager2 viewPager2;
    private AutoListFragmentStateAdapter pagerAdapter;
    private AutoListViewManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        medicamentos.add(new MedicamentoPorDia("Micofelanato",4));
        medicamentos.add(new MedicamentoPorDia("Advagraf",2));
        medicamentos.add(new MedicamentoPorDia("Omeoprazol",1));
        medicamentos.add(new MedicamentoPorDia("Enalapril",1));
        medicamentos.add(new MedicamentoAlterno("Prednisona",1f, 0.5f));
        medicamentos.add(new MedicamentoPorDia("Ostine",1));

        medicamentos.add(new MedicamentoPorDiasSemana("Azitromizina","LXV",1));
        medicamentos.add(new MedicamentoPorDia("Septrin", 1));

        medicamentos.add(new MedicamentoPorDia("Zinc",2));
        medicamentos.add(new MedicamentoPorDia("Atozet",1));

        medicamentos.add(new MedicamentoPorDia("Magnesioboi",6));
        medicamentos.add(new MedicamentoUnoPorCadaNumDias("Test",1,7));

        viewPager2 = (ViewPager2)findViewById(R.id.pager);

        lm=new AutoListViewManager(Person.class,"name","surname","age","dead");
        AutoListFragmentStateAdapter pagerAdapter = new AutoListFragmentStateAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setCurrentItem(0);

        TabLayout tablayout = findViewById(R.id.tablayout);
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tablayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
     }


}
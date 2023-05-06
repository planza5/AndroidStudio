package com.plm.calculamedicacion;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MedicacionUnitsTest {
    private Date date1,date2;

    @Before
    public void initDates() {
        try {
            date1= new SimpleDateFormat("dd/MM/yyyy").parse("04/12/2022");
            date2= new SimpleDateFormat("dd/MM/yyyy").parse("22/12/2022");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testMedicamentosPorDia(){
        MedicamentoPorDia mpd=new MedicamentoPorDia("test",2);
        float m=mpd.calculateTotalAmount(date1,date2);
        assertEquals(38f,m,0);
    }

    @Test
    public void testMedicamentosAlterno(){
        MedicamentoAlterno ma=new MedicamentoAlterno("test",1,0.5f);
        float m= ma.calculateTotalAmount(date1,date2);
        assertEquals(14.5,m,0);

        ma=new MedicamentoAlterno("test",0.5f,1);
        m= ma.calculateTotalAmount(date1,date2);
        assertEquals(14.5,m,0);
    }

    @Test
    public void testMedicamentosPorDiasSemana(){
        MedicamentoPorDiasSemana mpds=new MedicamentoPorDiasSemana("test","LMX",1);
        float m=mpds.calculateTotalAmount(date1,date2);
        assertEquals(9,m,0);
    }
}
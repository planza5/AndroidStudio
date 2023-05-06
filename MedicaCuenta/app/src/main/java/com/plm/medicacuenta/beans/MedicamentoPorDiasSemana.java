package com.plm.medicacuenta.beans;

import java.util.Calendar;
import java.util.Date;

public class MedicamentoPorDiasSemana extends Medicamento {
    private String week;
    private float amount;

    public MedicamentoPorDiasSemana(String name, String week, float amount){
        this.name=name;
        this.week=week;
        this.amount=amount;
    }

    @Override
    public float calculateTotalAmount(Date startDay, Date endDay) {
        int amountAcum=0;
        String initial=null;

        while (startDay.before(endDay)) {
            initial=getInitialDay(startDay);

            if(week.contains(initial)){
                amountAcum+=this.amount;
            }


            startDay.setTime(startDay.getTime()+(24*60*60*1000));
        }

        initial=getInitialDay(startDay);

        if(week.contains(initial)){
            amountAcum+=this.amount;
        }

        return amountAcum;
    }

    private String getInitialDay(Date date){
        Calendar day=Calendar.getInstance();
        day.setTime(date);

        String initial=null;

        switch (day.get(Calendar.DAY_OF_WEEK)){
            case 1:
                initial="D";
                break;
            case 2:
                initial="L";
                break;
            case 3:
                initial="M";
                break;
            case 4:
                initial="X";
                break;
            case 5:
                initial="J";
                break;
            case 6:
                initial="V";
                break;
            case 7:
                initial="S";
                break;
        }

        return initial;
    }
}

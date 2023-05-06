package com.plm.medicacuenta.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Medicamento {
    private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    protected String name;
    public abstract float calculateTotalAmount(Date startDay, Date endDay);

    public Float calculateTotalAmount(String startDay, String endDay){
        Float amount=null;

        try {
            amount= calculateTotalAmount(sdf.parse(startDay),sdf.parse(endDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return amount;
    }

    public String getName(){
        return name;
    }
}

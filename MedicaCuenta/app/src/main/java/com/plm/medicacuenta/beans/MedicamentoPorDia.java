package com.plm.medicacuenta.beans;

import java.util.Date;

public class MedicamentoPorDia extends Medicamento {
    private int amount;

    public MedicamentoPorDia(String name, int amount){
        this.name=name;
        this.amount=amount;
    }

    @Override
    public float calculateTotalAmount(Date startDay, Date endDay) {
        int days= TimeUtils.getDays(startDay,endDay);
        return amount * days;
    }
}

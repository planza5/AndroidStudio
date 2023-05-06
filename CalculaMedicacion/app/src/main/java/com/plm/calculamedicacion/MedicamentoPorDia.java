package com.plm.calculamedicacion;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MedicamentoPorDia extends Medicamento {
    private int amount;

    public MedicamentoPorDia(String name, int amount){
        this.name=name;
        this.amount=amount;
    }

    @Override
    public float calculateTotalAmount(Date startDay, Date endDay) {
        int days=Utils.getDays(startDay,endDay);
        return amount * days;
    }
}

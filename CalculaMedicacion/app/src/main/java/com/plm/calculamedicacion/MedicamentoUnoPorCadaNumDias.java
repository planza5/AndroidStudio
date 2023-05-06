package com.plm.calculamedicacion;

import java.util.Date;

public class MedicamentoUnoPorCadaNumDias extends Medicamento {
    private int days;
    private float amount;



    public MedicamentoUnoPorCadaNumDias(String name, float amount, int days) {
        this.name=name;
        this.amount=amount;
        this.days=days;
    }

    @Override
    public float calculateTotalAmount(Date startDay, Date endDay) {
        float acumAmount=1;
        float countDays=0;

        while (startDay.before(endDay)) {
            countDays++;

            if(countDays==days) {
                acumAmount += amount;
                countDays=0;
            }

            startDay.setTime(startDay.getTime()+(24*60*60*1000));
        }

        return acumAmount;
    }
}

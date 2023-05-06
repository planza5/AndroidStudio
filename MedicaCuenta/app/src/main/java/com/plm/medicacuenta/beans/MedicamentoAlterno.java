package com.plm.medicacuenta.beans;

import java.util.Date;

public class MedicamentoAlterno extends Medicamento {
    private float amount1;
    private float amount2;

    public MedicamentoAlterno(String name, float amount1, float amount2){
        this.name=name;
        this.amount1=amount1;
        this.amount2=amount2;

        if(amount1<amount2){
            float saveme=amount1;
            saveme=amount1;
            this.amount1=amount2;
            this.amount2=saveme;
        }
    }

    @Override
    public float calculateTotalAmount(Date startDay, Date endDay) {
        int days= TimeUtils.getDays(startDay,endDay);

        return days/2 * amount1 + days/2 * amount2 + days%2 * amount1;
    }
}

package com.plm.dataintime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    private final static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    public Data(String date, float fev1Cvf, float fev1, float cvf, float fev) throws ParseException {
        this.date = sdf.parse(date);
        this.fev1Cvf = fev1Cvf;
        this.fev1 = fev1;
        this.cvf = cvf;
        this.fev = fev;
    }

    private Date date;
    private float fev1Cvf;
    private float fev1;
    private float cvf;
    private float fev;

    public Date getDate() {
        return date;
    }

    public String getStringDate(){
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date) throws ParseException {
        this.date=sdf.parse(date);
    }

    public float getFev1Cvf() {
        return fev1Cvf;
    }

    public void setFev1Cvf(float fev1Cvf) {
        this.fev1Cvf = fev1Cvf;
    }

    public float getFev1() {
        return fev1;
    }

    public void setFev1(float fev1) {
        this.fev1 = fev1;
    }

    public float getCvf() {
        return cvf;
    }

    public void setCvf(float cvf) {
        this.cvf = cvf;
    }

    public float getFev() {
        return fev;
    }

    public void setFev(float fev) {
        this.fev = fev;
    }
}

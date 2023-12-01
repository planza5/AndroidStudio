package com.plm.dataintime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    private final static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yy");
    private Date date;
    private float fev1Cvf;
    private int fev1;
    private int fev1Tpc;
    private int cvf;
    private int cvfTpc;
    private int fev2575;
    private int fev2575Tpc;





    public Data(String date, float fev1Cvf, int fev1, int fev1Tpc, int cvf, int cvfTpc, int fev2575, int fev2575Tpc) throws ParseException {
        this.date = sdf.parse(date);
        this.fev1Cvf = fev1Cvf;
        this.fev1 = fev1;
        this.fev1Tpc = fev1Tpc;
        this.cvf = cvf;
        this.cvfTpc = cvfTpc;
        this.fev2575 = fev2575;
        this.fev2575Tpc = fev2575Tpc;
    }

    public Data(Date date, float fev1Cvf, int fev1, int fev1Tpc, int cvf, int cvfTpc, int fev2575, int fev2575Tpc) throws ParseException {
        this.date = date;
        this.fev1Cvf = fev1Cvf;
        this.fev1 = fev1;
        this.fev1Tpc = fev1Tpc;
        this.cvf = cvf;
        this.cvfTpc = cvfTpc;
        this.fev2575 = fev2575;
        this.fev2575Tpc = fev2575Tpc;
    }

    public static SimpleDateFormat getSdf() {
        return sdf;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getFev1Cvf() {
        return fev1Cvf;
    }

    public void setFev1Cvf(float fev1Cvf) {
        this.fev1Cvf = fev1Cvf;
    }

    public int getFev1() {
        return fev1;
    }

    public void setFev1(int fev1) {
        this.fev1 = fev1;
    }

    public int getFev1Tpc() {
        return fev1Tpc;
    }

    public void setFev1Tpc(int fev1Tpc) {
        this.fev1Tpc = fev1Tpc;
    }

    public int getCvf() {
        return cvf;
    }

    public void setCvf(int cvf) {
        this.cvf = cvf;
    }

    public int getCvfTpc() {
        return cvfTpc;
    }

    public void setCvfTpc(int cvfTpc) {
        this.cvfTpc = cvfTpc;
    }

    public int getFev2575() {
        return fev2575;
    }

    public void setFev2575(int fev2575) {
        this.fev2575 = fev2575;
    }

    public int getFev2575Tpc() {
        return fev2575Tpc;
    }

    public void setFev2575Tpc(int fev2575Tpc) {
        this.fev2575Tpc = fev2575Tpc;
    }

    public String getStringDate() {
        return sdf.format(date);
    }
}

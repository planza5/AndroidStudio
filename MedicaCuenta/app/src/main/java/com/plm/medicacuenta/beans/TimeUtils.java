package com.plm.medicacuenta.beans;

import com.plm.medicacuenta.MedicamentoResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class TimeUtils {
    private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");

    public static LocalDate toLocalDate(Date startDay) {
        return startDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static int getDays(Date d1, Date d2){
        return (int)ChronoUnit.DAYS.between(toLocalDate(d1),toLocalDate(d2)) +1;
    }

    public static boolean areValidDates(String startDate, String endDate) {
        try {
           Date d1= sdf.parse(startDate);
           Date d2= sdf.parse(endDate);

           if(d1.after(d2)){
               return false;
           }
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}

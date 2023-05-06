package com.plm.calculamedicacion;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Utils {
    public static LocalDate toLocalDate(Date startDay) {
        return startDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static int getDays(Date d1, Date d2){
        return (int)ChronoUnit.DAYS.between(toLocalDate(d1),toLocalDate(d2)) +1;
    }
}

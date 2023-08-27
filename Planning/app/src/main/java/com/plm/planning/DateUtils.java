package com.plm.planning;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {


    public static final Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Agrega 1 día al calendario actual
        return calendar.getTime();
    }

    public static final String getWeekDayInitial(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "L";
            case 2:
                return "M";
            case 3:
                return "X";
            case 4:
                return "J";
            case 5:
                return "V";
            case 6:
                return "S";
            case 7:
                return "D";
        }

        return "?";
    }

    public static final Date cloneDate(Date date) {
        return new Date(date.getTime());
    }

    public static Date makeDate(String date) throws ParseException {
        return Ctes.dateFormat.parse(date);
    }

    public static Date addDays(Date initViewDate, int xcells) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(initViewDate);
        cal.add(Calendar.DAY_OF_MONTH,xcells);

        return cal.getTime();
    }

    public static boolean isLastDayOfMonth(Date date) {
        // Convierte java.util.Date a java.time.LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Encuentra el último día del mes para la fecha convertida
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());

        // Compara la fecha convertida con el último día del mes
        return localDate.equals(lastDayOfMonth);
    }
}

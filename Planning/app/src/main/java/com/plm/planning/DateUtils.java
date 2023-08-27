package com.plm.planning;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Calendar;
import java.util.Date;

public class Utils {


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

}

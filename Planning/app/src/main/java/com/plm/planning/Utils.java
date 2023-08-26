package com.plm.planning;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Paint gridPaint;
    public static Paint paintDayNumberRed;
    public static Paint paintMonthName;
    public static Paint paintDayNumberBlack;

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Agrega 1 día al calendario actual
        return calendar.getTime();
    }

    static{
        gridPaint = new Paint();
        gridPaint.setColor(Color.BLUE);
        gridPaint.setStrokeWidth(0.5f);
        gridPaint.setStyle(Paint.Style.STROKE);


        paintDayNumberRed = new Paint();
        paintDayNumberRed.setTextSize(35);
        paintDayNumberRed.setColor(Color.RED);

        paintMonthName = new Paint();
        paintMonthName.setTextSize(45);
        paintMonthName.setColor(Color.BLACK);

        paintDayNumberBlack = new Paint();
        paintDayNumberBlack.setTextSize(35);
        paintDayNumberBlack.setColor(Color.BLACK);
    }
}

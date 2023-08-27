package com.plm.planning;

import android.graphics.Color;
import android.graphics.Paint;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Ctes {
    public final static int TOP_MARGIN = 100;
    public final static int TOP_MARGIN_DAY = 40;
    public final static int TOP_MARGIN_MONTH = 100;
    public final static int BOTTOM_MARGIN = 100;
    public final static int COLUMNS = 25;
    public final static int MARGIN_RIGHT = 100;
    public final static int MARGIN_LEFT = 100;
    public final static Paint gridPaint;
    public final static Paint paintDayNumberRed;
    public final static Paint paintMonthName;
    public final static Paint paintDayNumberBlack;
    public final static SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    static{
        gridPaint = new Paint();
        gridPaint.setColor(Color.BLUE);
        gridPaint.setStrokeWidth(0.5f);
        gridPaint.setStyle(Paint.Style.STROKE);


        paintDayNumberRed = new Paint();
        paintDayNumberRed.setTextSize(35);
        paintDayNumberRed.setColor(Color.RED);

        paintMonthName = new Paint();
        paintMonthName.setTextSize(50);
        paintMonthName.setColor(Color.BLACK);

        paintDayNumberBlack = new Paint();
        paintDayNumberBlack.setTextSize(35);
        paintDayNumberBlack.setColor(Color.BLACK);
    }
}

package com.plm.planning;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomView extends View {


    private int columns=25;
    private Date date=new Date();
    private int screenWidth;
    private int screenHeight;
    private int interval;
    private int xcells,ycells;

    private int marginDay =40;
    private int marginMonth =80;


    public CustomView(Activity context) {
        super(context);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        screenWidth = getWidth(); // Ancho de la pantalla
        screenHeight = getHeight(); // Alto de la pantalla

        // Espacio entre las líneas
        interval = screenWidth / columns;
        xcells = screenWidth / interval + 1;
        ycells = (screenHeight - marginDay - marginMonth)/interval + 1;

        drawGrid(canvas);
        drawDates(canvas);
        drawMonthNames(canvas);

    }

    private void drawDates(Canvas canvas) {
        Date dateTemp=cloneDate(date);

        for(int col = 0; col< xcells; col++){
            drawDateDay(canvas,col, dateTemp);
            dateTemp=Utils.getNextDay(cloneDate(dateTemp));
        }
    }

    private Date cloneDate(Date date) {
        return new Date(date.getTime());
    }

    private void drawDateDay(Canvas canvas,int col, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfMonth=getWeekDayInitial(date)+calendar.get(Calendar.DAY_OF_MONTH);
        int x= (int) (col*interval+interval/2- Utils.paintDayNumberBlack.measureText(dayOfMonth)/2);
        //canvas.drawRect(col*interval,0,interval,marginTop,gridPaintOrange);
        canvas.drawText(dayOfMonth,x,marginMonth+marginDay/2, calendar.get(Calendar.DAY_OF_WEEK)==6 ||  calendar.get(Calendar.DAY_OF_WEEK)==7? Utils.paintDayNumberRed : Utils.paintDayNumberBlack);

    }

    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());

    private void drawMonthNames(Canvas canvas){
        Date dateTemp=cloneDate(date);

        List<String> months=new ArrayList();
        Calendar cal=Calendar.getInstance();


        for(int col=0;col<xcells;col++){
            cal.setTime(dateTemp);
            int month=cal.get(Calendar.DAY_OF_MONTH);

            if(month==1){
                String monthName=monthFormat.format(cal.getTime());
                Paint.FontMetrics fm = Utils.paintMonthName.getFontMetrics();
                float y = marginMonth / 2 - (fm.ascent + fm.descent) / 2;

                canvas.drawText(Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1),col*interval+interval/3,y, Utils.paintMonthName);
                canvas.drawLine(col*interval,0,col*interval,screenHeight,Utils.paintMonthName);
            }

            dateTemp=Utils.getNextDay(dateTemp);
        }
    }

    private String getWeekDayInitial(Date date) {
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

    private void drawGrid(Canvas canvas) {

        for (int i = 0; i < xcells; i++) {
            int x = i * interval;
            canvas.drawLine(x, marginMonth, x, screenHeight, Utils.gridPaint);
        }

        for (int i = 0; i < ycells; i++) {
            int y = i * interval;
            canvas.drawLine(0, y+ marginDay+ marginMonth, screenWidth, y+ marginDay + marginMonth, Utils.gridPaint);
        }
    }
}

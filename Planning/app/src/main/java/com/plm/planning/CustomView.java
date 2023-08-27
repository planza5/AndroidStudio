package com.plm.planning;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomView extends View {



    private Date initViewDate =new Date();
    private Date endViewDate;
    private int screenWidth;
    private int screenHeight;
    private int interval;
    private int xcells,ycells;




    @SuppressLint("ClickableViewAccessibility")
    public CustomView(Activity context) {
        super(context);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.setOnTouchListener(new View.OnTouchListener(){
            private float initialX, initialY;
            private boolean isDragging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Registrar coordenadas iniciales
                        initialX = event.getX();
                        initialY = event.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Determinar si se ha movido lo suficiente para considerarlo un arrastre
                        float distX = initialX - event.getX();
                        float distY = initialY - event.getY();

                        if (!isDragging) {
                            if (Math.abs(initialX - event.getX()) > interval || Math.abs(initialY - event.getY()) > interval) {
                                isDragging = true;

                                // Lógica adicional para cuando inicia el arrastre
                            }
                        }else{
                            int numDaysMoved = (int) distX/interval;
                            initViewDate = DateUtils.addDays(initViewDate,numDaysMoved);

                            CustomView.this.invalidate();
                        }

                        return isDragging;

                    case MotionEvent.ACTION_UP:
                        if (isDragging) {
                            // Coordenadas finales
                            float endX = event.getX();
                            float endY = event.getY();

                            // Realizar la lógica de soltura aquí
                            // ...

                            isDragging = false;
                            return true;
                        }

                    default:
                        return false;
                }
            }
        });

    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(screenWidth==0){
            screenWidth = getWidth(); // Ancho de la pantalla
            screenHeight = getHeight(); // Alto de la pantalla

            // Espacio entre las líneas
            interval = (screenWidth - Ctes.MARGIN_RIGHT - Ctes.MARGIN_LEFT)/ Ctes.COLUMNS;
            xcells = (screenWidth - Ctes.MARGIN_RIGHT - Ctes.MARGIN_LEFT) / interval ;
            ycells = (screenHeight - Ctes.TOP_MARGIN_DAY - Ctes.TOP_MARGIN_MONTH)/interval;
            endViewDate = DateUtils.addDays(initViewDate,xcells);
        }

        drawGrid(canvas);
        drawDates(canvas);
        drawMonthNames(canvas);

    }

    private void drawDates(Canvas canvas) {
        Date dateTemp= DateUtils.cloneDate(initViewDate);

        for(int col = 0; col< xcells; col++){
            drawDateDay(canvas,col, dateTemp);
            dateTemp= DateUtils.getNextDay(dateTemp);
        }
    }


    private void drawDateDay(Canvas canvas,int col, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfMonth= DateUtils.getWeekDayInitial(date)+calendar.get(Calendar.DAY_OF_MONTH);
        int x= (int) (Ctes.MARGIN_LEFT + col*interval+interval/2- Ctes.paintDayNumberBlack.measureText(dayOfMonth)/2);
        //canvas.drawRect(col*interval,0,interval,marginTop,gridPaintOrange);
        canvas.drawText(dayOfMonth,x,Ctes.TOP_MARGIN + Ctes.TOP_MARGIN_MONTH +Ctes.TOP_MARGIN_DAY/2, calendar.get(Calendar.DAY_OF_WEEK)==6 ||  calendar.get(Calendar.DAY_OF_WEEK)==7? Ctes.paintDayNumberRed : Ctes.paintDayNumberBlack);

    }



    private void drawMonthNames(Canvas canvas) {
        Date dateTemp = DateUtils.cloneDate(initViewDate);

        List<String> months = new ArrayList();
        Calendar cal = Calendar.getInstance();


        for (int col = 0; col < xcells; col++) {
            cal.setTime(dateTemp);
            int month = cal.get(Calendar.DAY_OF_MONTH);

            if (month == 1) {
                String monthName = Ctes.monthFormat.format(cal.getTime());
                monthName = Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1);

                Paint.FontMetrics fm = Ctes.paintMonthName.getFontMetrics();
                float y = Ctes.TOP_MARGIN + Ctes.TOP_MARGIN_MONTH / 2 - (fm.ascent + fm.descent) / 2;

                canvas.drawText(monthName, Ctes.MARGIN_LEFT + col * interval + interval / 3, y, Ctes.paintMonthName);

                //linea separacion meses
                canvas.drawLine(Ctes.MARGIN_LEFT + col * interval, Ctes.TOP_MARGIN, Ctes.MARGIN_LEFT + col * interval, screenHeight - Ctes.BOTTOM_MARGIN, Ctes.paintMonthName);
            }

            if (DateUtils.isLastDayOfMonth(dateTemp)) {
                String monthName = Ctes.monthFormat.format(cal.getTime());
                monthName = Character.toUpperCase(monthName.charAt(0)) + monthName.substring(1);
                float stringWidth = Ctes.paintMonthName.measureText(monthName);

                Paint.FontMetrics fm = Ctes.paintMonthName.getFontMetrics();
                float y = Ctes.TOP_MARGIN + Ctes.TOP_MARGIN_MONTH / 2 - (fm.ascent + fm.descent) / 2;

                canvas.drawText(monthName, Ctes.MARGIN_LEFT + col * interval - stringWidth + interval -interval / 3, y, Ctes.paintMonthName);
            }

            dateTemp = DateUtils.getNextDay(dateTemp);
        }

    }

    private void drawGrid(Canvas canvas) {

        //vertical
        for (int i = 0; i < xcells + 1; i++) {
            int x = i * interval;
            canvas.drawLine(x + Ctes.MARGIN_LEFT, Ctes.TOP_MARGIN + Ctes.TOP_MARGIN_MONTH, x+Ctes.MARGIN_LEFT, screenHeight-Ctes.BOTTOM_MARGIN, Ctes.gridPaint);
        }

        int y = Ctes.TOP_MARGIN + Ctes.TOP_MARGIN_DAY + Ctes.TOP_MARGIN_MONTH;

        //horizontal
        while (y<screenHeight-Ctes.BOTTOM_MARGIN) {
            canvas.drawLine(Ctes.MARGIN_LEFT, y , screenWidth - Ctes.MARGIN_RIGHT, y , Ctes.gridPaint);
            y = y + interval;
        }

        //vertical
        canvas.drawLine(Ctes.MARGIN_LEFT,Ctes.TOP_MARGIN,Ctes.MARGIN_LEFT,screenHeight-Ctes.BOTTOM_MARGIN,Ctes.paintMonthName);
        canvas.drawLine(screenWidth-Ctes.MARGIN_RIGHT,Ctes.TOP_MARGIN,screenWidth-Ctes.MARGIN_RIGHT,screenHeight-Ctes.BOTTOM_MARGIN,Ctes.paintMonthName);
        //horizontal
        canvas.drawLine(Ctes.MARGIN_LEFT, Ctes.TOP_MARGIN, screenWidth - Ctes.MARGIN_RIGHT, Ctes.TOP_MARGIN, Ctes.paintMonthName);
        canvas.drawLine(Ctes.MARGIN_LEFT, screenHeight-Ctes.BOTTOM_MARGIN, screenWidth - Ctes.MARGIN_RIGHT, screenHeight-Ctes.BOTTOM_MARGIN, Ctes.paintMonthName);
    }

    private void drawTasks(){
        for(Task task:Model.TASKS){

        }
    }
}
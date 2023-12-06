package com.plm.dataintime.fragment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.plm.dataintime.Data;

import java.util.List;

public class DataView extends View {
    private final List<Data> dataInTime;

    private Paint paint;
    private int lineColor = Color.BLACK; // Color por defecto
    private float lineWidth = 5; // Grosor por defecto

    public DataView(Context context, List<Data> dataInTime) {
        super(context);
        this.dataInTime = dataInTime;
        init();
    }

    private void init() {
        paint = new Paint();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 30, getWidth(), getHeight(), paint);
    }

}

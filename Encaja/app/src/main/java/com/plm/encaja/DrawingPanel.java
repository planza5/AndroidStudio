package com.plm.encaja;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends View {
    private List<ItemRectangle> rectangles;

    private double totalWidth = 244;
    private double totalHeight = 60;
    private double zoom = 1;

    public DrawingPanel(Context context, double w, double h) {
        super(context);
        this.totalWidth = w;
        this.totalHeight = h;
        rectangles = new ArrayList<>();
    }

    public DrawingPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        rectangles = new ArrayList<>();
    }

    public DrawingPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rectangles = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calcular el tamaño del margen
        int margin = dxToPx(20);

        // Obtener el ancho y el alto del panel
        int width = canvas.getWidth() - 2 * margin;
        int height = canvas.getHeight() - 2 * margin;

        // Obtener el tamaño del rectángulo después del zoom
        double rectWidth = totalWidth * zoom;
        double rectHeight = totalHeight * zoom;

        // Calcular el factor de escala para que el rectángulo quepa en el panel
        double scale = Math.min(width / rectWidth, height / rectHeight);

        // Calcular el ancho y el alto del rectángulo después del escalado
        int rectScaledWidth = (int) (rectWidth * scale);
        int rectScaledHeight = (int) (rectHeight * scale);

        // Calcular las coordenadas del rectángulo
        int rectX = margin + (width - rectScaledWidth) / 2;
        int rectY = margin + (height - rectScaledHeight) / 2;

        // Dibujar el fondo negro
        canvas.drawColor(Color.BLACK);

        // Dibujar el rectángulo blanco
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(rectX, rectY, rectX + rectScaledWidth, rectY + rectScaledHeight, paint);

        // Dibujar los rectángulos de la lista
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        for (ItemRectangle itemRectangle : rectangles) {
            double itemWidth = itemRectangle.getDimension().getWidth() * scale;
            double itemHeight = itemRectangle.getDimension().getHeight() * scale;
            double itemX = itemRectangle.getLocation().getX() * scale + rectX;
            double itemY = itemRectangle.getLocation().getY() * scale + rectY;
            canvas.drawRect((float) itemX, (float) itemY, (float) (itemX + itemWidth), (float) (itemY + itemHeight), paint);
        }
    }

    public void setTotalWidth(double totalWidth) {
        this.totalWidth = totalWidth;
        invalidate();
    }

    public void setTotalHeight(double totalHeight) {
        this.totalHeight = totalHeight;
        invalidate();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        invalidate();
    }

    // Método para convertir dp a px
    private int dxToPx(int dx) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dx * density);
    }

    public void changeTotalDimension(double w, double h) {
        totalWidth = w;
        totalHeight = h;
    }

    public double getTotalWidth() {
        return totalWidth;
    }

    public double getTotalHeight() {
        return totalHeight;
    }

    public void setRectangles(List<ItemRectangle> rectangles) {
        this.rectangles = rectangles;
        invalidate(); // Llama a este método para forzar la actualización del dibujo
    }
}

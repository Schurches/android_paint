package com.example.steven.celpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.R.drawable.ic_menu_delete;
import static android.R.drawable.ic_menu_edit;

/**
 * Created by steven on 06/08/2017.
 */

public class VistaDibujo extends View {
    private Paint circulo;
    private Path localizacionCirculo;
    private final int colorCirculo = Color.parseColor("#584574");
    private Paint paint;
    private Path path;
    private float posX, posY;
    private Canvas canvas;
    private Bitmap lienzo;
    private Paint dibujo;
    public LinearLayout.LayoutParams params;
    public LinearLayout layout;

    public VistaDibujo(Context context) {
        super(context);
        this.setBackgroundColor(Color.WHITE);
        this.createCircle();
        this.inicializarPincel();
        this.dibujo = new Paint();
        this.params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.layout = new LinearLayout(context);
    }

    public VistaDibujo(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.WHITE);
        this.createCircle();
        this.inicializarPincel();
        this.dibujo = new Paint();
        this.params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.layout = new LinearLayout(context);
    }

    public VistaDibujo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundColor(Color.WHITE);
        this.createCircle();
        this.inicializarPincel();
        this.dibujo = new Paint();
        this.params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.layout = new LinearLayout(context);
    }

    public void setPencil(int width){
        this.paint.setStrokeWidth(width);
    }

    public void setPencilColor(int color){
        this.paint.setColor(color);
    }


    public void setPencilColor(String color){
        this.paint.setColor(Color.parseColor(color));
    }

    public Canvas getCanvas(){
        return canvas;
    }


    public void setClear(){
        this.canvas.drawColor(Color.WHITE);
        this.path.reset();
        this.localizacionCirculo.reset();
        this.invalidate(); //Vuelve a llamar al evento OnDraw
    }

    public void createCircle(){
        this.localizacionCirculo = new Path();
        this.circulo = new Paint();
        this.circulo.setColor(colorCirculo);
        this.circulo.setStyle(Paint.Style.STROKE);
        this.circulo.setStrokeWidth(14);
    }

    public void inicializarPincel(){
        this.paint = new Paint();
        this.path = new Path();
        this.paint.setDither(true);//Precision de colores
        this.paint.setAntiAlias(true);//Con esto se obtiene mayor cantidad de pixeles en el dispositivo

        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND); //Dibujara una linea recta angular
        setPencil(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        this.lienzo = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);//Los bits se almacenaran en Bytes en la memoria. Asi el dibujo se vera mas nitido
        this.canvas = new Canvas(this.lienzo);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(this.lienzo, 0,0, this.dibujo);
        canvas.drawPath(this.path, this.paint);
        canvas.drawPath(this.localizacionCirculo,this.circulo);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent evento){
        float X = evento.getX();
        float Y = evento.getY();
        switch(evento.getAction()){
            case MotionEvent.ACTION_DOWN: //Cuando pulsa la pantalla (dejar hundido o presionar)
                hundir(X,Y);
                break;
            case MotionEvent.ACTION_UP: //Cuando se suelta la pantalla
                soltar();
                break;
            case MotionEvent.ACTION_MOVE: //Cuando se hace el movimiento
                mover(X,Y);
                break;
            default:
                break;
        }
        this.invalidate();
        return super.onTouchEvent(evento);
    }

    private void mover(float X, float Y){
        this.localizacionCirculo.reset();
        this.path.quadTo(this.posX, this.posY, (X+this.posX)/2, (Y+this.posY)/2);
        this.posX = X;
        this.posY = Y;
        this.localizacionCirculo.addCircle(X,Y,30,Path.Direction.CW);
    }

    private void soltar(){
        this.localizacionCirculo.reset();
        this.canvas.drawPath(this.path, this.paint);
        this.path.reset();
    }

    private void hundir(float X, float Y){
        this.posX = X;
        this.posY = Y;
        this.path.reset();
        this.path.moveTo(this.posX, this.posY);
    }

}

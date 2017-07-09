package com.example.steven.celpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private Toolbar opciones;
    private VistaDibujo vistaDibujo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vistaDibujo = new VistaDibujo(this);
        setContentView(vistaDibujo);
        addContentView(vistaDibujo.layout,vistaDibujo.params);
        /*
        opciones = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(opciones);
        getSupportActionBar().setTitle("ASDASDASD");
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu theMenu) {
        getMenuInflater().inflate(R.menu.menu_opciones, theMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem selectedItem) {
        int id = selectedItem.getItemId();
        switch (id) {
            case R.id.Dibujar:
                vistaDibujo.setPencil();
                return true;
            case R.id.Borrar:
                vistaDibujo.setEraser();
                return true;
            case R.id.Limpiar:
                vistaDibujo.setClear();
                return true;
        }
        return super.onOptionsItemSelected(selectedItem);
    }

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
        public LayoutParams params;
        public LinearLayout layout;

        public VistaDibujo(Context contexto){
            super(contexto);
            this.setBackgroundColor(Color.WHITE);
            this.createCircle();
            this.inicializarPincel();
            this.dibujo = new Paint();
            this.params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.layout = new LinearLayout(contexto);
        }

        private void setPencil(){
            this.paint.setColor(Color.GREEN);
            this.paint.setStrokeWidth(14);
        }

        private void setEraser(){
            this.paint.setColor(Color.WHITE);
            this.paint.setStrokeWidth(40);
        }

        private void setClear(){
            this.canvas.drawColor(Color.WHITE);
            this.path.reset();
            this.localizacionCirculo.reset();
            this.invalidate(); //Vuelve a llamar al evento OnDraw
        }

        private void createCircle(){
            this.localizacionCirculo = new Path();
            this.circulo = new Paint();
            this.circulo.setColor(colorCirculo);
            this.circulo.setStyle(Paint.Style.STROKE);
            this.circulo.setStrokeWidth(14);
        }

        private void inicializarPincel(){
            this.paint = new Paint();
            this.path = new Path();
            this.paint.setDither(true);//Precision de colores
            this.paint.setAntiAlias(true);//Con esto se obtiene mayor cantidad de pixeles en el dispositivo
            this.paint.setColor(Color.BLACK);
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeCap(Paint.Cap.ROUND); //Dibujara una linea recta angular
            this.paint.setStrokeWidth(20);
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
            return true;
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
}

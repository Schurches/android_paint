package com.example.steven.celpaint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import static android.R.drawable.ic_menu_delete;
import static android.R.drawable.ic_menu_edit;

public class MainActivity extends Activity {

    private VistaDibujo drawingView;
    private FloatingActionButton botonFlotante1;
    private FloatingActionButton botonFlotante2;
    private DisplayMetrics device;
    private int baseLocationX;
    private int baseLocationY;
    private int currentStrokeSize;
    private int currentColorID;
    private boolean wasHEX;
    private int currentHEX;
    private int currentR;
    private int currentG;
    private int currentB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (VistaDibujo) findViewById(R.id.drawingView);
        device = getResources().getDisplayMetrics();
        baseLocationX = device.widthPixels;
        baseLocationY = device.heightPixels;
        LinearLayout.LayoutParams layoutCharacteristics = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutCharacteristics.setMargins(24,24,24,24);
        createFloatingButtons(this, layoutCharacteristics);
        this.addContentView(botonFlotante1,layoutCharacteristics);
        this.addContentView(botonFlotante2,layoutCharacteristics);
        currentStrokeSize=10;
        currentColorID=0;
        wasHEX=false;
        currentHEX=Color.BLACK;
        currentR = 1000;
        currentG = 1000;
        currentB = 1000;
    }

    private void setFloatingButtonsLocation() {
        botonFlotante1.setX(baseLocationX-baseLocationX/5);
        botonFlotante1.setY(baseLocationY-(baseLocationY/7));
        botonFlotante2.setX(baseLocationX-baseLocationX/5);
        botonFlotante2.setY(baseLocationY-(baseLocationY/4));
    }

    public void iniPropertiesActivity(){
        Intent iniActivity = new Intent(getApplicationContext(),PropertiesActivity.class);
        iniActivity.putExtra("STROKESIZE",currentStrokeSize);
        iniActivity.putExtra("COLORID",currentColorID);
        iniActivity.putExtra("HEXCODE",currentHEX);
        iniActivity.putExtra("RED",currentR);
        iniActivity.putExtra("GREEN",currentG);
        iniActivity.putExtra("BLUE",currentB);
        iniActivity.putExtra("HEXSTATE",wasHEX);
        startActivityForResult(iniActivity,1);
    }

    protected void onActivityResult(int petition, int requestCode, Intent properties){
        if(petition == 1){
            if(requestCode==RESULT_OK){
                Bundle datos = properties.getExtras();
                currentStrokeSize = datos.getInt("STROKESIZE");
                currentColorID = datos.getInt("COLORID");
                currentHEX = datos.getInt("HEXCODE");
                currentR = datos.getInt("RED");
                currentG = datos.getInt("GREEN");
                currentB = datos.getInt("BLUE");
                wasHEX = datos.getBoolean("HEXSTATE");
                drawingView.setPencil(currentStrokeSize);
                int color;
                switch(currentColorID){
                    case 1:
                        color = getApplicationContext().getColor(R.color.colorRED);
                        drawingView.setPencilColor(color);
                        break;
                    case 2:
                        color = getApplicationContext().getColor(R.color.colorBLUE);
                        drawingView.setPencilColor(color);
                        break;
                    case 3:
                        color = getApplicationContext().getColor(R.color.colorGREEN);
                        drawingView.setPencilColor(color);
                        break;
                    case 4:
                        color = getApplicationContext().getColor(R.color.colorPINK);
                        drawingView.setPencilColor(color);
                        break;
                    case 5:
                        color = getApplicationContext().getColor(R.color.colorYELLOW);
                        drawingView.setPencilColor(color);
                        break;
                    case 6:
                        color = getApplicationContext().getColor(R.color.colorBROWN);
                        drawingView.setPencilColor(color);
                        break;
                    case 7:
                        color = getApplicationContext().getColor(R.color.colorWHITE);
                        drawingView.setPencilColor(color);
                        break;
                    case 8:
                        if(wasHEX){
                            drawingView.setPencilColor(currentHEX);
                        }else{
                            drawingView.setPencilColor(Color.rgb(currentR,currentG,currentB));
                        }
                        break;
                }
            }
        }
    }

    public void createFloatingButtons(final Context context, LayoutParams layoutCharacteristics){
        DisplayMetrics device = getResources().getDisplayMetrics();
        //FloatingButton1 declaration and characteristic settings
        botonFlotante1 = new FloatingActionButton(context);
        botonFlotante1.setLayoutParams(layoutCharacteristics);
        botonFlotante1.setImageResource(ic_menu_edit);
        botonFlotante1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPropertiesActivity();
            }
        });

        //FloatingButton2 declaration and characteristic settings
        botonFlotante2 = new FloatingActionButton(context);
        botonFlotante2.setLayoutParams(layoutCharacteristics);
        botonFlotante2.setImageResource(ic_menu_delete);
        botonFlotante2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setClear();
            }
        });
        setFloatingButtonsLocation();
    }


}

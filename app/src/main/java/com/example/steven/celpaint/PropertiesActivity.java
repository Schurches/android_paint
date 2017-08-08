package com.example.steven.celpaint;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PropertiesActivity extends AppCompatActivity {

    //Color Buttons
    private Button redButton;
    private Button blueButton;
    private Button greenButton;
    private Button yellowButton;
    private Button pinkButton;
    private Button brownButton;
    private Button eraserButton;
    private Button customizedButton;
    //Action Buttons
    private Button setButton;
    private Button acceptButton;
    private Button cancelButton;
    //HEX to RGB checkbox
    private CheckBox checkbox;
    //Info fields
    private EditText red;
    private EditText green;
    private EditText blue;
    private EditText HEX;
    private EditText strokeWidth;
    //
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
        setContentView(R.layout.activity_properties);
        Bundle datos = getIntent().getExtras();
        currentStrokeSize = datos.getInt("STROKESIZE");
        currentColorID = datos.getInt("COLORID");
        currentHEX = datos.getInt("HEXCODE");
        currentR = datos.getInt("RED");
        currentG = datos.getInt("GREEN");
        currentB = datos.getInt("BLUE");
        wasHEX = datos.getBoolean("HEXSTATE");
        this.setFinishOnTouchOutside(false);
        findViews();
        restoreLastConfiguration();
    }

    public void restoreLastConfiguration(){
        if(currentColorID!=7){
            eraserButton.setText(R.string.eraseLabel);
        }
        if(currentColorID!=8){
            showFields(false);
        }else if(currentColorID==8){
            showFields(true);
        }
        strokeWidth.setHint(""+currentStrokeSize);
        if(wasHEX){
            HEX.setText(""+currentHEX);
        }else{
            if(currentR > 255 && currentG > 255 && currentB > 255){
                red.setText("");
                blue.setText("");
                green.setText("");
            }else{
                red.setText(""+currentR);
                blue.setText(""+currentB);
                green.setText(""+currentG);
            }
        }
        switch(currentColorID){
            case 1:
                redButton.setText(R.string.currentColor);
                break;
            case 2:
                blueButton.setText(R.string.currentColor);
                break;
            case 3:
                greenButton.setText(R.string.currentColor);
                break;
            case 4:
                pinkButton.setText(R.string.currentColor);
                break;
            case 5:
                yellowButton.setText(R.string.currentColor);
                break;
            case 6:
                brownButton.setText(R.string.currentColor);
                break;
            case 7:
                eraserButton.setText(R.string.currentColor);
                break;
            case 8:
                customizedButton.setText(R.string.currentColor);
                showFields(true);
                break;
        }
    }

    public void select_a_color(View vista){
        int selectedColorButton = 0;
        switch (vista.getId()){

            case R.id.colorButton1:
                selectedColorButton = 1;
                redButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton2:
                selectedColorButton = 2;
                blueButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton3:
                selectedColorButton = 3;
                greenButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton4:
                selectedColorButton = 4;
                pinkButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton5:
                selectedColorButton = 5;
                yellowButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton6:
                selectedColorButton = 6;
                brownButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton7:
                selectedColorButton = 7;
                eraserButton.setText(R.string.currentColor);
                break;
            case R.id.colorButton8:
                selectedColorButton = 8;
                customizedButton.setText(R.string.currentColor);
                showFields(true);
                break;
        }
        if(selectedColorButton != 7){
            eraserButton.setText(R.string.eraseLabel);
        }
        if(selectedColorButton != 8){
            showFields(false);
        }
        currentColorID = selectedColorButton;
    }

    public void showFields(boolean show){
        if(show){
            makeHEXVisible();
            checkbox.setVisibility(View.VISIBLE);
        }else{
            checkbox.setVisibility(View.GONE);
        }
    }

    public void changeHEXState(View view){
        makeHEXVisible();
    }

    public void makeHEXVisible(){
        if(checkbox.isChecked()){ //If HEX Allowed
            red.setVisibility(View.GONE);
            blue.setVisibility(View.GONE);
            green.setVisibility(View.GONE);
            red.setText("");
            blue.setText("");
            green.setText("");
            HEX.setVisibility(View.VISIBLE);
        }else{ //Otherwise, RGB
            red.setVisibility(View.VISIBLE);
            blue.setVisibility(View.VISIBLE);
            green.setVisibility(View.VISIBLE);
            HEX.setVisibility(View.GONE);
            HEX.setText("");
        }
    }

    public int setStrokeWidth(View view){
        if(!strokeWidth.getText().toString().isEmpty()){
            int size = Integer.parseInt(strokeWidth.getText().toString());
            if(size > 20){
                currentStrokeSize = 20;
                return 20;
            }else if(size < 1){
                currentStrokeSize = 1;
                return 1;
            }else{
                currentStrokeSize = size;
                return size;
            }
        }else{
            return currentStrokeSize;
        }
    }



    public void establishColor(View view){
        String HEXAmmount = HEX.getText().toString();
        String REDAmmount = red.getText().toString();
        String BLUEAmmount = blue.getText().toString();
        String GREENAmmount = green.getText().toString();
        Color custom = new Color();
        if(!checkbox.isChecked() && (checkbox.getVisibility()==View.VISIBLE)){
            if(!REDAmmount.isEmpty() && !BLUEAmmount.isEmpty() && !GREENAmmount.isEmpty()){
                currentR = Integer.parseInt(REDAmmount);
                currentG = Integer.parseInt(GREENAmmount);
                currentB = Integer.parseInt(BLUEAmmount);
                if((currentR < 255 && currentR > 0)&&(currentG < 255 & currentG > 0)&&(currentB < 255 && currentB > 0)){
                    currentHEX = Color.BLACK;
                    customizedButton.setBackgroundColor(custom.rgb(currentR,currentG,currentB));
                }else{
                    Toast.makeText(this,R.string.RGBAlert,Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,R.string.RGBAlert2,Toast.LENGTH_LONG).show();
            }
        }else if(checkbox.isChecked() && (checkbox.getVisibility()==View.VISIBLE)){
            if(!HEXAmmount.isEmpty()){
                currentR = 1000;
                currentG = 1000;
                currentB = 1000;
                currentHEX = custom.parseColor("#"+HEXAmmount);
                customizedButton.setBackgroundColor(currentHEX);
            }else{
                Toast.makeText(this,R.string.RGBAlert2,Toast.LENGTH_LONG).show();
            }
        }

    }

    public void findViews(){
        redButton = (Button) findViewById(R.id.colorButton1);
        blueButton = (Button) findViewById(R.id.colorButton2);
        greenButton = (Button) findViewById(R.id.colorButton3);
        pinkButton = (Button) findViewById(R.id.colorButton4);
        yellowButton = (Button) findViewById(R.id.colorButton5);
        brownButton = (Button) findViewById(R.id.colorButton6);
        eraserButton = (Button) findViewById(R.id.colorButton7);
        customizedButton = (Button) findViewById(R.id.colorButton8);
        setButton = (Button) findViewById(R.id.setButton);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        checkbox = (CheckBox) findViewById(R.id.HEXRGBCheckbox);
        red = (EditText) findViewById(R.id.redEDT);
        green = (EditText) findViewById(R.id.greenEDT);
        blue = (EditText) findViewById(R.id.blueEDT);
        HEX = (EditText) findViewById(R.id.hexEDT);
        strokeWidth = (EditText) findViewById(R.id.strokeSizeEDT);
    }

    public void finalizar(View v){
        Intent guardarInformacion = new Intent(); //Como no se le pasa un parametro para que inicie una actividad, solo guardara la info que se almacene
        if(v.getId() == R.id.acceptButton){
            guardarInformacion.putExtra("STROKESIZE",currentStrokeSize);
            guardarInformacion.putExtra("COLORID",currentColorID);
            guardarInformacion.putExtra("HEXCODE",currentHEX);
            guardarInformacion.putExtra("RED",currentR);
            guardarInformacion.putExtra("GREEN",currentG);
            guardarInformacion.putExtra("BLUE",currentB);
            guardarInformacion.putExtra("HEXSTATE",checkbox.isChecked());
            setResult(RESULT_OK,guardarInformacion);
        }else if(v.getId() == R.id.cancelButton){
            setResult(RESULT_CANCELED,guardarInformacion);
        }
        finish(); //Cierra la actividad actual
    }


}

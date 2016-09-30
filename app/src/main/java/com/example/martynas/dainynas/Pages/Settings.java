package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.SettingsDB;

import java.lang.reflect.Field;

public class Settings extends AppCompatActivity {
    private TextView preview;
    private NumberPicker numberPicker;
    private SettingsDB settingsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        ActiveAndroid.initialize(this);

        settingsDB = SettingsDB.load(SettingsDB.class, 1);
        Button previewButton = (Button) findViewById(R.id.tekstoDysisPreview);
        Button nustaytiButton = (Button) findViewById(R.id.tekstoDydisSet);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        preview = (TextView) findViewById(R.id.tekstasPreview);
        preview.setTextSize(settingsDB.zodziaiDydis);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(50);
        setNumberPickerTextColor(numberPicker, -1);


        numberPicker.setValue(settingsDB.zodziaiDydis);
        preview = (TextView) findViewById(R.id.tekstasPreview);

        previewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview.setTextSize(numberPicker.getValue());
            }
        });
        nustaytiButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDB.zodziaiDydis = numberPicker.getValue();
                settingsDB.save();
                preview.setTextSize(numberPicker.getValue());
                Toast.makeText(Settings.this, "Šrifto dydis pakeistas", Toast.LENGTH_SHORT).show();;
            }
        });
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerColor", e);
                }
            }
        }
        return false;
    }
}

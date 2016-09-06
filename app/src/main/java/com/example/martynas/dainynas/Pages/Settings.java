package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.SettingsDB;

public class Settings extends AppCompatActivity {
    private Button previewButton;
    private Button nustaytiButton;
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
        previewButton = (Button)findViewById(R.id.tekstoDysisPreview);
        nustaytiButton = (Button) findViewById(R.id.tekstoDydisSet);
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        preview = (TextView) findViewById(R.id.tekstasPreview);
        preview.setTextSize(settingsDB.zodziaiDydis);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(50);


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
            }
        });
    }
}

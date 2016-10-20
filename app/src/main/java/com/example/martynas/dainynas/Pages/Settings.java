package com.example.martynas.dainynas.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
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
import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.SettingsDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

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
        final Context context = this;

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

        Button saveDB = (Button) findViewById(R.id.saveDainos);
        saveDB.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveDBToFile(context);
                Toast.makeText(context, "Dainos išsaugotos į vidinę telefono atmintį", Toast.LENGTH_LONG).show();
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
    public void saveDBToFile (Context context){
        StringBuilder builder = new StringBuilder(7000000);
        String query = new Select("Dainos" + ".*, " + "Dainos" + ".Id as _id").from(Daina.class).orderBy("_id DESC").limit(1).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(query,null);
        cursor.moveToFirst();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        int savedVersionCode = sharedPreferences.getInt("version_code", 1);
        builder.append(String.valueOf(savedVersionCode) + "\t\t\t\r\n");
        long id = cursor.getLong(cursor.getColumnIndex("Id"));
        for (long i = 1; i <= id; i++){
            Daina daina = Daina.load(Daina.class, i);
            List<Posmelis> posmeliai = daina.posmeliai();
            String zodziai = "";
            String zodziaiOnlyENLetters = "";
            for (Posmelis posmelis:posmeliai
                    ) {zodziai += posmelis.zodziai + "\r\n\r\n";
                zodziaiOnlyENLetters += posmelis.zodziaiOnlyENLetters + "\r\n\r\n";
            }
            builder.append(daina.pavadinimas + "\t\r\n\r\n"
                    +daina.pavOnlyENLetters + "\t\r\n\r\n"
                    +daina.vertimas + "\t\r\n\r\n"
                    +daina.puslapis + "\t\r\n\r\n"
                    +zodziai + "\t\r\n"
                    +zodziaiOnlyENLetters + "\t\t\r\n\r\n");
        }
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, "dainosGen.txt");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(builder.toString().getBytes());
            stream.close();
            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, context.MODE_PRIVATE));
            outputStreamWriter.write(builder.toString());
            outputStreamWriter.close();*/
        }
        catch (IOException e){
            com.activeandroid.util.Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}

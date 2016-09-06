package com.example.martynas.dainynas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.Pages.AddDaina;
import com.example.martynas.dainynas.Pages.ListDaina;
import com.example.martynas.dainynas.Pages.Settings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Resources resources;
    private String output;
    private RadioGroup rikiuotiDainas;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActiveAndroid.initialize(this);

        checkFirstRun();

        rikiuotiDainas = (RadioGroup) findViewById(R.id.rikiuotiDainas);
        radioButton = (RadioButton) findViewById(R.id.buttonSortByPavadinimas);
        radioButton.setActivated(true);

        rikiuotiDainas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {
               if (checkedId == 0){
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByAtlikejas);
                   radioButton.setActivated(false);
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByPuslapis);
                   radioButton.setActivated(false);
               }
                else if (checkedId == 1) {
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByPavadinimas);
                   radioButton.setActivated(false);
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByPuslapis);
                   radioButton.setActivated(false);
               }
                else {
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByPavadinimas);
                   radioButton.setActivated(false);
                   radioButton = (RadioButton) findViewById(R.id.buttonSortByAtlikejas);
                   radioButton.setActivated(false);
               }
            }
        });

    }
    public void addDainaPage (View view){
        Intent intent = new Intent(this, AddDaina.class);
        startActivity(intent);
    }

    public void listDaina (View view){
        Intent intent = new Intent(this, ListDaina.class);
        startActivity(intent);
    }

    public void settings (View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            fillDatabase();
            SettingsDB settingsDB = new SettingsDB();
            settingsDB.zodziaiDydis = 20;
            settingsDB.save();

            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    private void fillDatabase (){
        resources = getResources();
        try
        {
            //Load the file from the raw folder - don't forget to OMIT the extension
            output = LoadFile("dainos", true);
            //output to LogCat
            Log.i("test", output);
        }
        catch (IOException e)
        {
            //display an error toast message
            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
            toast.show();
        }
// Atkomentuoti jei nuspresciau faila deti i assets folderi
        /*try
        {
            //Load the file from assets folder - don't forget to INCLUDE the extension
            output = LoadFile("from_assets_folder.txt", false);
            //output to LogCat
            Log.i("test", output);
        }
        catch (IOException e)
        {
            //display an error toast message
            Toast toast = Toast.makeText(this, "File: not found!", Toast.LENGTH_LONG);
            toast.show();
        }*/

        Daina fillDainos = new Daina(output);
    }

    public String LoadFile(String fileName, boolean loadFromRawFolder) throws IOException{
        //Create a InputStream to read the file into
        InputStream iS;

        if (loadFromRawFolder)
        {
            //get the resource id from the file name
            int rID = resources.getIdentifier("com.example.martynas.dainynas:raw/"+fileName, null, null);
            //get the file as a stream
            iS = resources.openRawResource(rID);
        }
        else
        {
            //get the file as a stream
            iS = resources.getAssets().open(fileName);
        }

        //create a buffer that has the same size as the InputStream
        byte[] buffer = new byte[iS.available()];
        //read the text file as a stream, into the buffer
        iS.read(buffer);
        //create a output stream to write the buffer into
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        //write this buffer to the output stream
        oS.write(buffer);
        //Close the Input and Output streams
        oS.close();
        iS.close();

        //return the output stream as a String
        return oS.toString();
    }
}

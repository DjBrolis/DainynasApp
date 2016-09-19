package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.SettingsDB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomePage extends AppCompatActivity {

    private Resources resources;
    private String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDainaPage(view);
            }
        });

        ActiveAndroid.initialize(this);

        checkFirstRun();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nustatymai:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addDainaPage (View view){
        Intent intent = new Intent(this, AddDaina.class);
        startActivity(intent);
    }

    public void listDaina (View view){
        Intent intent = new Intent(this, SearchList.class);
        intent.putExtra("FocusSearch", false);
        startActivity(intent);
    }

    public void SearchList (View view){
        Intent intent = new Intent(this, SearchList.class);
        intent.putExtra("FocusSearch", true);
        startActivity(intent);
    }

   /* public void settings (View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }*/

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
            boolean newInstall = true;
            int [] newVersions = new int [1];
            fillDatabase(newVersions, newInstall);
            SettingsDB settingsDB = new SettingsDB();
            settingsDB.zodziaiDydis = 20;
            settingsDB.save();

            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {
            boolean newInstall = false;
            int [] newVersions = new int[currentVersionCode-savedVersionCode];
            int j = 0;
            for (int i = savedVersionCode + 1; i <= currentVersionCode; i++){
                newVersions[j] = i;
                j++;
            }
            fillDatabase(newVersions, newInstall);

            // TODO This is an upgrade

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    private void fillDatabase (int [] newVersions, boolean newInstall){
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

        String[] outputTest = output.trim().split("\t\t\t");
        String outputConcat = "";
        if (newInstall){
            for (int i = 1; i < outputTest.length; i = i+2) {
                Daina fillDainos = new Daina(outputTest[i]);
            }
        }
        else {
            int j = 0;
            for (int i = 1; i < outputTest.length; i = i + 2) {
                if (newVersions[j] == Integer.parseInt(outputTest[i-1].trim())) {
                    Daina fillDainos = new Daina(outputTest[i]);
                    j++;
                }
            }
        }


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

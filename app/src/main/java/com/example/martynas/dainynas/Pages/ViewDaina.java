package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.SettingsDB;

import java.util.List;

public class ViewDaina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daina);

        Intent intent = getIntent();
        Daina daina = Daina.load(Daina.class, intent.getLongExtra("Daina", 1));
        SettingsDB settingsDB = SettingsDB.load(SettingsDB.class, 1);
        TextView textView = (TextView) findViewById(R.id.viewDainaZodziai);
        textView.setTextSize(settingsDB.zodziaiDydis);

        //Pakeicia toolbar uzrasa i dainos pavadinima
        Toolbar viewDainaToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(viewDainaToolbar);
        getSupportActionBar().setTitle(daina.pavadinimas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Posmelis> posmeliai = daina.posmeliai();
        String zodziai = new String();
        for (Posmelis posmelis: posmeliai
             ) {
            zodziai += posmelis.zodziai + "\n";
        }
        textViewZodziai.setText(zodziai);
    }

}

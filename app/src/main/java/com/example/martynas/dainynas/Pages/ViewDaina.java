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
    protected Daina daina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daina);

        Intent intent = getIntent();
        daina = Daina.load(Daina.class, intent.getLongExtra("Daina", 1));

        SettingsDB settingsDB = SettingsDB.load(SettingsDB.class, 1);
        TextView textView = (TextView) findViewById(R.id.viewDainaZodziai);
        textView.setTextSize(settingsDB.zodziaiDydis);
        TextView textView1 = (TextView) findViewById(R.id.viewVertimas);

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
        textView.setText(zodziai);
        if (!daina.vertimas.isEmpty()) {
            textView1.setText(daina.vertimas);
        }
        else {
            textView1.setVisibility(View.GONE);
        }
    }
    public void editDaina (View view) {
        Intent intent = new Intent(this, EditDaina.class);
        intent.putExtra("DainaId", daina.getId());
        startActivity(intent);
    }

}

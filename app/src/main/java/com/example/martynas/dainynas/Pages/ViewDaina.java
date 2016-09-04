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

import java.util.List;

public class ViewDaina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Daina daina = Daina.load(Daina.class, intent.getLongExtra("Daina", 1));

        //Pakeicia toolbar uzrasa i dainos pavadinima
        Toolbar viewDainaToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(viewDainaToolbar);
        getSupportActionBar().setTitle(daina.pavadinimas);

        TextView textViewZodziai = (TextView) findViewById(R.id.viewDainaZodziai);

        List<Posmelis> posmeliai = daina.posmeliai();
        String zodziai = new String();
        for (Posmelis posmelis: posmeliai
             ) {
            zodziai += posmelis.zodziai + "\n";
        }
        textViewZodziai.setText(zodziai);
    }

}

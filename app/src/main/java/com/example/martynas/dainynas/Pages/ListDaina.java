package com.example.martynas.dainynas.Pages;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.MainActivity;
import com.example.martynas.dainynas.R;

import java.util.ArrayList;
import java.util.List;

public class ListDaina extends AppCompatActivity {

    public VisosDainos visosDainos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_daina);
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

        Toolbar viewDainaToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(viewDainaToolbar);
        getSupportActionBar().setTitle("Visos dainos");

        populateListView ();
        registerDainosPasirinkimas();

    }
    public void populateListView() {

        List<Daina> dainuSarasas = new Select().from(Daina.class).orderBy("Pavadinimas ASC").execute();
        visosDainos = new VisosDainos(dainuSarasas);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.daina, visosDainos.dainuPavadinimai);
        ListView list = (ListView) findViewById(R.id.listViewDainos);
        list.setAdapter(adapter);

    }


    private void registerDainosPasirinkimas(){
        final ListView list = (ListView) findViewById(R.id.listViewDainos);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;
                Intent intent = new Intent(ListDaina.this, ViewDaina.class);
                intent.putExtra("Daina", visosDainos.dainuId[i]);
                startActivity(intent);
            }
        });
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }


}

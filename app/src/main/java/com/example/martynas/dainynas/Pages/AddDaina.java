package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaViewModel;
import com.example.martynas.dainynas.R;

public class AddDaina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daina);

        Intent intent = getIntent();

        Toolbar viewDainaToolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(viewDainaToolbar);
        getSupportActionBar().setTitle("Pridėti dainą");
    }

    public void addDaina (View viev) {
        ActiveAndroid.initialize(this);
        DainaViewModel dainaModelTemp = new DainaViewModel();
        EditText editText = (EditText) findViewById(R.id.dainosPavadinimas);
        dainaModelTemp.pavadinimas = editText.getText().toString();
        editText = (EditText) findViewById(R.id.dainosZodziai);
        dainaModelTemp.zodziai = editText.getText().toString();

        Daina daina = new Daina(dainaModelTemp);

        String message = "Daina " + dainaModelTemp.pavadinimas + " išsaugota";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}

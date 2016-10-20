package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaViewModel;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;

import java.io.IOException;
import java.util.List;

public class EditDaina extends AppCompatActivity {
    protected EditText pavadinimas;
    protected EditText puslapis;
    protected EditText zodziai;
    protected EditText vertimas;
    protected long id;
    protected long[] ids;
    protected int i;
    protected Daina daina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daina);
        Intent intent = getIntent();
        id = intent.getLongExtra("DainaId", 1);

        pavadinimas = (EditText) findViewById(R.id.editPavadinimas);
        puslapis = (EditText) findViewById(R.id.editPuslapis);
        zodziai = (EditText) findViewById(R.id.editZodziai);
        vertimas = (EditText) findViewById(R.id.editVertimas);

        String query = new Select("Id").from(Daina.class).orderBy("Id DESC").limit(1).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        final long maxId = cursor.getLong(0);
        ids = new long[(int) maxId];
        query = new Select("Id").from(Daina.class).orderBy("Pavadinimas").toSql();
        cursor = Cache.openDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        ids[0] = cursor.getLong(0);
        for (i=1; i < maxId; ++i){
            cursor.moveToNext();
            ids[i] = cursor.getLong(0);
        }
        boolean found = false;
        i = 0;
        while (!found){
            if (ids[i] == id){
                found = true;
            }
            else {
                ++i;
            }
        }


        fillView(ids[i]);

        ImageButton imageButton = (ImageButton) findViewById(R.id.saveEditedDaina);
        imageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveDaina();
                Toast.makeText(EditDaina.this, "Daina "+ '"' + pavadinimas.getText().toString() + '"' + " išsaugota", Toast.LENGTH_SHORT).show();
            }
        });
        ImageButton nextDaina = (ImageButton) findViewById(R.id.goNextDaina);
        nextDaina.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                ++i;
                if (i == (int) maxId){
                    i=0;
                }
                fillView(ids[i]);
            }
        });
        ImageButton previousDaina = (ImageButton) findViewById(R.id.goPreviousDaina);
        previousDaina.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                --i;
                if (i == -1){
                    i = (int) maxId - 1;
                }
                fillView(ids[i]);
            }
        });
    }
    protected void saveDaina (){
        DainaViewModel dainaViewModel = new DainaViewModel();
        dainaViewModel.pavadinimas = pavadinimas.getText().toString();
        dainaViewModel.puslapis = Integer.parseInt(puslapis.getText().toString());
        dainaViewModel.vertimas = vertimas.getText().toString();
        dainaViewModel.zodziai = zodziai.getText().toString();
        daina.updateDaina (daina, dainaViewModel);
    }

    protected void fillView (long id){
        daina = Daina.load(Daina.class, id);
        getSupportActionBar().setTitle(daina.pavadinimas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Posmelis> posmeliai = daina.posmeliai();
        StringBuilder builder = new StringBuilder();
        for (Posmelis posmelis: posmeliai
                ) {
            builder.append(posmelis.zodziai + '\n');
        }
        pavadinimas.setText(daina.pavadinimas);
        if (daina.puslapis != 0){
            puslapis.setText(String.valueOf(daina.puslapis));
        }else {
            puslapis.setText("");
        }
        zodziai.setText(builder.toString());
        vertimas.setText(daina.vertimas);
    }
}

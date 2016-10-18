package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaViewModel;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;

import java.util.List;

public class EditDaina extends AppCompatActivity {
    protected EditText pavadinimas;
    protected EditText puslapis;
    protected EditText zodziai;
    protected EditText vertimas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daina);
        Intent intent = getIntent();

        pavadinimas = (EditText) findViewById(R.id.editPavadinimas);
        puslapis = (EditText) findViewById(R.id.editPuslapis);
        zodziai = (EditText) findViewById(R.id.editZodziai);
        vertimas = (EditText) findViewById(R.id.editVertimas);

        final Daina daina = Daina.load(Daina.class, intent.getLongExtra("DainaId", 1));
        List<Posmelis> posmeliai = daina.posmeliai();
        StringBuilder builder = new StringBuilder();
        for (Posmelis posmelis: posmeliai
             ) {
            builder.append(posmelis.zodziai + '\n');
        }
        pavadinimas.setText(daina.pavadinimas);
        puslapis.setText("1");
        zodziai.setText(builder.toString());
        vertimas.setText(daina.vertimas);
        ImageButton imageButton = (ImageButton) findViewById(R.id.saveEditedDaina);
        imageButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveDaina();
                Toast.makeText(EditDaina.this, pavadinimas.getText().toString() + "išsaugota", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void saveDaina (){
        DainaViewModel dainaViewModel = new DainaViewModel();
        dainaViewModel.pavadinimas = pavadinimas.getText().toString();
        dainaViewModel.puslapis = Integer.parseInt(pavadinimas.getText().toString());
        dainaViewModel.vertimas = vertimas.getText().toString();
        new Daina(dainaViewModel);
    }
}

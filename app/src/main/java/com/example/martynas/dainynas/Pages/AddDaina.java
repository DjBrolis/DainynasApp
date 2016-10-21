package com.example.martynas.dainynas.Pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaViewModel;
import com.example.martynas.dainynas.Mail;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AddDaina extends AppCompatActivity {
    protected EditText pavadinimas, puslapis, zodziai, vertimas;
    protected ImageButton saveAddDaina, newAddDaina;
    public Daina daina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daina);
        Intent intent = getIntent();

        Toolbar viewDainaToolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(viewDainaToolbar);
        getSupportActionBar().setTitle("Pridėti dainą");

        pavadinimas = (EditText) findViewById(R.id.savePavadinimas);
        puslapis = (EditText) findViewById(R.id.savePuslapis);
        zodziai = (EditText) findViewById(R.id.saveZodziai);
        vertimas = (EditText) findViewById(R.id.saveVertimas);

        newAddDaina = (ImageButton) findViewById(R.id.newAddDaina);
        newAddDaina.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pavadinimas.setText("");
                puslapis.setText("");
                zodziai.setText("");
                vertimas.setText("");
                newAddDaina.setVisibility(newAddDaina.GONE);
            }
        });

        saveAddDaina = (ImageButton) findViewById(R.id.saveAddDaina);
        saveAddDaina.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (check()){
                    saveDaina();
                    File file = saveToFile(daina);
                    Mail m = new Mail("dainynassender@gmail.com", "DainynoV4rtotojas");

                    String[] toArr = {"dainynasreceiver@gmail.com"};
                    m.setTo(toArr);
                    m.setFrom("dainynassender@gmail.com");
                    m.setSubject("Nauja daina " + daina.pavadinimas);
                    m.setBody(daina.pavadinimas);

                    try {
                        m.addAttachment(file.getCanonicalPath(), file.getName());

                        if (!m.send()) {
                            Toast.makeText(AddDaina.this, "Įvyko klaida", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(AddDaina.this, "Įvyko klaida", Toast.LENGTH_LONG).show();
                        Log.e("EditDaina", "Could not send email", e);
                    }
                    file.deleteOnExit();
                    pavadinimas.setText("");
                    puslapis.setText("");
                    zodziai.setText("");
                    vertimas.setText("");
                   // newAddDaina.setVisibility(newAddDaina.VISIBLE);
                }
            }
        });


    }

    public void saveDaina (){
        DainaViewModel dainaViewModel = new DainaViewModel();
        dainaViewModel.pavadinimas = pavadinimas.getText().toString();
        if (!puslapis.getText().toString().isEmpty()){
            dainaViewModel.puslapis = Integer.parseInt(puslapis.getText().toString());
        } else {
            dainaViewModel.puslapis = 0;
        }
        dainaViewModel.zodziai = zodziai.getText().toString();
        dainaViewModel.vertimas = vertimas.getText().toString();
        daina = new Daina(dainaViewModel);
        Toast.makeText(AddDaina.this, "Daina " + '"' + dainaViewModel.pavadinimas + '"' + " išsaugota", Toast.LENGTH_LONG).show();
    }
    public boolean check () {
        if (pavadinimas.getText().toString().isEmpty()){
            Toast.makeText(AddDaina.this, "Įveskite dainos pavadinimą", Toast.LENGTH_LONG).show();
            return false;
        }else if (zodziai.getText().toString().isEmpty()){
            Toast.makeText(AddDaina.this, "Įveskite dainos žodžius", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public File saveToFile (Daina daina) {
        StringBuilder builder = new StringBuilder();
        String zodziaiToFile = "";
        String zodziaiOnlyENLettersToFile = "";
        List<Posmelis> posmeliai = daina.posmeliai();
        for (Posmelis posmelis:posmeliai
                ) {zodziaiToFile += posmelis.zodziai + "\r\n\r\n";
            zodziaiOnlyENLettersToFile += posmelis.zodziaiOnlyENLetters + "\r\n\r\n";
        }
        builder.append(daina.pavadinimas + "\t\r\n\r\n"
                +daina.pavOnlyENLetters + "\t\r\n\r\n"
                +daina.vertimas + "\t\r\n\r\n"
                +daina.puslapis + "\t\r\n\r\n"
                +zodziaiToFile + "\t\r\n"
                +zodziaiOnlyENLettersToFile + "\t\t\r\n\r\n");
        String fileName = daina.pavadinimas + "abcdefghij";
        File path = this.getExternalFilesDir(null);
        File file = new File(path, fileName.substring(0, 10) + ".txt");
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(builder.toString().getBytes());
            stream.close();
        }
        catch (IOException e){
            Toast.makeText(this, "Įvyko klaida", Toast.LENGTH_LONG).show();
        }

        return file;
    }

}

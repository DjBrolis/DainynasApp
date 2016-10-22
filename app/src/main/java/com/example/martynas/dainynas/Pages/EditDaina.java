package com.example.martynas.dainynas.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaViewModel;
import com.example.martynas.dainynas.Mail;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class EditDaina extends AppCompatActivity {
    protected EditText pavadinimas;
    protected EditText puslapis;
    protected EditText zodziai;
    protected EditText vertimas;
    protected long id, maxId;
    protected long[] ids;
    protected int i, j;
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

        createIds();
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
                if (check()) {
                    saveDaina();
                    Toast.makeText(EditDaina.this, "Daina " + '"' + pavadinimas.getText().toString() + '"' + " išsaugota", Toast.LENGTH_SHORT).show();
                    File file = saveToFile(daina);
                    Mail m = new Mail("dainynassender@gmail.com", "DainynoV4rtotojas");

                    String[] toArr = {"dainynasreceiver@gmail.com"};
                    m.setTo(toArr);
                    m.setFrom("dainynassender@gmail.com");
                    m.setSubject("Pataisyta daina " + daina.pavadinimas);
                    m.setBody(daina.pavadinimas);

                    try {
                        m.addAttachment(file.getCanonicalPath(), file.getName());

                        if (!m.send()) {
                            Toast.makeText(EditDaina.this, "Įvyko klaida", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(EditDaina.this, "Įvyko klaida", Toast.LENGTH_LONG).show();
                        Log.e("EditDaina", "Could not send email", e);
                    }
                    file.deleteOnExit();
                }
            }
        });

        ImageButton deleteDaina = (ImageButton) findViewById(R.id.deleteEditedDaina);
        deleteDaina.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
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
        if (!puslapis.getText().toString().isEmpty()){
            dainaViewModel.puslapis = Integer.parseInt(puslapis.getText().toString());
        }else {
            dainaViewModel.puslapis = 0;
        }
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
    public boolean check () {
        if (pavadinimas.getText().toString().isEmpty()){
            Toast.makeText(EditDaina.this, "Įveskite dainos pavadinimą", Toast.LENGTH_LONG).show();
            return false;
        }else if (zodziai.getText().toString().isEmpty()){
            Toast.makeText(EditDaina.this, "Įveskite dainos žodžius", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this, R.style.YourDialogStyle)
                //set message, title, and icon
                .setTitle("Atsargiai!")
                .setMessage("Ar tikrai norite ištrinti šią dainą?")

                //.setIcon(R.drawable.delete)

                .setPositiveButton("Taip", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        daina = Daina.load(Daina.class, ids[i]);
                        List<Posmelis> posmeliai = daina.posmeliai();
                        for (Posmelis posmelis: posmeliai
                                ) {
                            posmelis.delete();
                        }
                        daina.delete();
                        createIds();
                        --i;
                        if (i == -1){
                            i = i + 1;
                            if (i == (int) maxId){ // Šiuo atveju vis tiek nulūš
                                Toast.makeText(EditDaina.this, "Jūs ištrynėte visas dainas", Toast.LENGTH_LONG).show();
                            }
                        }
                        fillView(ids[i]);
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("Atšaukti", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
    public void createIds (){
        maxId = new Select().from(Daina.class).count();
       // Cursor cursor = Cache.openDatabase().rawQuery(query, null);
        //cursor.moveToFirst();
       // maxId = cursor.getLong(0);
        ids = new long[(int) maxId];
        j = i;  //cia kvailas sprendimas, bet dabar per daug pavarges, kad galvociau
        String query = new Select("Id").from(Daina.class).orderBy("Pavadinimas").toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        ids[0] = cursor.getLong(0);
        for (i=1; i < maxId; i++){
            cursor.moveToNext();
            ids[i] = cursor.getLong(0);
        }
        i=j;
    }
}

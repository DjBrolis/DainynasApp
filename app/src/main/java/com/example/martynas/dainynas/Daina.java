package com.example.martynas.dainynas;

import android.content.Context;
import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Martynas on 2016-08-31.
 */
@Table(name = "Dainos")
public class Daina extends Model{
    @Column(name = "Pavadinimas")
    public String pavadinimas;
    @Column(name = "Zodziai")
    public String zodziai;
    @Column (name = "Vertimas")
    public String vertimas;
    @Column(name = "Favorite")
    public int favorite;
    @Column (name = "Puslapis")
    public int puslapis;

    public List<Posmelis> posmeliai () {
        return getMany(Posmelis.class,"Daina");
    }

    public Daina(){
        super();
    }

    public Daina (DainaViewModel dainaViewModel){
        super();
        Daina dainaTemp = new Daina();
        dainaTemp.pavadinimas = dainaViewModel.pavadinimas;
        dainaTemp.vertimas = dainaViewModel.vertimas;
        dainaTemp.zodziai = LietRaidPanaik(dainaViewModel.pavadinimas);
        String[] zodziaiTemp = dainaViewModel.zodziai.trim().split("\n");
        Posmelis posmelisTemp = new Posmelis();
        posmelisTemp.daina = dainaTemp;
        dainaTemp.save();

        boolean naujasStulpelis = false;
        for (String eilute: zodziaiTemp
                ) {
            if (eilute.isEmpty() && !posmelisTemp.zodziai.isEmpty()) {
                posmelisTemp.daina = dainaTemp;
                posmelisTemp.save();
                naujasStulpelis = true;
            }
            else if (naujasStulpelis && !eilute.isEmpty()){
                naujasStulpelis = false;
                posmelisTemp = new Posmelis();
                posmelisTemp.daina = dainaTemp;
                posmelisTemp.zodziai += eilute + "\n";
            }
            else if (!eilute.isEmpty()){
                posmelisTemp.zodziai += eilute + "\n";
            }
            else {
                posmelisTemp.daina = dainaTemp;
                posmelisTemp.save();
            }

        }
        posmelisTemp.daina = dainaTemp;
        posmelisTemp.save();
        dainaTemp.save();

    }

    // This constructor takes raw String from file where songs are divided by "\t\t" and song
    // properties are divided by "\t"
    public Daina (String output){
        String[] dainos = output.split("\t\t");
        ActiveAndroid.beginTransaction();
        try {
            for (String daina:dainos
                    ) {
                String[] pavadinimasIrZodziai = daina.trim().split("\t");
                Daina dainaTemp = new Daina();
                dainaTemp.favorite = 0;  // pakeisti
                dainaTemp.pavadinimas = pavadinimasIrZodziai[0].trim();
                dainaTemp.zodziai = LietRaidPanaik(dainaTemp.pavadinimas);
                if (pavadinimasIrZodziai.length > 4) {
                    dainaTemp.vertimas = pavadinimasIrZodziai[4].trim();
                }
                else {
                    dainaTemp.vertimas = "";
                }
                String[] zodziaiTemp = pavadinimasIrZodziai[3].trim().split("\r\n");
                Posmelis posmelisTemp = new Posmelis();
                posmelisTemp.daina = dainaTemp;
                dainaTemp.save();

                boolean naujasStulpelis = false;
                for (String eilute: zodziaiTemp
                        ) {
                    if (eilute.isEmpty() && !posmelisTemp.zodziai.isEmpty()) {
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.save();
                        naujasStulpelis = true;
                    }
                    else if (naujasStulpelis && !eilute.isEmpty()){
                        naujasStulpelis = false;
                        posmelisTemp = new Posmelis();
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.zodziai += eilute + "\n";
                    }
                    else if (!eilute.isEmpty()){
                        posmelisTemp.zodziai += eilute + "\n";
                    }
                }
                posmelisTemp.daina = dainaTemp;
                posmelisTemp.save();
                dainaTemp.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public Daina (String output, Context context) //this is used only to generate file with all songs
    {
        String[] dainos = output.split("\t\t");
        ActiveAndroid.beginTransaction();
        try {
            for (String daina:dainos
                    ) {
                String[] pavadinimasIrZodziai = daina.trim().split("\t");
                Daina dainaTemp = new Daina();
                dainaTemp.favorite = 0;  // pakeisti
                dainaTemp.pavadinimas = pavadinimasIrZodziai[0].trim();
                dainaTemp.zodziai = LietRaidPanaik(dainaTemp.pavadinimas);
                if (pavadinimasIrZodziai.length > 4) {
                    dainaTemp.vertimas = pavadinimasIrZodziai[4].trim();
                }
                else {
                    dainaTemp.vertimas = "";
                }
                String[] zodziaiTemp = pavadinimasIrZodziai[3].trim().split("\r\n");
                Posmelis posmelisTemp = new Posmelis();
                posmelisTemp.daina = dainaTemp;
                dainaTemp.save();

                boolean naujasStulpelis = false;
                for (String eilute: zodziaiTemp
                        ) {
                    if (eilute.isEmpty() && !posmelisTemp.zodziai.isEmpty()) {
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.save();
                        naujasStulpelis = true;
                    }
                    else if (naujasStulpelis && !eilute.isEmpty()){
                        naujasStulpelis = false;
                        posmelisTemp = new Posmelis();
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.zodziai += eilute + "\n";
                        posmelisTemp.zodziaiOnlyENLetters += LietRaidPanaik(eilute) + "\n";
                    }
                    else if (!eilute.isEmpty()){
                        posmelisTemp.zodziai += eilute + "\n";
                        posmelisTemp.zodziaiOnlyENLetters += LietRaidPanaik(eilute) + "\n";
                    }
                }
                posmelisTemp.daina = dainaTemp;
                posmelisTemp.save();
                dainaTemp.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
        StringBuilder builder = new StringBuilder();
        String query = new Select("Dainos" + ".*, " + "Dainos" + ".Id as _id").from(Daina.class).orderBy("_id DESC").limit(1).toSql();
        Cursor cursor = Cache.openDatabase().rawQuery(query,null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex("Id"));
        for (long i = 1; i <= id; i++){
            Daina daina = Daina.load(Daina.class, i);
            List<Posmelis> posmeliai = daina.posmeliai();
            String zodziai = "";
            String zodziaiOnlyENLetters = "";
            for (Posmelis posmelis:posmeliai
                    ) {zodziai += posmelis.zodziai + "\r\n\r\n";
                zodziaiOnlyENLetters += posmelis.zodziaiOnlyENLetters + "\r\n\r\n";
            }
            builder.append(daina.pavadinimas + "\t\r\n\r\n"
                    +daina.zodziai + "\t\r\n\r\n"
                    +daina.vertimas + "\t\r\n\r\n"
                    +daina.puslapis + "\t\r\n\r\n"
                    +zodziai + "\t\r\n"
                    +zodziaiOnlyENLetters + "\t\t\r\n\r\n");
        }
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, "dainosGen.txt");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(builder.toString().getBytes());
            stream.close();
            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, context.MODE_PRIVATE));
            outputStreamWriter.write(builder.toString());
            outputStreamWriter.close();*/
        }
        catch (IOException e){
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }
    public String LietRaidPanaik (String pavadinimas){
        char [] temp = pavadinimas.toLowerCase().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char raide : temp){
            switch (raide){
                case 'ą':
                    builder.append('a');
                    break;
                case 'à':
                    builder.append('a');
                    break;
                case 'ę':
                    builder.append('e');
                    break;
                case 'ė':
                    builder.append('e');
                    break;
                case 'è':
                    builder.append('e');
                    break;
                case 'ū':
                    builder.append('u');
                    break;
                case 'ù':
                    builder.append('u');
                    break;
                case 'ų':
                    builder.append('u');
                    break;
                case 'č':
                    builder.append('c');
                    break;
                case 'š':
                    builder.append('s');
                    break;
                case 'ž':
                    builder.append('z');
                    break;
                case 'į':
                    builder.append('i');
                    break;
                case 'ò':
                    builder.append('o');
                    break;
                default:
                    builder.append(raide);
                    break;
            }
        }
        return builder.toString();
    }
}

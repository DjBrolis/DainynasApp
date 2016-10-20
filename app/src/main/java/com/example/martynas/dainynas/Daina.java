package com.example.martynas.dainynas;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.example.martynas.dainynas.Pages.HomePage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Martynas on 2016-08-31.
 */
@Table(name = "Dainos")
public class Daina extends Model{
    @Column(name = "Pavadinimas")
    public String pavadinimas;
    @Column(name = "PavOnlyENLetters")
    public String pavOnlyENLetters;
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
        dainaTemp.pavOnlyENLetters = LietRaidPanaik(dainaViewModel.pavadinimas);
        dainaTemp.puslapis = dainaViewModel.puslapis;
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
                dainaTemp.pavOnlyENLetters = LietRaidPanaik(dainaTemp.pavadinimas);
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
                Log.d(dainaTemp.pavadinimas + "sugedo");
                dainaTemp.pavOnlyENLetters = pavadinimasIrZodziai[1].trim();
                dainaTemp.vertimas = pavadinimasIrZodziai[2].trim();
                dainaTemp.puslapis = Integer.parseInt(pavadinimasIrZodziai[3].trim());
                String[] zodziaiTemp = pavadinimasIrZodziai[4].trim().split("\r\n");
                String[] zodziaiOnlyENTemp = pavadinimasIrZodziai[5].trim().split("\r\n");
                if (zodziaiOnlyENTemp.length != zodziaiTemp.length){
                    Toast.makeText(context, dainaTemp.pavadinimas, Toast.LENGTH_LONG).show();
                }
                Posmelis posmelisTemp = new Posmelis();
                posmelisTemp.daina = dainaTemp;
                dainaTemp.save();

                boolean naujasStulpelis = false;

                for (int i = 0; i<zodziaiTemp.length && i<zodziaiOnlyENTemp.length; i++){
                    if (zodziaiTemp[i].isEmpty() && !posmelisTemp.zodziai.isEmpty()) {
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.save();
                        naujasStulpelis = true;
                    }
                    else if (naujasStulpelis && !zodziaiTemp[i].isEmpty()){
                        naujasStulpelis = false;
                        posmelisTemp = new Posmelis();
                        posmelisTemp.daina = dainaTemp;
                        posmelisTemp.zodziai += zodziaiTemp[i] + "\n";
                        posmelisTemp.zodziaiOnlyENLetters += zodziaiOnlyENTemp[i] + "\n";
                    }
                    else if (!zodziaiTemp[i].isEmpty()){
                        posmelisTemp.zodziai += zodziaiTemp[i] + "\n";
                        posmelisTemp.zodziaiOnlyENLetters += zodziaiOnlyENTemp[i] + "\n";
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

    public void updateDaina (Daina dainaTemp, DainaViewModel dainaViewModel){
        dainaTemp.pavadinimas = dainaViewModel.pavadinimas;
        dainaTemp.vertimas = dainaViewModel.vertimas;
        dainaTemp.pavOnlyENLetters = LietRaidPanaik(dainaViewModel.pavadinimas);
        dainaTemp.puslapis = dainaViewModel.puslapis;
        String[] zodziaiTemp = dainaViewModel.zodziai.trim().split("\n");
        List<Posmelis> posmeliai = dainaTemp.posmeliai();
        for (Posmelis posmelis:posmeliai
             ) {
            posmelis.delete();
        }
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

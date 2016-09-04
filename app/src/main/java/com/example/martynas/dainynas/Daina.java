package com.example.martynas.dainynas;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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
                dainaTemp.pavadinimas = pavadinimasIrZodziai[0].trim();
                String[] zodziaiTemp = pavadinimasIrZodziai[1].trim().split("\r\n");
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
}

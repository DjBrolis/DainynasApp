package com.example.martynas.dainynas;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Martynas on 2016-08-31.
 */
@Table(name = "Posmeliai")
public class Posmelis extends Model{
    @Column(name = "PosmelisId")
    public int posmelisId;
    @Column(name = "Zodziai")
    public String zodziai;
    @Column(name = "DainaiD")
    public int dainaId;
    @Column(name = "Daina")
    public Daina daina;
    @Column (name = "ZodziaiOnlyENLetters")
    public String zodziaiOnlyENLetters;


    public Posmelis (){
        super();
        this.zodziai = "";
        this.zodziaiOnlyENLetters = "";
    }

    public Posmelis (int posmelisId, String zodziai, int dainaId, Daina daina, String zodziaiOnlyENLetters){
        this.posmelisId = posmelisId;
        this.zodziai = zodziai;
        this.dainaId = dainaId;
        this.daina = daina;
        this.zodziaiOnlyENLetters = zodziaiOnlyENLetters;
    }

}

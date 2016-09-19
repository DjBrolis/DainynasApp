package com.example.martynas.dainynas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.StringSearch;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;

public class StudentRepo {

    public StudentRepo() {
        super();
    }

    public Cursor getStudentList() {

        String tableName = Cache.getTableInfo(Daina.class).getTableName();
       // String selectQuery = new Select().from(Daina.class).toSql();
        String selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class).orderBy("Pavadinimas").toSql();

        Cursor cursor = Cache.openDatabase().rawQuery(selectQuery, null);
        //Open connection to read only

        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor getStudentListByKeyword(String search) {
        //Open connection to read only
        String searchAlt;
        String selectQuery;
        String tableName = Cache.getTableInfo(Daina.class).getTableName();
        if (search.length() > 0){
            searchAlt = search.substring(0,1).toUpperCase() + search.substring(1);
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                    .where("Pavadinimas  LIKE  '%"+search+"%'").or("Pavadinimas  LIKE  '%"+searchAlt+"%'").orderBy("Pavadinimas").toSql();}
        else{
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                    .where("Pavadinimas  LIKE  '%"+search+"%'").orderBy("Pavadinimas").toSql();}


        Cursor cursor = Cache.openDatabase().rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Daina getStudentById(int Id){

        Daina daina = Daina.load(Daina.class, Id);
        return daina;
    }
}
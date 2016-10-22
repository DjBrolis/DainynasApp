package com.example.martynas.dainynas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.StringSearch;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;

public class StudentRepo {
    private String selectQuery;

    public StudentRepo() {
        super();
    }

    public Cursor getStudentList(boolean favorite) {

        String tableName = Cache.getTableInfo(Daina.class).getTableName();
       // String selectQuery = new Select().from(Daina.class).toSql();
        if (!favorite){
        selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class).orderBy("PavOnlyENLetters").toSql();
        }
        else {
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class).where("Favorite=1").orderBy("PavOnlyENLetters").toSql();

        }
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

    public Cursor getStudentListByKeyword(String search, boolean favorite) {
        //Open connection to read only
        String searchAlt;
        String tableName = Cache.getTableInfo(Daina.class).getTableName();

        if (!favorite){

            if (search.length() > 0){
                searchAlt = search.substring(0,1).toUpperCase() + search.substring(1);
                selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                        .where("(Pavadinimas  LIKE " + searchQuery(search, "Pavadinimas") + ')')
                        .or("(PavOnlyENLetters  LIKE " + searchQuery(search, "PavOnlyENLetters") + ')')
                        .or("(Puslapis = '" + search.trim() + "')")
                        .orderBy("Favorite DESC, Pavadinimas ASC").toSql();}
            else{
                selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                        .where("Pavadinimas  LIKE " + searchQuery(search, "Pavadinimas")).orderBy("Pavadinimas").toSql();}
        }
        else {
            if (search.length() > 0){
                searchAlt = search.substring(0,1).toUpperCase() + search.substring(1);
                selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                        .where("(Pavadinimas  LIKE " + searchQuery(search, "Pavadinimas")+ ')')
                        .or("(PavOnlyENLetters  LIKE " + searchQuery(search, "PavOnlyENLetters") + ')')
                        .or("Puslapis = '" + search.trim() + "'").orderBy("Favorite DESC, Pavadinimas ASC").toSql();}
            else{
                selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Daina.class)
                        .where("Pavadinimas  LIKE " + searchQuery(search, "Pavadinimas")).and("Favorite=1").orderBy("Pavadinimas").toSql();}
        }


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
    public String searchQuery (String search, String columnName){
        String temp[] = search.trim().split(" |,");
        StringBuilder builder = new StringBuilder();
        for (String string : temp){
            if (builder.length() > 0) {
                builder.append("and " + columnName + " LIKE '%" + string + "%'");
            }
            else {
                builder.append("'%" + string + "%'");
            }
        }
        return builder.toString();
    }
}
package com.example.martynas.dainynas;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.query.Select;

/**
 * Created by Martynas on 2016-09-08.
 */
public class PosmelisRepo {
    private String selectQuery;
    public PosmelisRepo () {super();}

    public Cursor getPosmeliaiList(boolean favorite){
        String tableName = Cache.getTableInfo(Posmelis.class).getTableName();
        if (!favorite) {
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Posmelis.class).toSql();
        }
        else{
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id").from(Posmelis.class).where("Favorite=1").toSql();
        }
        Cursor cursor = Cache.openDatabase().rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Cursor getPosmeliaiListByKeyword(String search, boolean favorite){
        String tableName = Cache.getTableInfo(Posmelis.class).getTableName();
        if (!favorite) {
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id")
                    .from(Posmelis.class).where("Zodziai  LIKE  '%" + search + "%'").toSql();
        }
        else {
            selectQuery = new Select(tableName + ".*, " + tableName + ".Id as _id")
                    .from(Posmelis.class).where("Zodziai  LIKE  '%" + search + "%'").toSql();
        }
        //tableName = tableName.substring(0,1).toLowerCase() + tableName.substring(1);
        Cursor cursor = Cache.openDatabase().rawQuery(selectQuery, null);

        tableName = tableName.substring(1).toUpperCase();

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public Posmelis getPosmelisById(int Id){
        Posmelis posmelis = Posmelis.load(Posmelis.class, Id);
        return posmelis;
    }
}

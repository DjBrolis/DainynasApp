package com.example.martynas.dainynas.Pages;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.CustomAdapterF;
import com.example.martynas.dainynas.CustomAdapterPF;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.DainaRepo;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.PosmelisRepo;
import com.example.martynas.dainynas.R;

public class Favorites extends AppCompatActivity implements View.OnClickListener {
    private CustomAdapterF customAdapterF;
    ListView listView;
    Cursor cursor;
    DainaRepo studentRepo ;
    private final static String TAG= Favorites.class.getName().toString();
    Long dainaId;
    Long posmelisId;

    private CustomAdapterPF customAdapterPF;
    ListView listViewP;
    Cursor cursorP;
    PosmelisRepo posmelisRepo;
    Intent intent;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ActiveAndroid.initialize(this);
        intent = getIntent();

        studentRepo = new DainaRepo();
        cursor=studentRepo.getStudentList(true);
        customAdapterF = new CustomAdapterF(Favorites.this,  cursor, 0);
        listView = (ListView) findViewById(R.id.lstFavorite);
        listView.setAdapter(customAdapterF);

        posmelisRepo = new PosmelisRepo();
        //cursorP = posmelisRepo.getPosmeliaiList(true);
        customAdapterPF = new CustomAdapterPF(Favorites.this, cursorP, 0);
        listViewP = (ListView) findViewById(R.id.lstPosmeliaiF);
        listViewP.setAdapter(customAdapterPF);

        registerDainosPasirinkimas();
        registerDainosPasirinkimasP();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (s.length() < 4){
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s,true);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapterF.swapCursor(cursor);}
                    else {
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s,true);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapterF.swapCursor(cursor);

                        Log.d(TAG, "onQueryTextSubmit ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s,true);
                        if (cursorP==null){
                            Toast.makeText(Favorites.this,"No records found!",Toast.LENGTH_LONG).show();
                        }/*else{
                            Toast.makeText(SearchList.this, cursorP.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapterPF.swapCursor(cursorP);
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s.length() < 4){

                        Log.d(TAG, "onQueryTextChange ");
                        cursor=studentRepo.getStudentListByKeyword(s,true);
                        if (cursor!=null){
                            customAdapterF.swapCursor(cursor);
                        }
                        else {customAdapterF.swapCursor(null);}
                    }
                    else {
                        Log.d(TAG, "onQueryTextChange ");
                        cursor=studentRepo.getStudentListByKeyword(s,true);
                        if (cursor!=null){
                            customAdapterF.swapCursor(cursor);
                        }
                        else {customAdapterF.swapCursor(null);}

                        Log.d(TAG, "onQueryTextChange ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s,true);
                        if (cursorP!=null){
                            customAdapterPF.swapCursor(cursorP);
                        }
                        else {customAdapterPF.swapCursor(null);}
                    }
                    return false;
                }

            });

        }

        return true;

    }

    private void registerDainosPasirinkimas(){
        final ListView list = (ListView) findViewById(R.id.lstFavorite);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;

                Intent intent = new Intent(Favorites.this, ViewDaina.class);
                cursor.moveToPosition(i);
                dainaId = cursor.getLong(cursor.getColumnIndex("_id"));
                /*Cursor cursor = (Cursor) customAdapter.getItem(i);
                int dainaId = cursor.getColumnIndex("Id");*/

                intent.putExtra("Daina", dainaId);
                startActivity(intent);
            }
        });
    }

    private void registerDainosPasirinkimasP(){
        final ListView list = (ListView) findViewById(R.id.lstPosmeliaiF);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;

                Intent intent = new Intent(Favorites.this, ViewDaina.class);
                cursorP.moveToPosition(i);
                posmelisId = cursorP.getLong(cursorP.getColumnIndex("_id"));
                dainaId = Posmelis.load(Posmelis.class, posmelisId).daina.getId();
                /*Cursor cursor = (Cursor) customAdapter.getItem(i);
                int dainaId = cursor.getColumnIndex("Id");*/

                intent.putExtra("Daina", dainaId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        position = listView.getPositionForView(view);
        if (listView.getPositionForView(view) != AdapterView.INVALID_POSITION) {
            cursor.moveToPosition(position);
            Daina daina = Daina.load(Daina.class, cursor.getLong(cursor.getColumnIndex("_id")));
            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()){
                daina.favorite = 1;
            }
            else{
                daina.favorite = 0;
            }
            daina.save();
        }
        else if (listViewP.getPositionForView(view) != AdapterView.INVALID_POSITION){
            position = listViewP.getPositionForView(view);
            cursorP.moveToPosition(position);
            Posmelis posmelis = Posmelis.load(Posmelis.class, cursorP.getLong(cursorP.getColumnIndex("_id")));
            Daina daina = posmelis.daina;
            CheckBox checkBox = (CheckBox) view;
            if (checkBox.isChecked()){
                daina.favorite = 1;
            }
            else{
                daina.favorite = 0;
            }
            daina.save();
        }

    }
}

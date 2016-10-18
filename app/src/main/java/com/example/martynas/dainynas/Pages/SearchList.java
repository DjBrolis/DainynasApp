package com.example.martynas.dainynas.Pages;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.CustomAdapterP;
import com.example.martynas.dainynas.CustomAdapter;
import com.example.martynas.dainynas.Daina;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.PosmelisRepo;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.StudentRepo;

public class SearchList extends AppCompatActivity implements View.OnClickListener{

    private CustomAdapter customAdapter;
    ListView listView;
    Cursor cursor;
    StudentRepo studentRepo ;
    private final static String TAG= SearchList.class.getName().toString();
    Long dainaId;
    Long posmelisId;

    private CustomAdapterP customAdapterP;
    ListView listViewP;
    Cursor cursorP;
    PosmelisRepo posmelisRepo;
    Intent intent;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ActiveAndroid.initialize(this);
        intent = getIntent();

        studentRepo = new StudentRepo();
        cursor=studentRepo.getStudentList(false);
        customAdapter = new CustomAdapter(SearchList.this,  cursor, 0);
        listView = (ListView) findViewById(R.id.lstStudent);
        listView.setAdapter(customAdapter);

        posmelisRepo = new PosmelisRepo();
       // cursorP = posmelisRepo.getPosmeliaiList();
        customAdapterP = new CustomAdapterP(SearchList.this, cursorP, 0);
        listViewP = (ListView) findViewById(R.id.lstPosmeliai);
        listViewP.setAdapter(customAdapterP);

        registerDainosPasirinkimas();
        registerDainosPasirinkimasP();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            if (intent.getExtras().getBoolean("FocusSearch", true)) {
                MenuItemCompat.expandActionView(menu.findItem(R.id.search));
            }
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (s.length() < 4){
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s,false);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapter.swapCursor(cursor);}
                    else {
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s,false);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapter.swapCursor(cursor);

                        Log.d(TAG, "onQueryTextSubmit ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s,false);
                        if (cursorP==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }/*else{
                            Toast.makeText(SearchList.this, cursorP.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapterP.swapCursor(cursorP);
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (s.length() < 4){

                        Log.d(TAG, "onQueryTextChange ");
                        cursor=studentRepo.getStudentListByKeyword(s,false);
                        customAdapterP.swapCursor(null);
                        if (cursor!=null){
                            customAdapter.swapCursor(cursor);
                        }
                        else {customAdapter.swapCursor(null);}
                    }
                    else {
                        Log.d(TAG, "onQueryTextChange ");
                        cursor=studentRepo.getStudentListByKeyword(s,false);
                        if (cursor!=null){
                            customAdapter.swapCursor(cursor);
                        }
                        else {customAdapter.swapCursor(null);}

                        Log.d(TAG, "onQueryTextChange ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s,false);
                        if (cursorP!=null){
                            customAdapterP.swapCursor(cursorP);
                        }
                        else {customAdapterP.swapCursor(null);}
                    }
                    return false;
                }

            });

        }

        return true;

    }

    private void registerDainosPasirinkimas(){
        final ListView list = (ListView) findViewById(R.id.lstStudent);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;

                Intent intent = new Intent(SearchList.this, ViewDaina.class);
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
        final ListView list = (ListView) findViewById(R.id.lstPosmeliai);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TextView textView = (TextView) view;

                Intent intent = new Intent(SearchList.this, ViewDaina.class);
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

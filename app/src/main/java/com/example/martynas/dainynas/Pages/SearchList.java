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
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.martynas.dainynas.CustomAdapterP;
import com.example.martynas.dainynas.CustomAdapter;
import com.example.martynas.dainynas.Posmelis;
import com.example.martynas.dainynas.PosmelisRepo;
import com.example.martynas.dainynas.R;
import com.example.martynas.dainynas.StudentRepo;

public class SearchList extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        ActiveAndroid.initialize(this);
        Intent intent = getIntent();

        studentRepo = new StudentRepo();
        cursor=studentRepo.getStudentList();
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

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (s.length() < 4){
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapter.swapCursor(cursor);}
                    else {
                        Log.d(TAG, "onQueryTextSubmit ");
                        cursor=studentRepo.getStudentListByKeyword(s);
                        /*if (cursor==null){
                            Toast.makeText(SearchList.this,"No records found!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SearchList.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
                        }*/
                        customAdapter.swapCursor(cursor);

                        Log.d(TAG, "onQueryTextSubmit ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s);
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
                        cursor=studentRepo.getStudentListByKeyword(s);
                        if (cursor!=null){
                            customAdapter.swapCursor(cursor);
                        }
                        else {customAdapter.swapCursor(null);}
                    }
                    else {
                        Log.d(TAG, "onQueryTextChange ");
                        cursor=studentRepo.getStudentListByKeyword(s);
                        if (cursor!=null){
                            customAdapter.swapCursor(cursor);
                        }
                        else {customAdapter.swapCursor(null);}

                        Log.d(TAG, "onQueryTextChange ");
                        cursorP=posmelisRepo.getPosmeliaiListByKeyword(s);
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
                dainaId = cursor.getLong(4);
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
                posmelisId = cursorP.getLong(5);
                dainaId = Posmelis.load(Posmelis.class, posmelisId).daina.getId();
                /*Cursor cursor = (Cursor) customAdapter.getItem(i);
                int dainaId = cursor.getColumnIndex("Id");*/

                intent.putExtra("Daina", dainaId);
                startActivity(intent);
            }
        });
    }
}

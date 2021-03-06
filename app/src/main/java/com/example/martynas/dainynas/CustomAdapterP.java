package com.example.martynas.dainynas;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.martynas.dainynas.Pages.SearchList;

/**
 * Created by Martynas on 2016-09-08.
 */
public class CustomAdapterP extends CursorAdapter {

    private LayoutInflater mInflater;

    public CustomAdapterP(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        ViewHolder holder  =   new ViewHolder();
        holder.txtId = (TextView) view.findViewById(R.id.txtId);
        holder.txtName = (TextView) view.findViewById(R.id.txtName);
        holder.txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        holder.favoriteCheckBox = (CheckBox) view.findViewById(R.id.favoriteCheckBox);
        view.setTag(holder);

        holder.favoriteCheckBox.setOnClickListener((SearchList) context);
        //holder.checkBox.setOnCheckedChangeListener((SearchList) context);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //If you want to have zebra lines color effect uncomment below code
        /*if(cursor.getPosition()%2==1) {
             view.setBackgroundResource(R.drawable.item_list_backgroundcolor);
        } else {
            view.setBackgroundResource(R.drawable.item_list_backgroundcolor2);
        }*/

        ViewHolder holder  =   (ViewHolder)    view.getTag();

        holder.txtId.setText(cursor.getString(cursor.getColumnIndex("Id")));
        holder.txtName.setText(Daina.load(Daina.class, cursor.getLong(cursor.getColumnIndex("Daina"))).pavadinimas);

        holder.txtEmail.setText(cursor.getString(cursor.getColumnIndex("Zodziai")));

    }

    static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtEmail;
        CheckBox favoriteCheckBox;
    }


}

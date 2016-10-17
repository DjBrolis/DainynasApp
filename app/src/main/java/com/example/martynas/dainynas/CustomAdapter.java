package com.example.martynas.dainynas;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.martynas.dainynas.Pages.SearchList;

public class CustomAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View view    =    mInflater.inflate(R.layout.item, parent, false);
        final ViewHolder holder  =   new ViewHolder();
        holder.txtId    =   (TextView)  view.findViewById(R.id.txtId);
        holder.txtName    =   (TextView)  view.findViewById(R.id.txtName);
        holder.txtEmail   =   (TextView)  view.findViewById(R.id.txtEmail);
        holder.favoriteCheckBox = (CheckBox) view.findViewById(R.id.favoriteCheckBox);
        view.setTag(holder);

        holder.favoriteCheckBox.setOnClickListener((SearchList) context);

       // holder.favoriteCheckBox.setOnCheckedChangeListener((SearchList) context);
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
       /* holder.txtId.setText("ID");
        holder.txtName.setText("Pavadinimas");
        holder.txtEmail.setText("Zodziai");*/

        holder.txtId.setText(cursor.getString(cursor.getColumnIndex("Id")));
        holder.txtName.setText(cursor.getString(cursor.getColumnIndex("Pavadinimas")));
        holder.txtEmail.setText("");
        if (cursor.getInt(cursor.getColumnIndex("Favorite")) == 1){
            holder.favoriteCheckBox.setChecked(true);
        }
        else {
            holder.favoriteCheckBox.setChecked(false);
        }
    }

    static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtEmail;
        CheckBox favoriteCheckBox;
    }
}
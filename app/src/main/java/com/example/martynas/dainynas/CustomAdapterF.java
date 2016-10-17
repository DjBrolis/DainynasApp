package com.example.martynas.dainynas;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.martynas.dainynas.Pages.Favorites;

public class CustomAdapterF extends CursorAdapter {
    private LayoutInflater mInflater;

    public CustomAdapterF(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        View view    =    mInflater.inflate(R.layout.item_favorite, parent, false);
        final ViewHolder holder  =   new ViewHolder();
        holder.txtIdFavorite    =   (TextView)  view.findViewById(R.id.txtIdFavorite);
        holder.txtPavadinimas    =   (TextView)  view.findViewById(R.id.txtPavadinimas);
        holder.txt1   =   (TextView)  view.findViewById(R.id.txt1);
        holder.favoriteCheckBoxF = (CheckBox) view.findViewById(R.id.favoriteCheckBoxF);
        view.setTag(holder);

        holder.favoriteCheckBoxF.setOnClickListener((Favorites) context);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.txtIdFavorite.setText(cursor.getString(cursor.getColumnIndex("_id")));
        holder.txtPavadinimas.setText(cursor.getString(cursor.getColumnIndex("Pavadinimas")));
        holder.txt1.setText("");
        if (cursor.getInt(cursor.getColumnIndex("Favorite")) == 1){
            holder.favoriteCheckBoxF.setChecked(true);
        }
        else {
            holder.favoriteCheckBoxF.setChecked(false);
        }
    }

    static class ViewHolder {
        TextView txtIdFavorite;
        TextView txtPavadinimas;
        TextView txt1;
        CheckBox favoriteCheckBoxF;
    }
}

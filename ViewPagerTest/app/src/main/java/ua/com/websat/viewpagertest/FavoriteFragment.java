package ua.com.websat.viewpagertest;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Sat on 24.09.2014.
 */
public class FavoriteFragment extends Fragment  implements CompoundButton.OnCheckedChangeListener {

    private ItemSearchAdapter itemFavoriteAdapter;
    private ArrayList<SearchItem> favoriteItems;
    private FavoritesListener favoritesListener;
    DBHelper dbHelper;

    public void setData(ArrayList<SearchItem> items) {
        this.favoriteItems = items;
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    public void addFavorite(SearchItem item){
        boolean exist = false;
        for (SearchItem i: favoriteItems)
            if (i.getTitle() == item.getTitle()) exist = true;

        if (!exist) {
            itemFavoriteAdapter.add(item);
            itemFavoriteAdapter.notifyDataSetChanged();
        }
    }

    public void removeFavorite(SearchItem item){
        itemFavoriteAdapter.remove(item);
        itemFavoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            favoritesListener = (FavoritesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " should implement " + FavoritesListener.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

//        ArrayList<SearchItem> favoriteItems = new ItemSearchAdapter(this.getActivity(), searchItems).getFavorites();
        itemFavoriteAdapter = new ItemSearchAdapter(this.getActivity(), favoriteItems, this);


        ListView listView = (ListView) rootView.findViewById(R.id.listview_favorite);
        listView.setAdapter(itemFavoriteAdapter);

        dbHelper = new DBHelper(getActivity());
        dbHelper.getWritableDatabase();

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SearchItem item = itemFavoriteAdapter.getItem((Integer) buttonView.getTag());
        item.setFavorite(isChecked);

        if (!isChecked) {
            favoritesListener.remove(item);
        }
    }


    public void saveToDB() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (SearchItem item: favoriteItems) {
            cv.put("title", item.getTitle());
            cv.put("image", item.getImage());
            cv.put("thumbnail", item.getThumbnail());

            db.insert("favorites", null, cv);
        }
//        dbHelper.close();
        db.close();
        progressDialog.dismiss();
    }
    public void loadFromDB() {
//        ContentValues cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(this.getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("favorites", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int colTitleIndex = cursor.getColumnIndex("title");
            int colImageIndex = cursor.getColumnIndex("image");
            int colThumbnailIndex = cursor.getColumnIndex("thumbnail");

            do {
                String title = cursor.getString(colTitleIndex);
                String image = cursor.getString(colImageIndex);
                String thumbnail = cursor.getString(colThumbnailIndex);
                favoriteItems.add(new SearchItem(title, image, thumbnail, true));
            } while (cursor.moveToNext());
            itemFavoriteAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
//        dbHelper.close();
    }

    public void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favorites", null, null);
        db.close();
    }
}

package ua.com.websat.viewpagertest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sat on 25.09.2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, "FavoritesDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        db.execSQL("create table favorites ("
        + "id integer primary key autoincrement,"
        + "title text,"
        + "image text,"
        + "thumbnail text"
        + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

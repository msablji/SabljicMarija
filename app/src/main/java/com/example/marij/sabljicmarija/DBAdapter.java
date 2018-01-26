package com.example.marij.sabljicmarija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by marij on 26.1.2018..
 */

public class DBAdapter {
    static final String KEY_ROWID = "id";
    static final String KEY_DATUM = "datum";
    static final String KEY_TERMIN = "termin";
    static final String KEY_DOGADJAJ = "dogadjaj";

    static final String KEY_IDVRSTE = "id_vrste";
    static final String KEY_KATEGORIJA = "kategorija";

    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE1 = "rokovnik";
    static final String DATABASE_TABLE2 = "vrsta";
    static final int DATABASE_VERSION = 4;

    static final String DATABASE_CREATE_ROKOVNIK_TABLE=
            "create table rokovnik (id integer primary key autoincrement, "
                    + "datum text not null, termin text not null, dogadjaj text not null);";

    static final String DATABASE_CREATE_VRSTA_TABLE=
            "create table vrsta (id_vrste integer primary key autoincrement, "
                    + "id integer not null, "
                    + "kategorija text not null);";


    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE_ROKOVNIK_TABLE);
                db.execSQL(DATABASE_CREATE_VRSTA_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS rokovnik");
            db.execSQL("DROP TABLE IF EXISTS vrsta");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertRokovnik(String datum, String termin, String dogadjaj)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DATUM ,datum);
        initialValues.put(KEY_TERMIN, termin);
        initialValues.put(KEY_DOGADJAJ, dogadjaj);
        return db.insert(DATABASE_TABLE1, null, initialValues);
    }

    //--insert address into table--
    public long insertVrsta(Integer kategorija,  Integer id)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, id);
        initialValues.put(KEY_KATEGORIJA, kategorija);

        return db.insert(DATABASE_TABLE2, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteRokovnik(long rowId)
    {
        return db.delete(DATABASE_TABLE1, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //--deletes address for contact--
    public boolean deleteVrsta(long rowId)
    {
        return db.delete(DATABASE_TABLE2, KEY_ROWID + "=" + rowId, null) > 0;
    }


    //---retrieves all the contacts---
    public Cursor getAllRokovnik()
    {
        return db.query(DATABASE_TABLE1, new String[] {KEY_ROWID, KEY_DATUM,
                KEY_TERMIN, KEY_DOGADJAJ}, null, null, null, null, null);
    }

    //---retrieves all the addresses---
    public Cursor getAllVrsta()
    {
        return db.query(DATABASE_TABLE2, new String[] {KEY_IDVRSTE, KEY_KATEGORIJA,
                KEY_ROWID}, null, null, null, null,null);
    }





    public Cursor getZadani(String datu) throws SQLException
    {
        Cursor mCursor =
                db.query( DATABASE_TABLE1, new String[] {KEY_ROWID, KEY_DATUM,
                                KEY_TERMIN, KEY_DOGADJAJ}, KEY_DATUM + "=" + datu, null,
                        null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


}

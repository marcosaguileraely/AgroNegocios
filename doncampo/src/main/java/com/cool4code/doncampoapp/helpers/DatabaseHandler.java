package com.cool4code.doncampoapp.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COOL4CODE TEAM @cool4code on 9/28/14.
 * Indeed DonCampo Team
 * David Alméciga @dagrinchi
 * Marcos Aguilera @marcode_ely
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "placitadb";
    // Labels table name
    private static final String TABLE_NAME = "units";
    // Labels Table Columns names

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Getting all labels
     * returns list of labels
     * */

     public List <SpinnerObject> getAllLabels(String table_name){
        List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                labels.add ( new SpinnerObject ( cursor.getInt(0) , cursor.getString(1) , cursor.getString(2) ));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning labels
        return labels;
    }

    public List <SpinnerObject> getAllProducts(String table_name){
        List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                labels.add ( new SpinnerObject ( cursor.getInt(0), cursor.getString(1), cursor.getString(2) ));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning labels
        return labels;
    }

    public String getAuth(String table_name){
        String selectQuery = "SELECT  access_token FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String token_str = null;
        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                token_str = String.valueOf(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();
        // returning labels
        return token_str;
    }

    public long validateExpiresAt(String table_name){
        String selectQuery = "SELECT  miliExpires FROM " + table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        long millis = 0;

        if ( cursor.moveToFirst () ) {
            do {
                millis = cursor.getLong(0);
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        return millis;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*not using yet*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*not using yet*/
    }
}

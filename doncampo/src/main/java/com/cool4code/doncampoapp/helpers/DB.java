package com.cool4code.doncampoapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by COOL4CODE TEAM @cool4code on 9/28/14.
 * Indeed DonCampo Team
 * Created by Marcos Aguilera Ely @marcode_ely on 10/19/14.
 * Created by David Alméciga @dagrinchi on 10/19/14.
 */
public class DB extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Agronegocios.db";

    private ArrayList<HashMap> tables;

    public DB(Context context, ArrayList<HashMap> tables) {
        super(context, DATABASE_NAME, null, 1);
        this.tables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableName = null;
        JSONArray tableData = null;

        for (HashMap table : tables) {
            Set<Entry> ent = table.entrySet();
            for (Entry e : ent) {
                tableName = (String) e.getKey();
                tableData = (JSONArray) e.getValue();
            }
            String tableFields = join(getTableFields(tableData), ", ");
            String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (autoId INTEGER PRIMARY KEY AUTOINCREMENT, " + tableFields + ")";
            db.execSQL(sql);

            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < tableData.length(); i++) {
                try {
                    JSONObject jsonObject = tableData.getJSONObject(i);
                    for (String tf : getTableFields(tableData)) {
                        contentValues.put(tf, jsonObject.getString(tf));
                    }
                    db.insert(tableName, null, contentValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (HashMap table : this.tables) {
            String tableName = (String) table.get("tableName");
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }
        this.onCreate(db);
    }

    public void initDataTable(HashMap hmTable) {
        String tableName = null;
        JSONArray tableData = null;

        Set<Map.Entry> ent = hmTable.entrySet();
        for (Map.Entry e : ent) {
            tableName = (String) e.getKey();
            tableData = (JSONArray) e.getValue();
        }
        if (tableData != null) {
            emptyData(tableName);
            insertData(tableName, tableData);
        }
    }

    public void emptyData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{tableName});
    }

    public void insertData(String tableName, JSONArray tableData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < tableData.length(); i++) {
            try {
                JSONObject jsonObject = tableData.getJSONObject(i);
                for (String tf : getTableFields(tableData)) {
                    contentValues.put(tf, jsonObject.getString(tf));
                }
                db.insert(tableName, null, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        db.close();
    }

    public void updateData(String tableName, JSONArray tableData, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < tableData.length(); i++) {
            try {
                JSONObject jsonObject = tableData.getJSONObject(i);
                for (String tf : getTableFields(tableData)) {
                    contentValues.put(tf, jsonObject.getString(tf));
                }
                db.update(tableName, contentValues, "autoId =" + id, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        db.close();
    }

    public ArrayList<HashMap> getAllData(String tableName) {
        ArrayList data = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + tableName, null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap obj = new HashMap();
            for (int j=0; j < res.getColumnCount(); j++) {
                obj.put(res.getColumnName(j), res.getString(res.getColumnIndex(res.getColumnName(j))));
            }
            data.add(obj);
            res.moveToNext();
        }
        res.close();
        return data;
    }

    public ArrayList<HashMap> getDataByName(String tableName, String column, String value) {
        ArrayList<HashMap> data = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        /*query(String table, String[] columns, String selection,
                String[] selectionArgs, String groupBy, String having,
                String orderBy)*/
        Cursor res = db.query(tableName,
                new String[] { },
                column + " LIKE ? ",
                new String[] { "%" + String.valueOf(value) + "%"},
                null, null, null);
        if (res != null)
            res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap obj = new HashMap();
            for (int j=0; j < res.getColumnCount(); j++) {
                obj.put(res.getColumnName(j), res.getString(res.getColumnIndex(res.getColumnName(j))));
            }
            data.add(obj);
            res.moveToNext();
        }
        res.close();
        return data;
    }

    public ArrayList<HashMap> getDataByValue(String tableName, String column, String value) {
        ArrayList<HashMap> data = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.query(tableName, new String[] { }, column + "=?",
                new String[] { String.valueOf(value) }, null, null, null, null);
        if (res != null)
            res.moveToFirst();
        while(res.isAfterLast() == false){
            HashMap obj = new HashMap();
            for (int j=0; j < res.getColumnCount(); j++) {
                obj.put(res.getColumnName(j), res.getString(res.getColumnIndex(res.getColumnName(j))));
            }
            data.add(obj);
            res.moveToNext();
        }
        res.close();
        return data;
    }

    private ArrayList<String> getTableFields(JSONArray tableData) {
        ArrayList tableFields = new ArrayList();
        for (int i = 0; i < tableData.length(); i++) {
            try {
                JSONObject jsonObject = tableData.getJSONObject(i);
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    tableFields.add(String.valueOf(it.next()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
        return tableFields;
    }

    private String join(ArrayList<String> r, String delimiter) {
        if(r == null || r.size() == 0 ){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int i, len = r.size() - 1;
        for (i = 0; i < len; i++){
            sb.append(r.get(i) + delimiter);
        }
        return sb.toString() + r.get(i);
    }

}

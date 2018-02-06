package com.fall.devs.chute;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BaseDeDonnees extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "User_table";
    public static final String COL_1 = "Num_sec";
    public static final String COL_2 = "Nom";
    public static final String COL_3 = "Prenom";
    public static final String COL_4 = "Date_naissance";
    public static final String COL_5 = "Tel";
    public static final String COL_6 = "Tel_med";

    public BaseDeDonnees(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (Num_sec TEXT PRIMARY KEY ,Nom TEXT,Prenom TEXT,Date_naissance TEXT,Tel TEXT,Tel_med TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public String getNumberPhone(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curosor= db.rawQuery("select * from "+TABLE_NAME,null);
        String numero=curosor.getString(curosor.getColumnIndex(COL_5));
        return numero;
    }


    public boolean insertData(String Num_sec,String Nom,String Prenom,String Date_naissance,String Tel,String Tel_med) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,Num_sec);
        contentValues.put(COL_2,Nom);
        contentValues.put(COL_3,Prenom);
        contentValues.put(COL_4,Date_naissance);
        contentValues.put(COL_5,Tel);
        contentValues.put(COL_6,Tel_med);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
}

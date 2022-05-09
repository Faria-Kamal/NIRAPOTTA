package com.example.nirapotta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class MyDBfunctions2 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "mydb2";
    private static final String TABLE_NAME= "mytab2";

    private static final String TAB_ID= "id2";
    private static final String TAB_UNAME= "uname";


    MyDBfunctions2(Context c){
        super(c,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String s = "CREATE TABLE "+TABLE_NAME+" ("+TAB_ID+" INTEGER PRIMARY KEY, "+TAB_UNAME+" TEXT)";
        sqLiteDatabase.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void addingDatatoTable(DataTemp dt){

        SQLiteDatabase sqd = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(TAB_UNAME,dt.getUname());

        sqd.insert(TABLE_NAME,null,cv);
        sqd.close();
    }

    String fetch_info() {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT "+TAB_UNAME+" FROM "+TABLE_NAME+" WHERE "+TAB_ID+" = "+1;

        Cursor c = sq.rawQuery(q, null);
        String s = "";

        c.moveToFirst();

        if(c.moveToFirst()) {
            s = c.getString(c.getColumnIndex(TAB_UNAME+""));
        }

        return s;

    }

}

package com.example.nirapotta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBFunctions extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "mydb";
    private static final String TABLE_NAME= "mytab";

    private static final String TAB_ID= "id";
    private static final String TAB_NAME= "name";
    private static final String TAB_PHONE= "phone";

    MyDBFunctions(Context c){
        super(c,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String s = "CREATE TABLE "+TABLE_NAME+" ("+TAB_ID+" INTEGER PRIMARY KEY, "+TAB_NAME+" TEXT, "+TAB_PHONE+" TEXT)";
        sqLiteDatabase.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void addingDatatoTable(DataTemp dt){

        SQLiteDatabase sqd = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(TAB_NAME,dt.getName());
        cv.put(TAB_PHONE,dt.getphone());

        sqd.insert(TABLE_NAME,null,cv);
        sqd.close();
    }

    String[] my_data(){
        SQLiteDatabase sq= this.getReadableDatabase();
        String q= "SELECT * FROM "+TABLE_NAME;
        Cursor c= sq.rawQuery(q,null);
        String[] received_data = new String[c.getCount()];

        c.moveToFirst();
        if(c.moveToFirst())
        {
            int counter =0;
            do{
                received_data[counter]=c.getString(c.getColumnIndex(TAB_NAME+"")) +"\nPhone Number: "+
                        c.getString(c.getColumnIndex(TAB_PHONE+""));
                counter= counter+1;

            }while(c.moveToNext());
        }
        return received_data;
    }

    String[] fetch_phone_number() {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT "+TAB_PHONE+" FROM "+TABLE_NAME;

        Cursor c = sq.rawQuery(q, null);
        String[] received_data2 = new String[c.getCount()];

        c.moveToFirst();

        if(c.moveToFirst()) {
            int counter =0;
            do{
                received_data2[counter]=(c.getString(c.getColumnIndex(TAB_PHONE+"")));
                counter= counter+1;

            }while(c.moveToNext());
        }

        return received_data2;

    }

    String fetch_phone(int id) {

        SQLiteDatabase sq = this.getReadableDatabase();

        String q = "SELECT "+TAB_PHONE+" FROM "+TABLE_NAME+" WHERE "+TAB_ID+" = "+id;

        Cursor c = sq.rawQuery(q, null);
        String s = "";

        c.moveToFirst();

        if(c.moveToFirst()) {
            s = c.getString(c.getColumnIndex(TAB_PHONE+""));
        }

        return s;

    }


    int update_phone(int id, String bday) {

        SQLiteDatabase sq = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TAB_PHONE, bday);

        return sq.update(TABLE_NAME, cv, TAB_ID+" = ? ", new String[]{id+""});

    }


    int delete_phone(String bday){

        SQLiteDatabase s = this.getWritableDatabase();

        return s.delete(TABLE_NAME, TAB_PHONE+" = ?", new String[] {bday});

    }


}

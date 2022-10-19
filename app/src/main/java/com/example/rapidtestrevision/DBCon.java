package com.example.rapidtestrevision;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class DBCon extends SQLiteOpenHelper {
    public DBCon(@Nullable Context context) {
        super(context, "rtr.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table rtrData(PV TEXT not null,Date TEXT not null,Physics INTEGER not null,Chemistry INTEGER not null,Botany INTEGER not null,Zoology INTEGER not null,Mistake TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists rtr.db");
    }
    public Boolean insertData(String Date,int physics,int chemistry,int botany,int zoology,String mistake) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (mistake == "" || mistake == null) {
            mistake = "No Mistakes Encountered";
        }
        if (Date != "") {
            contentValues.put("PV", Date.substring(0, 5) + String.valueOf(physics) + String.valueOf(chemistry) + String.valueOf(botany) + String.valueOf(zoology));
            contentValues.put("Date", Date);
            contentValues.put("Physics", physics);
            contentValues.put("Chemistry", chemistry);
            contentValues.put("Botany", botany);
            contentValues.put("Zoology", zoology);
            contentValues.put("Mistake", mistake);
            long result = DB.insert("rtrData", null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } return false;
    }

    public Boolean updateData(String PV,String PV1, String Date, int physics, int chemistry, int botany, int zoology, String mistake){
        SQLiteDatabase DB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PV",PV1);
        contentValues.put("Date",Date);
        contentValues.put("Physics",physics);
        contentValues.put("Chemistry",chemistry);
        contentValues.put("Botany",botany);
        contentValues.put("Zoology",zoology);
        if(mistake!=""||mistake!=null){
            contentValues.put("Mistake",mistake);
        }
        Cursor cur=DB.rawQuery("select * from rtrData where PV=?",new String[]{PV});
        if (cur.getCount()>0){
            long result=DB.update("rtrData",contentValues,"PV=?",new String[]{PV});
            if(result==-1){
                return false;
            }
            else {
                return true;
            }
        }else {
            return false;
        }
    }

    public  Cursor getData(){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cur=DB.rawQuery("select * from rtrData",null);
        return cur;
    }

    public  Cursor getDatabyPV(String PV){
        SQLiteDatabase DB=this.getWritableDatabase();
        Cursor cur=DB.rawQuery("select * from rtrData where PV=?",new String[]{PV});
        return cur;
    }
}

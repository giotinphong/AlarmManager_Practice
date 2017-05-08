package com.practice.sonnguyen.alarmmanager_practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sonnguyen on 4/29/17.
 */

public class sqlData extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context context;
    private static final String TABLE_NAME = "ALARM",DATABASE = "DB";

    private static final String ID = "ID",TIME = "TIME",
            NOTE="NOTE",IS_ON="ISON",RINGTONE = "RINGTONE",
            VIBRATE = "VIBRATE",SOUNDLV = "SOUNDLV",LISTDAY = "LISTDAY";

    public sqlData(Context context) {
        super(context, DATABASE, null, 3);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        String sql = "create table if not exists "+TABLE_NAME+"("+ID+" integer   primary  key AUTOINCREMENT,"+TIME+"" +
                " long not null, "+NOTE+" nvarchar(50), "+IS_ON+" boolean not null,"+RINGTONE+" TEXT,"+VIBRATE+" BOOLEAN,"+
        SOUNDLV+" INTEGER,"+LISTDAY+" NVARCHAR(8))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+TABLE_NAME);
    }
    public int insert(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(TIME, alarm.getTime());
        values.put(NOTE, alarm.getNote());
        values.put(IS_ON, alarm.isOn());
        values.put(RINGTONE,alarm.getRingtone().toString());
        values.put(VIBRATE,alarm.isVibrate());
        values.put(SOUNDLV,alarm.getSoundLevel());
        values.put(LISTDAY,alarm.ListToString());
        db = this.getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
        return getidEnd();
    }
    public void update(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(TIME, alarm.getTime());
        values.put(NOTE, alarm.getNote());
        values.put(IS_ON, alarm.isOn());
        values.put(RINGTONE,alarm.getRingtone().toString());
        values.put(VIBRATE,alarm.isVibrate());
        values.put(SOUNDLV,alarm.getSoundLevel());
        values.put(LISTDAY,alarm.ListToString());
        db = this.getWritableDatabase();
        db.update(TABLE_NAME,values,ID+"=?",new String[]{String.valueOf(alarm.getId())});
        db.close();
    }
    public  void delete(Alarm alarm){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ID+"=?",new String[]{String.valueOf(alarm.getId())});
        db.close();
    }

    public Alarm getAlarm(int id){
        Alarm alarm = new Alarm();
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{ID,TIME,NOTE,IS_ON,RINGTONE,VIBRATE,SOUNDLV,LISTDAY},ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        alarm.setId(Integer.parseInt(cursor.getString(0)));
        alarm.setTime(Long.parseLong(cursor.getString(1)));
        alarm.setNote(cursor.getString(2));
        alarm.setOn((Integer.parseInt(cursor.getString(3))==1 ? true: false));
        alarm.setRingtone(Uri.parse(cursor.getString(4)));
        alarm.setVibrate((Integer.parseInt(cursor.getString(5))==1 ? true: false));
        alarm.setSoundLevel(Integer.parseInt(cursor.getString(6)));
        alarm.StringToList(cursor.getString(7));
        db.close();
        return alarm;
    }
     public ArrayList<Alarm> getAllAlarm(){
         ArrayList<Alarm> alarms = new ArrayList<>();
         String query = "select * from "+TABLE_NAME;
         db = this.getReadableDatabase();
         Cursor cursor = db.rawQuery(query,null);
         if(cursor.moveToFirst()){
             do{
                 Alarm alarm = new Alarm();
                 alarm.setId(Integer.parseInt(cursor.getString(0)));
                 alarm.setTime(Long.parseLong(cursor.getString(1)));
                 alarm.setNote(cursor.getString(2));
                 alarm.setOn((Integer.parseInt(cursor.getString(3))==1 ? true: false));
                 try {
                     alarm.setRingtone(Uri.parse(cursor.getString(4)));
                 }catch (Exception e){
                     Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                     alarm.setRingtone(null);
                 }
                 alarm.setVibrate((Integer.parseInt(cursor.getString(5))==1 ? true: false));
                 alarm.setSoundLevel(Integer.parseInt(cursor.getString(6)));
                 alarm.StringToList(cursor.getString(7));
                 alarms.add(alarm);

             }while (cursor.moveToNext());
         }
         db.close();
         return alarms;
     }
     public int getidEnd(){
            ArrayList<Alarm> alarmArrayList = getAllAlarm();
            return alarmArrayList.get(alarmArrayList.size()-1).getId();
     }
     public int getSize(){
         return getAllAlarm().size();
     }
}

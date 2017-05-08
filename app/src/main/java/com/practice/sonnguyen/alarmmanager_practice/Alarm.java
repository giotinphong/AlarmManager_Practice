package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by sonnguyen on 4/29/17.
 */

public class Alarm  {

    private long time;
    private int id,soundLevel;
    private String note;
    private boolean isOn,isVibrate;
    private Uri ringtone;
    private ArrayList<Integer> listDays;

    public int getSoundLevel() {
        return soundLevel;
    }

    public ArrayList<Integer> getListDays() {
        return listDays;
    }
    public ArrayList<Integer> StringToList(String s) {
        char[]c = s.toCharArray();
        for(char ci : c){
            listDays.add(Integer.parseInt(ci+""));
        }
        return listDays;
    }
    public String ListToString(){
        String s = "";
        for(int i : listDays)
            s+=i+"";
        return s;
    }
    public void setListDays(ArrayList<Integer> listDays) {
        this.listDays = listDays;
    }

    public void setSoundLevel(int soundLevel) {
        this.soundLevel = soundLevel;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Uri getRingtone() {
        return ringtone;
    }

    public void setRingtone(Uri ringtone) {
        this.ringtone = ringtone;
    }

    public Alarm() {
        this.isOn= true;
        ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        isVibrate=false;
        soundLevel = 18;
        listDays = new ArrayList<>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public Alarm(long time) {
        this.time = time;
        isOn = true;
    }
}

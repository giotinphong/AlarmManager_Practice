package com.practice.sonnguyen.alarmmanager_practice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Build;

import android.os.SystemClock;
import android.preference.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by sonnguyen on 4/29/17.
 */

public class CustomAlarmManager {
    private static final String sTagAlarms = ":alarms";

    public static void addAlarm(Context context, Intent intent, Alarm alarm) {

        long current = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent.putExtra("id",alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(alarm.getTime()<current) {
            alarm.setTime(alarm.getTime()+3600*24*1000);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTime(), pendingIntent);
        saveAlarmId(context, alarm.getId());

        //This means that the receiver will not be called unless the application explicitly enables it.
        // This prevents the boot receiver from being called unnecessarily. You can enable a receiver
        ComponentName receiver = new ComponentName(context, AlarmBroadcastService.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void cancelAlarm(Context context, Intent intent, int Alarm_id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Alarm_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        removeAlarmId(context, Alarm_id);
    }

    public static void cancelAllAlarms(Context context, Intent intent) {
        for (int idAlarm : getAlarmIds(context)) {
            cancelAlarm(context, intent, idAlarm);
        }
    }

    public static boolean hasAlarm(Context context, Intent intent, int Alarm_id) {
        return PendingIntent.getBroadcast(context, Alarm_id, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private static void saveAlarmId(Context context, int id) {
        List<Integer> idsAlarms = getAlarmIds(context);

        if (idsAlarms.contains(id)) {
            return;
        }

        idsAlarms.add(id);

        saveIdsInPreferences(context, idsAlarms);
    }

    private static void removeAlarmId(Context context, int id) {
        List<Integer> idsAlarms = getAlarmIds(context);

        for (int i = 0; i < idsAlarms.size(); i++) {
            if (idsAlarms.get(i) == id)
                idsAlarms.remove(i);
        }

        saveIdsInPreferences(context, idsAlarms);
    }

    private static List<Integer> getAlarmIds(Context context) {
        List<Integer> ids = new ArrayList<>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray2 = new JSONArray(prefs.getString(context.getPackageName() + sTagAlarms, "[]"));

            for (int i = 0; i < jsonArray2.length(); i++) {
                ids.add(jsonArray2.getInt(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

    private static void saveIdsInPreferences(Context context, List<Integer> lstIds) {
        JSONArray jsonArray = new JSONArray();
        for (Integer idAlarm : lstIds) {
            jsonArray.put(idAlarm);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + sTagAlarms, jsonArray.toString());

        editor.apply();
    }
    public void Repeat(Context context,Intent intent,long time,Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time* 1000, pendingIntent);
    }
}


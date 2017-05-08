package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.practice.sonnguyen.alarmmanager_practice.activity.AlarmNotificationDisplayActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sonnguyen on 4/29/17.
 */

public class AlarmBroadcastService extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        {

            int i = intent.getIntExtra("id", -1);
            sqlData db = new sqlData(context.getApplicationContext());
            Alarm alarm = db.getAlarm(i);
            ArrayList<Integer> listDay = alarm.getListDays();
            if(listDay.size()!=0) {
                TimeZone timeZone = TimeZone.getTimeZone("UTC");
                int today = Calendar.getInstance(timeZone).getTime().getDay();
                if (listDay.contains(today)) {
                    Intent in = new Intent(context.getApplicationContext(), AlarmNotificationDisplayActivity.class);
                    in.putExtra("id", i);
                    in.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivities(new Intent[]{in});
                }
            }
            else {
                Intent in = new Intent(context.getApplicationContext(), AlarmNotificationDisplayActivity.class);
                in.putExtra("id", i);
                in.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivities(new Intent[]{in});
            }
        }

    }

}

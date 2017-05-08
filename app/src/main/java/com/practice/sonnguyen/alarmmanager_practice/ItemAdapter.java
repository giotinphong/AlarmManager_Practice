package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sonnguyen on 4/29/17.
 */

public class ItemAdapter extends ArrayAdapter<Alarm> {
    private ArrayList<Alarm> alarmArrayList;
    private Context context;
    private sqlData db;
    private CustomAlarmManager customAlarmManager;
    public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Alarm> objects) {
        super(context, resource, objects);
        alarmArrayList = objects;
        db = new sqlData(context);
        this.context = context;
        db = new sqlData(getContext().getApplicationContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v==null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.item_alarm,null);
        }
        TextView textClock = (TextView) v.findViewById(R.id.item_textclock);

        Button btnDel = (Button)v.findViewById(R.id.item_btn_del);
        final Alarm alarm = alarmArrayList.get(position);
        Date date = new Date(alarm.getTime());
        textClock.setText(date.getHours()+":"+ (date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes()));

        customAlarmManager = new CustomAlarmManager();
        final Intent in = new Intent(context.getApplicationContext(),AlarmBroadcastService.class);
        final Button toggleButton = (Button)v.findViewById(R.id.item_btnCheck);
        toggleButton.setBackgroundResource(alarm.isOn()? R.drawable.switch_on : R.drawable.switch_off);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm.isOn()){
                    alarm.setOn(false);
                    customAlarmManager.cancelAlarm(getContext(),in,alarm.getId());
                }
                else {
                    alarm.setOn(true);
                    Toast.makeText(getContext(),alarm.getId()+":"+alarm.isOn(),Toast.LENGTH_SHORT).show();
                    customAlarmManager.addAlarm(getContext(),in,alarm);
                }
                db.update(alarm);
                notifyDataSetChanged();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm.isOn()){
                    customAlarmManager.cancelAlarm(getContext(),in,alarm.getId());
                }
                db.delete(alarm);
                alarmArrayList.remove(alarm);
                notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }
}

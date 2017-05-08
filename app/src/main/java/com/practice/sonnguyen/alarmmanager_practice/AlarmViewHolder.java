package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sonnguyen on 4/30/17.
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder {
     Alarm alarm;
     TextView textClock,txtnote,txtDay;
     Button btnCheck,btnDel;
    SwitchCompat  switch_on_off;

    public AlarmViewHolder(final View itemView) {
        super(itemView);
        textClock = (TextView)itemView.findViewById(R.id.item_textclock);
       // btnCheck = (Button)itemView.findViewById(R.id.item_btnCheck);
        btnDel = (Button)itemView.findViewById(R.id.item_btn_del);
        txtnote = (TextView)itemView.findViewById(R.id.item_txt_note);
        txtDay = (TextView)itemView.findViewById(R.id.item_txt_listDay);
        switch_on_off = (SwitchCompat)itemView.findViewById(R.id.item_switch_on_off);

    }
    public Alarm getAlarm(){
        return alarm;
    }
}

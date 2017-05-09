package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Intent;
import android.support.v7.widget.CardView;
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
    SwitchCompat  switch_on_off;
    CardView card_detail;

    public AlarmViewHolder(final View itemView) {
        super(itemView);
        textClock = (TextView)itemView.findViewById(R.id.item_textclock);
        txtnote = (TextView)itemView.findViewById(R.id.item_txt_note);
        txtDay = (TextView)itemView.findViewById(R.id.item_txt_listDay);
        switch_on_off = (SwitchCompat)itemView.findViewById(R.id.item_switch_on_off);
        card_detail = (CardView)itemView.findViewById(R.id.item_cardview_detail);

    }
    public Alarm getAlarm(){
        return alarm;
    }
}

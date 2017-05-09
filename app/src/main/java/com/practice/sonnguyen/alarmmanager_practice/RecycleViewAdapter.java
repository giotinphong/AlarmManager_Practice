package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.practice.sonnguyen.alarmmanager_practice.activity.AddNewAlarmActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sonnguyen on 4/30/17.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    ArrayList<Alarm> alarmArrayList;
    private sqlData db;
    private CustomAlarmManager customAlarmManager;
    private Intent in;
    Context itemView;
    public RecycleViewAdapter(ArrayList<Alarm> alarmArrayList) {
        this.alarmArrayList = alarmArrayList;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        itemView = parent.getContext();
        customAlarmManager = new CustomAlarmManager();
        in = new Intent(parent.getContext(),AlarmBroadcastService.class);
        db = new sqlData(parent.getContext().getApplicationContext());
        return  new AlarmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AlarmViewHolder holder, final int position) {
        final Alarm alarm = alarmArrayList.get(position);
        Date date = new Date(alarm.getTime());
        holder.textClock.setText(date.getHours()+":"+ (date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes()));
        String note = alarm.getNote();
        if(note!=null) holder.txtnote.setText(note);
        else holder.txtnote.setText("");
        setuptxtDay(alarm,holder);
        final int[] first = {0};
        holder.switch_on_off.setChecked(alarm.isOn());
        holder.switch_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setOn(isChecked);
                    db.update(alarm);
                if(first[0] ==1)
                    if(isChecked)
                        customAlarmManager.addAlarm(itemView,in,alarm);
                    else
                        customAlarmManager.cancelAlarm(itemView,in,alarm.getId());
                 first[0] =1;

            }
        });
        holder.card_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(holder.itemView.getContext(),AddNewAlarmActivity.class);
                in.putExtra("id",alarm.getId());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.startActivity(in);
            }
        });


        //set swipe to delete

    }

    @Override
    public int getItemCount() {
        return alarmArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public void setuptxtDay(Alarm alarm,AlarmViewHolder holder){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date date = new Date(alarm.getTime());
        ArrayList<Integer> arrayList = alarm.getListDays();
        if(arrayList.size()!=0){
            String s = "";
            for(int i : arrayList)
            {
                if(i==1) s+="CN ";
                else s+="T."+i+" ";
            }

            holder.txtDay.setText(s);
        }
        else{
            //neu arraylist size = 0 thi neu alarm bat -> tgian alarm < tgian hthong -> ngay mai / tgian alarm > tgian hthong->hom nay
            if(alarm.isOn()){
                int AlarmHour = date.getHours();
                int AlarmMinus = date.getMinutes();
                int CurrentHour = calendar.getTime().getHours();
                int CurrentMinus = calendar.getTime().getMinutes();
                if(AlarmHour>CurrentHour)
                    if(AlarmMinus>CurrentMinus)
                        //trong hom nay
                        holder.txtDay.setText("Hôm nay");
                    else
                        holder.txtDay.setText("Ngày mai");
                else
                    holder.txtDay.setText("Ngày mai");
            }
            else holder.txtDay.setText("");
        }
    }

}

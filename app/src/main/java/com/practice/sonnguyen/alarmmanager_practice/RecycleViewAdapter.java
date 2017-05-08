package com.practice.sonnguyen.alarmmanager_practice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        itemView = v.getContext();
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
        holder.btnCheck.setBackgroundResource(alarm.isOn()? R.drawable.switch_on : R.drawable.switch_off);
        holder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm.isOn()){
                    alarm.setOn(false);
                    customAlarmManager.cancelAlarm(itemView,in,alarm.getId());
                    holder.btnCheck.setBackgroundResource(R.drawable.switch_off);
                }
                else {
                    alarm.setOn(true);
                    Toast.makeText(itemView,alarm.getId()+":"+alarm.isOn(),Toast.LENGTH_SHORT).show();
                    customAlarmManager.addAlarm(itemView,in,alarm);
                    holder.btnCheck.setBackgroundResource(R.drawable.switch_on);
                }
                db.update(alarm);
                setuptxtDay(alarm,holder);


            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarm.isOn()){
                    customAlarmManager.cancelAlarm(itemView,in,alarm.getId());
                }
                db.delete(alarm);
                alarmArrayList.remove(alarm);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, alarmArrayList.size());

            }
        });
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

package com.practice.sonnguyen.alarmmanager_practice.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.practice.sonnguyen.alarmmanager_practice.Alarm;
import com.practice.sonnguyen.alarmmanager_practice.AlarmBroadcastService;
import com.practice.sonnguyen.alarmmanager_practice.CustomAlarmManager;
import com.practice.sonnguyen.alarmmanager_practice.R;
import com.practice.sonnguyen.alarmmanager_practice.sqlData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddNewAlarmActivity extends AppCompatActivity {
    private  int hourSet=0,minuteSet=0;
    private sqlData db;
    private CustomAlarmManager customAlarmManager;
    private TextView textClock_SetTime;
    private Alarm alarm;
    private SeekBar SeekSoundLevel;
    private TextView txtDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customAlarmManager = new CustomAlarmManager();
        db = new sqlData(getApplicationContext());
        alarm = new Alarm();
        final EditText edNote = (EditText)findViewById(R.id.acti_addnewalarm_edittext_note);
        Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();

        textClock_SetTime = (TextView) findViewById(R.id.acti_addnewalarm_textclock_settime);
        textClock_SetTime.setText(date.getHours()+":"+date.getMinutes());
        textClock_SetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddNewAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //set time to this
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                           hourSet = timePicker.getHour();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            minuteSet = timePicker.getMinute();
                        }
                        else{
                            hourSet = timePicker.getCurrentHour();
                            minuteSet = timePicker.getCurrentMinute();

                        }
                        textClock_SetTime.setText(hourSet+":"+minuteSet);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        txtDay = (TextView)findViewById(R.id.acti_addnewalarm_txt_day);
        SeekSoundLevel = (SeekBar) findViewById(R.id.acti_addnewalarm_seekbar_soundLevel);
        SeekSoundLevel.setMax(20);
        SeekSoundLevel.setProgress(16);
        SeekSoundLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alarm.setSoundLevel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CardView Repeat = (CardView)findViewById(R.id.acti_addnewalarm_cardview_repeat);
        Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: xem lai loi crash
                final Dialog dialog = new Dialog(AddNewAlarmActivity.this);
                dialog.setContentView(R.layout.layout_repeat_day);
                final CheckBox t2,t3,t4,t5,t6,t7,cn;
                final ArrayList<Integer> listDay = new ArrayList<Integer>();
                Button btnOK,btnCancel;
                t2 = (CheckBox)dialog.findViewById(R.id.repeat_2);
                t3 = (CheckBox)dialog.findViewById(R.id.repeat_3);
                t4 = (CheckBox)dialog.findViewById(R.id.repeat_4);
                t5 = (CheckBox)dialog.findViewById(R.id.repeat_5);
                t6 = (CheckBox)dialog.findViewById(R.id.repeat_6);
                t7 = (CheckBox)dialog.findViewById(R.id.repeat_7);
                cn = (CheckBox)dialog.findViewById(R.id.repeat_1);
                CheckBox allDay = (CheckBox)dialog.findViewById(R.id.repeat_all);
                allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            t2.setChecked(true);
                            t3.setChecked(true);
                            t4.setChecked(true);
                            t5.setChecked(true);
                            t6.setChecked(true);
                            t7.setChecked(true);
                            cn.setChecked(true);
                        }
                        else{
                            t2.setChecked(false);
                            t3.setChecked(false);
                            t4.setChecked(false);
                            t5.setChecked(false);
                            t6.setChecked(false);
                            t7.setChecked(false);
                            cn.setChecked(false);
                        }
                    }
                });
                if(!t2.isChecked() || !t3.isChecked() || !t4.isChecked() || !t5.isChecked() || !t6.isChecked()
                        || !t7.isChecked() || !cn.isChecked())
                    allDay.setChecked(false);
                btnOK = (Button)dialog.findViewById(R.id.repeat_btn_ok);
                btnCancel = (Button)dialog.findViewById(R.id.repeat_btn_cancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = "";
                        if(t2.isChecked()) {
                            listDay.add(2);
                            s+="T2  ";
                        }
                        if(t3.isChecked()) {
                            s+="T3  ";
                            listDay.add(3);
                        }
                        if(t4.isChecked()) {
                            s+="T4  ";
                            listDay.add(4);
                        }
                        if(t5.isChecked()){
                            s+="T5  ";
                            listDay.add(5);
                        }
                        if(t6.isChecked()){
                            s+="T6  ";
                            listDay.add(6);
                        }
                        if(t7.isChecked()){
                            s+="T7  ";
                            listDay.add(7);
                        }
                        if(cn.isChecked()) {
                            s+="CN  ";
                            listDay.add(1);
                        }
                        alarm.setListDays(listDay);
                        txtDay.setText(s==""? "Không lặp lại":s );
                        dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //setup vibrate
        CardView setVibrate = (CardView) findViewById(R.id.acti_addnewalarm_cardview_vibrate);
        setVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                alarm.setVibrate(true);
                                db.update(alarm);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                alarm.setVibrate(false);
                                db.update(alarm);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewAlarmActivity.this);
                builder.setMessage("Bạn muốn sử dụng chế độ rung ?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.acti_addnewalarm_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hourSet==0&&minuteSet==0){
                    Snackbar.make(findViewById(android.R.id.content),"Bạn chưa chọn giờ",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Intent in = new Intent(getApplicationContext(),AlarmBroadcastService.class);

                Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                //neu gio hien tai > gio set
                if(Calendar.getInstance().getTime().getHours()>hourSet){
                    date.setDate(date.getDay()+1);
                }
                date.setHours(hourSet);
                date.setMinutes(minuteSet);
                date.setSeconds(0);
                alarm.setTime(date.getTime());
                alarm.setNote(edNote.getText().toString());
                alarm.setId(db.insert(alarm));
                //truyen id of alarm vao intent
                in.putExtra("id",alarm.getId());
                customAlarmManager.addAlarm(getApplicationContext(),in,alarm);
                //neu nhu phut cua hien tai > phut set, gio hien tai < gio set
                int Chour = Calendar.getInstance().getTime().getHours(),Cminute = Calendar.getInstance().getTime().getMinutes();
                if(Cminute>minuteSet && Chour == hourSet - 1){
                    Toast.makeText(getApplicationContext(),"Chuông báo được khởi động sau "+(minuteSet+60 - Calendar.getInstance().getTime().getMinutes())
                            +" phút nữa.",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Chuông báo được khởi động sau "+(date.getHours()- Chour)
                            +" giờ " +(date.getMinutes()-Cminute)+" phút nữa.",Toast.LENGTH_SHORT).show();
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",1);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        CardView ringtone = (CardView) findViewById(R.id.acti_addnewalarm_cardview_ringtone);
        ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, 5);
            }
        });


    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                this.alarm.setRingtone(uri);
                db.update(alarm);
                TextView alarmName = (TextView)findViewById(R.id.acti_addnewalarm_txt_name_alarm);
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                String title = ringtone.getTitle(this);
                alarmName.setText(title);
            }

        }
    }
}

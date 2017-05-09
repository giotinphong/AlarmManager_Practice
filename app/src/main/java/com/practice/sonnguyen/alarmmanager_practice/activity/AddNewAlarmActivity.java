package com.practice.sonnguyen.alarmmanager_practice.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
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
    private  int hourSet=0,minuteSet=0,id=-1;
    private sqlData db;
    private CustomAlarmManager customAlarmManager;
    private Alarm alarm;
    private SeekBar SeekSoundLevel;
    private TextView txtRepeat,txtVibrate;
    private TimePicker timePicker;
    private ArrayList<Integer>listDay;
    private TextView txtRingtone;
    private EditText edRemind;
    private FloatingActionButton fabEdit;

    private CheckBox t2,t3,t4,t5,t6,t7,cn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        db = new sqlData(this);
        customAlarmManager = new CustomAlarmManager();
        txtRepeat = (TextView)findViewById(R.id.acti_addnewalarm_txt_repeat);
        txtVibrate = (TextView)findViewById(R.id.acti_addnewalarm_txt_vibrate_display);
        SeekSoundLevel = (SeekBar) findViewById(R.id.acti_addnewalarm_seekbar_soundLevel);
        txtRingtone = (TextView)findViewById(R.id.acti_addnewalarm_txt_ringtone);
        edRemind = (EditText)findViewById(R.id.acti_addnewalarm_edittext_remind);
        fabEdit = (FloatingActionButton)findViewById(R.id.acti_addnewalarm_fab_add);
        timePicker = (TimePicker)findViewById(R.id.acti_addnewalarm_picker_time);
        SeekSoundLevel = (SeekBar) findViewById(R.id.acti_addnewalarm_seekbar_soundLevel);
        SeekSoundLevel.setMax(20);

        //is edit = true -> edit : false => show detail
        try {
            id = getIntent().getExtras().getInt("id");
        }catch (Exception e){}
        if(id!=-1){

            customAlarmManager = new CustomAlarmManager();
            alarm = db.getAlarm(id);
            Date date = new Date(alarm.getTime());
            int hour = date.getHours(),minute = date.getMinutes();
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
            //set
            listDay = alarm.getListDays();
            if(listDay.size()==0){
                txtRepeat.setText("Không lặp lại");
            }
            else {
                String s = "";
                for(int i : listDay){
                    if(i!=1)
                    s+="T."+i+" ";
                    else
                        s+="CN ";
                }
                txtRepeat.setText(s);
            }
            txtVibrate.setText(alarm.isVibrate() ? "Rung" : "Không rung");
            SeekSoundLevel.setProgress(alarm.getSoundLevel());
            Ringtone ringtone = RingtoneManager.getRingtone(this, alarm.getRingtone());
            txtRingtone.setText(ringtone.getTitle(this));
            edRemind.setText(alarm.getNote());
            //fabEdit.setBackgroundResource(R.drawable.ic_edit);
            fabEdit.setImageResource(R.drawable.ic_edit);
        }

        else {
            listDay = new ArrayList<>();
            alarm = new Alarm();
            SeekSoundLevel.setProgress(16);
        }



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
                //co id
                if(id !=-1){
                    listDay = alarm.getListDays();
                    if(listDay.contains(1)) cn.setChecked(true);
                    if(listDay.contains(2)) t2.setChecked(true);
                    if(listDay.contains(3)) t3.setChecked(true);
                    if(listDay.contains(4)) t4.setChecked(true);
                    if(listDay.contains(5)) t5.setChecked(true);
                    if(listDay.contains(6)) t6.setChecked(true);
                    if(listDay.contains(7)) t7.setChecked(true);
                }
                listDay = new ArrayList<Integer>();
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
                        txtRepeat.setText(s==""? "Không lặp lại":s );
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
                                txtVibrate.setText("Rung");
                                db.update(alarm);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                alarm.setVibrate(false);
                                txtVibrate.setText("Không rung");
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
                if(hourSet==0&&minuteSet==0){
                    Snackbar.make(findViewById(android.R.id.content),"Bạn chưa chọn giờ",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Intent in = new Intent(getApplicationContext(),AlarmBroadcastService.class);

                Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                //neu gio hien tai > gio set
                int add1day =0 ;
                Date date1 = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                if(hourSet<date1.getHours()||(hourSet==date1.getHours()&&minuteSet<=date1.getHours())){
                    date.setTime(date.getTime()+24*60*60*1000);
                    Toast.makeText(getApplicationContext(),"Chuông báo được khởi động vào lúc "+(hourSet)
                            +" giờ " +(minuteSet)+" phút ngày mai.",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Chuông báo được khởi động sau "+(hourSet-date1.getHours())+" giờ " + (minuteSet-date1.getMinutes())+
                            " phút nữa.",Toast.LENGTH_SHORT).show();


                date.setHours(hourSet);
                date.setMinutes(minuteSet);
                date.setSeconds(0);
                alarm.setTime(date.getTime());
                alarm.setNote(edRemind.getText().toString());
                //if have id->update alarm, else add new alarm
                if(id!=-1)
                    db.update(alarm);
                else
                    alarm.setId(db.insert(alarm));
                //truyen id of alarm vao intent
                //in.putExtra("id",alarm.getId());
                customAlarmManager.addAlarm(getApplicationContext(),in,alarm);
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
                TextView alarmName = (TextView)findViewById(R.id.acti_addnewalarm_txt_ringtone);
                Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
                String title = ringtone.getTitle(this);
                alarmName.setText(title);
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}

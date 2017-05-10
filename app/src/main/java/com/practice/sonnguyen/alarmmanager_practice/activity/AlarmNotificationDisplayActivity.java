package com.practice.sonnguyen.alarmmanager_practice.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.practice.sonnguyen.alarmmanager_practice.Alarm;
import com.practice.sonnguyen.alarmmanager_practice.AlarmBroadcastService;
import com.practice.sonnguyen.alarmmanager_practice.CustomAlarmManager;
import com.practice.sonnguyen.alarmmanager_practice.R;
import com.practice.sonnguyen.alarmmanager_practice.sqlData;

import java.io.IOException;

public class AlarmNotificationDisplayActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private static final long TIME_NOTI = 5*60*1000;
    private Alarm alarm;
    private  CustomAlarmManager alarmManager;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_notification_display);
        final int id = getIntent().getExtras().getInt("id");
        final sqlData db = new sqlData(getApplicationContext());
        alarm = db.getAlarm(id);
        in = new Intent(getApplicationContext(),AlarmBroadcastService.class);
        alarmManager = new CustomAlarmManager();
        Window wind;
        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        final Vibrator vibrator = (Vibrator) this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        mp = new MediaPlayer();
        if(alarm.isVibrate()){
            // Vibrate for 5 min
            vibrator.vibrate(TIME_NOTI);
        }

        mp.setVolume(alarm.getSoundLevel()*1f/200f,alarm.getSoundLevel()*1f/200f);
    try {
            mp.setDataSource(getApplicationContext(),alarm.getRingtone());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        mp.setLooping(true);
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

        Button btnOff = (Button)findViewById(R.id.acti_alarmdisplay_btn_off);
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                alarmManager.cancelAlarm(getApplicationContext(),in,id);
                alarm.setOn(false);
                db.update(alarm);
                Toast.makeText(getApplicationContext(),"Chúc bạn một ngày làm việc hiệu quả",Toast.LENGTH_SHORT).show();
                vibrator.cancel();
                finish();
            }
        });
        Button btnSleep = (Button) findViewById(R.id.acti_alarmdisplay_btn_sleep);
        btnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                 in = new Intent(getApplicationContext(),AlarmBroadcastService.class);
                in.putExtra("id",alarm.getId());
                alarmManager.Repeat(getApplicationContext(),in,2*60*1000,alarm);
                Toast.makeText(getApplicationContext(),"Chuông báo được nhắc lại trong 2 phút nữa",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp.stop();

        in.putExtra("id",alarm.getId());
        alarmManager.Repeat(getApplicationContext(),in,2*60*1000,alarm);
        Toast.makeText(getApplicationContext(),"Chuông báo được nhắc lại trong 2 phút nữa",Toast.LENGTH_SHORT).show();
        finish();
    }

}

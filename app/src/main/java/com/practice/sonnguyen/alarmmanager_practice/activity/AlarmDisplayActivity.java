package com.practice.sonnguyen.alarmmanager_practice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.practice.sonnguyen.alarmmanager_practice.Alarm;
import com.practice.sonnguyen.alarmmanager_practice.CustomAlarmManager;
import com.practice.sonnguyen.alarmmanager_practice.ItemAdapter;
import com.practice.sonnguyen.alarmmanager_practice.R;
import com.practice.sonnguyen.alarmmanager_practice.RecycleViewAdapter;
import com.practice.sonnguyen.alarmmanager_practice.sqlData;

import java.util.ArrayList;

public class AlarmDisplayActivity extends AppCompatActivity {
    private ItemAdapter itemAdapter;
    private ArrayList<Alarm> alarmArrayList;
    private sqlData db;
    private RecycleViewAdapter recycleViewAdapter;
    private RecyclerView rv;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alarmArrayList = new ArrayList<>();
        rv = (RecyclerView)findViewById(R.id.acti_alarmdisplayMain_rec_view_list);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        db = new sqlData(getApplicationContext());
        img = (ImageView) findViewById(R.id.acti_alarmdisplayMain_img_add);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),AddNewAlarmActivity.class),1);
            }
        });
        updateRecycleView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.acti_alarmdisplayMain_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivityForResult(new Intent(getApplicationContext(),AddNewAlarmActivity.class),1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            updateRecycleView();
        }
    }

    private void updateRecycleView() {
        alarmArrayList = db.getAllAlarm();
        recycleViewAdapter = new RecycleViewAdapter(alarmArrayList);
        rv.setAdapter(recycleViewAdapter);
        img.setVisibility((alarmArrayList.size()==0)? View.VISIBLE : View.INVISIBLE);
        recycleViewAdapter.notifyDataSetChanged();
    }

}

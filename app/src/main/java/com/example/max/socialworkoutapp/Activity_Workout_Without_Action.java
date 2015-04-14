package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Workout_Without_Action extends ActionBarActivity {

    private TextView tv_WorkoutName;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> strArr_Tasks;
    private ListView listView_Tasks;


    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_without_action_page);

        registerViews();

        //workoutList = new ArrayList<WorkoutItem>();

        strArr_Tasks = new ArrayList<String>();
        for (int i = 0 ; i < 15 ; i++){
            strArr_Tasks.add(i,"TASK : " + i);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr_Tasks);
        listView_Tasks.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Workout");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        //new GetWorkout().execute("");// Http or JSON path

        registerForContextMenu(listView_Tasks);
    }

    public void registerViews() {
        tv_WorkoutName = (TextView)findViewById(R.id.textView_WWA_WorkoutName);
        listView_Tasks = (ListView) findViewById(R.id.list_WWA_Tasks);
    }

}

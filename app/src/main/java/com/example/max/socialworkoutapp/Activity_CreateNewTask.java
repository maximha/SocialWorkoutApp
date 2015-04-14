package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_CreateNewTask extends ActionBarActivity implements View.OnClickListener{

    EditText et_TaskName , et_Description , et_Time , et_Rev;

    private Button btnAct_Task_Save;
    private static final String TAG = "State";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_page);

        registerViews();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Task");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    public void registerViews() {
        et_TaskName = (EditText) findViewById(R.id.editText_TaskName);
        et_Description= (EditText) findViewById(R.id.editText_Description);
        et_Time = (EditText) findViewById(R.id.editText_TaskTime);
        et_Rev= (EditText) findViewById(R.id.editText_TaskRev);

        btnAct_Task_Save = (Button) findViewById(R.id.btn_Save_Task);
        btnAct_Task_Save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_Task:
                Log.d(TAG, "Save_Task Pressed");
                // Save Button in Create New Task page
                //Intent intentMyWorkouts = new Intent(this, MyWorkouts.class);
                //startActivity(intentMyWorkouts);
                break;
            default:
                break;
        }
    }
}
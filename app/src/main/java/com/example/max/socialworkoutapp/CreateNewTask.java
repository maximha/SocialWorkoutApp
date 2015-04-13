package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateNewTask extends ActionBarActivity implements View.OnClickListener{
    private Button btnAct_Task_Save;
    private static final String TAG = "State";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_page);

        btnAct_Task_Save = (Button) findViewById(R.id.btn_Save_Task);
        btnAct_Task_Save.setOnClickListener(this);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Task");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
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
package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateNewWorkout extends ActionBarActivity implements View.OnClickListener{
    private Button btnAct_CNW_Save;
    private static final String TAG = "State";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_workout_page);

        btnAct_CNW_Save = (Button) findViewById(R.id.btn_Save_CreateNewWorkout);
        btnAct_CNW_Save.setOnClickListener(this);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Workout");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_CreateNewWorkout:
                Log.d(TAG, "Save_CreateNewWorkout Button Pressed");
                // Save Button in Create New Workout page
                //Intent intentMyWorkouts = new Intent(this, MyWorkouts.class);
                //startActivity(intentMyWorkouts);
                break;
            default:
                break;
        }
    }
}

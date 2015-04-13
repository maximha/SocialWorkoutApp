package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeMenu extends ActionBarActivity implements View.OnClickListener {

    //private Button btnActChangeProf;
    private Button btnActCreateNewWorkout;
    private Button btnActMyWorkouts;
    private Button btnActStorageWorkouts;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu_page);

        //btnActChangeProf = (Button) findViewById(R.id.btn_ChangeProf);
        //btnActChangeProf.setOnClickListener(this);

        btnActCreateNewWorkout = (Button) findViewById(R.id.btn_NewWorkout);
        btnActCreateNewWorkout.setOnClickListener(this);

        btnActMyWorkouts = (Button) findViewById(R.id.btn_MyWorkouts);
        btnActMyWorkouts.setOnClickListener(this);

        btnActStorageWorkouts = (Button) findViewById(R.id.btn_Storage);
        btnActStorageWorkouts.setOnClickListener(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("Log Out");
        menu.add("Change Profile");

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        if(item.getTitle() == "Log Out"){
            Intent intentLogOut = new Intent(this, Login.class);
            startActivity(intentLogOut);
        }
        if (item.getTitle() == "Change Profile"){
            Intent intentChangeProf = new Intent(this, ChangeProfile.class);
            startActivity(intentChangeProf);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_MyWorkouts:
                Log.d(TAG, "MyWorkouts Button Pressed");
                // MyWorkouts Button in Home menu page
                Intent intentMyWorkouts = new Intent(this, MyWorkouts.class);
                startActivity(intentMyWorkouts);
                break;
            case R.id.btn_Storage:
                Log.d(TAG, "Storage Button Pressed");
                // Storage Button in Home menu page
                Intent intentStorageWorkouts = new Intent(this, StorageWorkouts.class);
                startActivity(intentStorageWorkouts);
                break;
            case R.id.btn_NewWorkout:
                Log.d(TAG, "NewWorkout Button Pressed");
                // Create New Workout Button in Home menu page
                Intent intentNewWorkout = new Intent(this, CreateNewWorkout.class);
                startActivity(intentNewWorkout);
                break;
            default:
                break;
        }
    }

}

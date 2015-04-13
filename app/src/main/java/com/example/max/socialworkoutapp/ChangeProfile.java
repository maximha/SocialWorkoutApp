package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ChangeProfile extends ActionBarActivity implements View.OnClickListener {

    private Button btnActSave;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile);

        btnActSave = (Button) findViewById(R.id.btn_Save_ChangeProf);
        btnActSave.setOnClickListener(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Profile");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_ChangeProf:
                Log.d(TAG, "Save Button Pressed");
                // Save Button in Change profile page
                Intent intentSaveChangeProf = new Intent(this, HomeMenu.class);
                startActivity(intentSaveChangeProf);
                break;
            default:
                break;
        }
    }
}

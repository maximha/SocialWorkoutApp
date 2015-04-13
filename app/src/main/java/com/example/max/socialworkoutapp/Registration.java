package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by Max on 14/03/2015.
 */
public class Registration extends ActionBarActivity implements View.OnClickListener {

    private Button btnActRegistration;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        btnActRegistration = (Button) findViewById(R.id.btn_Registration);
        btnActRegistration.setOnClickListener(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registration");

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
            case R.id.btn_Registration:
                Log.d(TAG, "Registration Button Pressed");
                // Registration Button
                Intent intentRegistration = new Intent(this, HomeMenu.class);
                startActivity(intentRegistration);
                break;
            default:
                break;
        }
    }
}

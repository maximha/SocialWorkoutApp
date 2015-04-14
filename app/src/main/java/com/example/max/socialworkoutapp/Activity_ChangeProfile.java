package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity_ChangeProfile extends ActionBarActivity implements View.OnClickListener {

    EditText et_FirstName , et_LastName , et_UserName , et_Pass , et_ConfirmPass;

    private Button btnActSave;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile);

        registerViews();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Profile");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    public void registerViews() {
        et_FirstName = (EditText) findViewById(R.id.editText_FN_ChangeProf);
        et_LastName = (EditText) findViewById(R.id.editText_LN_ChangeProf);
        et_UserName = (EditText) findViewById(R.id.editText_User_ChangeProf);
        et_Pass = (EditText) findViewById(R.id.editText_Pass_ChangeProf);
        et_ConfirmPass = (EditText) findViewById(R.id.editText_ConfPass_ChangeProf);

        btnActSave = (Button) findViewById(R.id.btn_Save_ChangeProf);
        btnActSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_ChangeProf:
                Log.d(TAG, "Save Button Pressed");
                // Save Button in Change profile page
                Intent intentSaveChangeProf = new Intent(this, Activity_HomeMenu.class);
                startActivity(intentSaveChangeProf);
                break;
            default:
                break;
        }
    }
}

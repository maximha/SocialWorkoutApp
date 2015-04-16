package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Activity_Login extends ActionBarActivity implements View.OnClickListener {

    EditText et_User , et_Password;

    private Button btnActLogin;
    private Button btnActHomeRegistration;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        registerViews();

        // Action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

    }

    public void registerViews() {

        et_User = (EditText) findViewById(R.id.editText_User);
        et_Password = (EditText) findViewById(R.id.editText_Password);

        btnActLogin = (Button) findViewById(R.id.btn_LogIn);
        btnActLogin.setOnClickListener(this);

        btnActHomeRegistration = (Button) findViewById(R.id.btn_homeRegistration);
        btnActHomeRegistration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "on the ID button to determine which causes the handler");
        switch (v.getId()){
            case R.id.btn_LogIn :
                if(checkValidation ())//check if data entered is correct
                {
                    Log.d(TAG, "Login Button Pressed");
                    // Login Button
                    Intent intentLogIn = new Intent(this, Activity_HomeMenu.class);
                    startActivity(intentLogIn);
                }
                break;
            case R.id.btn_homeRegistration :
                Log.d(TAG , "Registration Button Pressed");
                // Registration Button
                Intent intentRegistration = new Intent(this , Activity_Registration.class);
                startActivity(intentRegistration);
                break;
            default:
                break;
        }

    }

    //check validation
    private boolean checkValidation() {
        boolean ret = true;

        if (!EditText_Validators.isName(et_User, true)) ret = false;
        if (!EditText_Validators.isPassword(et_Password, true)) ret = false;
        return ret;
    }
}

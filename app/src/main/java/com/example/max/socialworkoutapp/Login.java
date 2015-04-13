package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends ActionBarActivity implements View.OnClickListener {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        /*switch (item.getItemId()){
            case R.id.action_Back :
                Toast.makeText(this ,"Back" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_Plus :
                Toast.makeText(this ,"Plus" , Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "on the ID button to determine which causes the handler");
        switch (v.getId()){
            case R.id.btn_LogIn :
                Log.d(TAG , "Login Button Pressed");
                // Login Button
                Intent intentLogIn = new Intent(this , HomeMenu.class);
                startActivity(intentLogIn);
                break;
            case R.id.btn_homeRegistration :
                Log.d(TAG , "Registration Button Pressed");
                // Registration Button
                Intent intentRegistration = new Intent(this , Registration.class);
                startActivity(intentRegistration);
                break;
            default:
                break;
        }

    }
}

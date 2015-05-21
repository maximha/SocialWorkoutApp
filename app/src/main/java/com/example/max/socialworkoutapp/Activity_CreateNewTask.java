package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class Activity_CreateNewTask extends ActionBarActivity implements View.OnClickListener{

    EditText et_TaskName , et_Description , et_Time , et_Rev;

    final Context context = this;
    private PostHelper SHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_task_page);

        registerViews();

        defineAdapter();


    }

    public void registerViews() {
        et_TaskName = (EditText) findViewById(R.id.editText_TaskName);
        et_Description = (EditText) findViewById(R.id.editText_Description);
        et_Time = (EditText) findViewById(R.id.editText_TaskTime);
        et_Rev = (EditText) findViewById(R.id.editText_TaskRev);

        Button btnAct_Task_Save = (Button) findViewById(R.id.btn_Save_Task);
        btnAct_Task_Save.setOnClickListener(this);
    }

    private void defineAdapter(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Task");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_Task:
                if(checkValidation()) {
                    SHelper = new PostHelper(context);
                    SHelper.execute("http://localhost:36301/api/AddTask","AddTask", sharedGet(), et_TaskName.getText().toString(),
                                            et_Description.getText().toString(),et_Time.getText().toString(), et_Rev.getText().toString());
                    try {
                        checkPostResult(showResult());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Form contains error", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                break;
        }
    }


    // check if user allow to register
    public void checkPostResult(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            // Save Button in Create New Task page
            Intent intentMyWorkouts = new Intent(this, Activity_Workout.class);
            startActivity(intentMyWorkouts);
            finish();
        } else {
            Toast.makeText(this, "This task already exist !!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    // get response from http request
    private String showResult() {
        if (SHelper == null)
            return null;
        try {
            return SHelper.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //check validation
    private boolean checkValidation() {
        boolean ret = true;

        if (!EditText_Validators.hasText(et_TaskName)) ret = false;
        if (!EditText_Validators.hasText(et_Description)) ret = false;
        if (!EditText_Validators.hasText(et_Time)) ret = false;
        if (!EditText_Validators.hasText(et_Rev)) ret = false;
        return ret;
    }

    private String sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return  editor.getString("workoutName", null);
    }
}
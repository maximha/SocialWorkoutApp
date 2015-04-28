package com.example.max.socialworkoutapp;


import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Activity_Task extends ActionBarActivity {

    private LinearLayout linearLayout_Task,linearLayout_Task_1 ,linearLayout_Task_2;
    private LinearLayout linearLayout_Task_1_1 ,linearLayout_Task_2_1 , linearLayout_Task_2_2;

    private TextView tv_taskname ,tv_time , tv_rev;
    private TextView row_taskName , row_description , row_taskTime ,row_taskRew;

    private PostHelper SHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_page);


        registerViews();
        ShowTask();
        defineArrayAdapter();
    }

    private  void  ShowTask(){
        SHelper = new PostHelper();
        SHelper.execute("http://localhost:36301/api/TaskByName","Task",sharedGet());
        try {
            checkPostResultTask(showResult());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerViews() {
        linearLayout_Task = (LinearLayout) findViewById(R.id.LinearLayout_Task);
        linearLayout_Task_1 = (LinearLayout) findViewById(R.id.LinearLayout_Task_1);
        linearLayout_Task_1_1 = (LinearLayout) findViewById(R.id.LinearLayout_Task_1_1);
        linearLayout_Task_2 = (LinearLayout) findViewById(R.id.LinearLayout_Task_2);
        linearLayout_Task_2_1 = (LinearLayout) findViewById(R.id.LinearLayout_Task_2_1);
        linearLayout_Task_2_2 = (LinearLayout) findViewById(R.id.LinearLayout_Task_2_2);
        tv_taskname = (TextView) findViewById(R.id.tv_row_taskName);
        row_taskName = (TextView) findViewById(R.id.row_taskName);
        row_description = (TextView) findViewById(R.id.row_description);
        tv_time = (TextView) findViewById(R.id.tv_row_time);
        row_taskTime = (TextView) findViewById(R.id.row_taskTime);
        tv_rev = (TextView) findViewById(R.id.tv_row_rev);
        row_taskRew = (TextView) findViewById(R.id.row_taskRew);
    }

    private void defineArrayAdapter(){

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Task");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_task, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.btn_edit_task){
            Intent intentPlus_W = new Intent(this, Activity_CreateNewTask.class);
            startActivity(intentPlus_W);
        }

        return super.onOptionsItemSelected(item);
    }

    private String sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return  editor.getString("taskName", null);
    }

    // get response from http request
    private String showResult() {
        if (SHelper == null)
            return null;
        try {
            return SHelper.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkPostResultTask(String result) throws JSONException{
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            String[] jsonArr = null;
            jsonArr = getJsonArray(json);
            /*strArr_Tasks = new ArrayList<String>();
            for (int i = 0 ; i < jsonArr.length ; i++){

                strArr_Tasks.add(jsonArr[i]);
            }*/
            row_taskName.setText(jsonArr[0]);
            row_description.setText(jsonArr[1]);
            row_taskTime.setText(jsonArr[2]);
            row_taskRew.setText(jsonArr[3]);

        } else {
            Toast.makeText(this, "This task already exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    private String[] getJsonArray(JSONObject json){
        String[] modelTaskElements = null;
        try {
            JSONArray tasks = json.getJSONArray("itemTask");
            modelTaskElements = new String[tasks.length()];
            for (int i = 0; i< tasks.length() ; i++ ){
                modelTaskElements[i] = tasks.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelTaskElements;
    }
}

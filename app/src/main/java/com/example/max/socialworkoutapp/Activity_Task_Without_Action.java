package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;


public class Activity_Task_Without_Action extends ActionBarActivity {

    private TextView row_taskName , row_description , row_taskTime ,row_taskRew;
    final Context context = this;
    private PostHelper SHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_page);

        registerViews();
        try {
            ShowTask();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        defineAdapter();
    }

    private  void  ShowTask() throws GeneralSecurityException, IOException {
        String[] shared = sharedGetParametersForStorageWorkout();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/StorageTaskProperty", "StorageTaskProperty", shared[0] , shared[1]);
        try {
            checkPostResultTask(showResult());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerViews() {

        row_taskName = (TextView) findViewById(R.id.row_taskName);
        row_description = (TextView) findViewById(R.id.row_description);
        row_taskTime = (TextView) findViewById(R.id.row_taskTime);
        row_taskRew = (TextView) findViewById(R.id.row_taskRew);
    }

    private void defineAdapter(){

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Task");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
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

    private void checkPostResultTask(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            Model_TaskItem model ;
            model = getJsonArray(json);
            model = aesDecrypt(model);
            row_taskName.setText(model.getTaskName());
            row_description.setText(model.getDescriptionTask());
            row_taskTime.setText(model.getTimeTask());
            row_taskRew.setText(model.getRevTask());

        } else {
            Toast.makeText(this, "This task already exist !!!",
                    Toast.LENGTH_LONG).show();
        }
    }
    private Model_TaskItem getJsonArray(JSONObject json){
        Model_TaskItem model = new Model_TaskItem();
        try {
            JSONObject c = json.getJSONObject("itemTask");
            String tName = c.getString("taskName");
            String wName = c.getString("workoutName");
            String desc = c.getString("description");
            String time = c.getString("time");
            String rev = c.getString("rev");

            model.setTaskName(tName);
            model.setWorkoutName(wName);
            model.setDescriptionTask(desc);
            model.setTimeTask(time);
            model.setRevTask(rev);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    private Model_TaskItem aesDecrypt(Model_TaskItem encryptedModel) throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        encryptedModel.setDescriptionTask(AES.decrypt(encryptedModel.getDescriptionTask(),shared[1]));
        encryptedModel.setRevTask(AES.decrypt(encryptedModel.getRevTask(), shared[1]));
        encryptedModel.setTaskName(AES.decrypt(encryptedModel.getTaskName(), shared[1]));
        encryptedModel.setTimeTask(AES.decrypt(encryptedModel.getTimeTask(), shared[1]));
        encryptedModel.setWorkoutName(AES.decrypt(encryptedModel.getWorkoutName(), shared[1]));
        return  encryptedModel;
    }

    private String[] sharedGetParametersForStorageWorkout()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return new String[]{editor.getString("storageWorkoutName", null) ,editor.getString("storageTaskName", null)};
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return new String[]{editor.getString("taskName", null) ,editor.getString("aesKey", null)};
    }
}

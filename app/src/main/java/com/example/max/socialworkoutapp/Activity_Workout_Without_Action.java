package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class Activity_Workout_Without_Action extends ActionBarActivity {

    private ArrayList<String> strArr_Tasks;
    private ListView listView_Tasks;
    private PostHelper SHelper;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_without_action_page);

        registerViews();

        try {
            ShowTasksList();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        defineArrayAdapter();
    }

    private  void  ShowTasksList() throws GeneralSecurityException, IOException {
        String[] shared = sharedGetParametersForStorageWorkout();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/ListOfTaskName", "GetStorageTaskList", shared[0], shared[1]);
        try {
            checkPostResultTaskList(showResult());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerViews() {
        listView_Tasks = (ListView) findViewById(R.id.list_WWA_Tasks);
    }

    private void defineArrayAdapter(){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext()
                , android.R.layout.simple_list_item_1, strArr_Tasks);
        listView_Tasks.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Workout");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        registerForContextMenu(listView_Tasks);

        // React to user clicks on item
        listView_Tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                String dataTaskName = (String) parentAdapter.getItemAtPosition(position);
                String[] sharedWorkoutName = sharedGetParametersForStorageWorkout();
                sharedPutParametersStorageTask(sharedWorkoutName[1],dataTaskName);

                Intent intentItemPress_MW;
                intentItemPress_MW = new Intent(Activity_Workout_Without_Action.this, Activity_Task_Without_Action.class);

                startActivity(intentItemPress_MW);
            }
        });

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

    private void checkPostResultTaskList(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            String[] jsonArr ;
            jsonArr = getJsonArray(json);
            jsonArr = aesDecrypt(jsonArr);
            strArr_Tasks = new ArrayList<>();
            Collections.addAll(strArr_Tasks, jsonArr);
        } else {
            Toast.makeText(this, "This task already exist !!!",
                    Toast.LENGTH_LONG).show();
        }
    }
    private String[] getJsonArray(JSONObject json){
        String[] modelNames = null;
        try {
            JSONArray tasks = json.getJSONArray("tasksList");
            modelNames = new String[tasks.length()];
            for (int i = 0; i< tasks.length() ; i++ ){
                modelNames[i] = tasks.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelNames;
    }

    private String[] aesDecrypt(String[] encryptedText) throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        for(int i=0;i<encryptedText.length;i++)
        {
            encryptedText[i] = AES.decrypt(encryptedText[i],shared[1]);
        }
        return  encryptedText;
    }

    private String[] sharedGetParametersForStorageWorkout()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return new String[]{editor.getString("storageUserName", null) ,editor.getString("storageWorkoutName", null)};
    }
    private void sharedPutParametersStorageTask(String storageWorkoutName, String storageTaskName)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("storageWorkoutName", storageWorkoutName);
        editor.putString("storageTaskName", storageTaskName);
        editor.apply();
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return new String[]{editor.getString("workoutName", null) ,editor.getString("aesKey", null)};
    }
}

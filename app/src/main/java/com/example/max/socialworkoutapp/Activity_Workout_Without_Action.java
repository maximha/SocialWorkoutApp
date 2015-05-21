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
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Activity_Workout_Without_Action extends ActionBarActivity {

    private ArrayAdapter<String> adapter;
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
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

        adapter = new ArrayAdapter<>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr_Tasks);
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

                if (intentItemPress_MW != null)
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
            for (int i = 0 ; i < jsonArr.length ; i++){

                strArr_Tasks.add(jsonArr[i]);
            }
        } else {
            Toast.makeText(this, "This task already exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
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
        String[] sharedData = {editor.getString("storageUserName", null) ,editor.getString("storageWorkoutName", null)};
        return  sharedData;
    }
    private void sharedPutParametersStorageTask(String storageWorkoutName, String storageTaskName)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("storageWorkoutName", storageWorkoutName);
        editor.putString("storageTaskName", storageTaskName);
        editor.commit();
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("workoutName", null) ,editor.getString("aesKey", null)};
        return  sharedData;
    }
}

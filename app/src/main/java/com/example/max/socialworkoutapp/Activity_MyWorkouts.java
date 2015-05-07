package com.example.max.socialworkoutapp;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Activity_MyWorkouts extends ActionBarActivity{

    private ArrayList<String> strArr;
    private ListView listView_MyWorkouts;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "State";
    final Context context = this;
    private PostHelper SHelper;

    private boolean flag = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_workouts_page);


        registerViews();

        try {
            ShowWorkoutsList();
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

    public void registerViews() {
        listView_MyWorkouts = (ListView) findViewById(R.id.list_MyWorkouts);
    }

    private  void  ShowWorkoutsList() throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        String encryptedUsername = AES.encrypt(shared[0],shared[1]);
        SHelper = new PostHelper();
        SHelper.execute("http://localhost:36301/api/ListOfWorkoutsName","ListOfWorkoutsName", encryptedUsername);
        try {
            checkPostResultWorkoutList(showResult());
            ///checkPostResult(showResult());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void defineArrayAdapter(){
        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr);
        listView_MyWorkouts.setAdapter(adapter);

        registerForContextMenu(listView_MyWorkouts);

        // React to user clicks on item
        listView_MyWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                String data=(String)parentAdapter.getItemAtPosition(position);

                sharedPut(data);

                Intent intentItemPress_MW = null;
                intentItemPress_MW = new Intent(Activity_MyWorkouts.this, Activity_Workout.class);

                if(intentItemPress_MW != null)
                    startActivity(intentItemPress_MW);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Option of item "+info.position);
        inflater.inflate(R.menu.list_item_contex_men, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.context_menu_upload:
                return true;
            case R.id.context_menu_delete:
                //removeItemFromList(info.position);
                strArr.remove(info.position);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    // method to remove item fom list
    /*protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                Activity_MyWorkouts.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete "+strArr.get(position)+" ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                strArr.remove(deletePosition);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("My Workouts");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        registerForContextMenu(listView_MyWorkouts);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_workout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.btn_add_workout){
            Log.d(TAG, "Plus Button Pressed");
            createDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog()
    {
        LayoutInflater li = LayoutInflater.from(context);
        View dialog_add_workout_name = li.inflate(R.layout.dialog_add_workout_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Add workout name");
        alertDialogBuilder.setView(dialog_add_workout_name);
        final EditText workoutNameInput = (EditText)dialog_add_workout_name.findViewById(R.id.et_dialog_add_workout_name);
        //flag = checkValidation();
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                String inputResult = (workoutNameInput.getText().toString());
                if(true) {
                    String[] shared = sharedGet();
                    SHelper = new PostHelper();
                    SHelper.execute("http://localhost:36301/api/addworkout","Workout", shared[0], inputResult);
                    try {
                        strArr.add(inputResult);
                        checkPostResult(showResult());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } /*else {
                    Toast.makeText(this, "Form contains error", Toast.LENGTH_LONG)
                            .show();
                }*/
                //strArr.add(inputResult);
                //defineArrayAdapter();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //check validation
    /*private boolean checkValidation(String input) {
        boolean ret = true;
        EditText textToCheck ;
        textToCheck.setText(input);

        if (!EditText_Validators.isName(userInput, true)) ret = false;
        return ret;
    }*/

    // check if user allow to register
    public void checkPostResult(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            // Save Button in Create New Task page
            defineArrayAdapter();
            //Intent intentMyWorkouts = new Intent(this, Activity_MyWorkouts.class);
            //startActivity(intentMyWorkouts);
        } else {
            Toast.makeText(this, "This workout already exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
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

    private void sharedPut(String workoutName)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("workoutName", workoutName);
        editor.commit();
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("userName", null) ,editor.getString("aesKey", null)};
        return  sharedData;
    }

    private void checkPostResultWorkoutList(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            String[] jsonArr = null;
            String[] decryptedData = null;
            jsonArr = getJsonArray(json);
            decryptedData = aesDecrypt(jsonArr);
            strArr = new ArrayList<String>();
            for (int i = 0 ; i < jsonArr.length ; i++){

                strArr.add(decryptedData[i]);
            }
        } else {
            Toast.makeText(this, "This workout already exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    private String[] aesDecrypt(String[] encryptedText) throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        for(int i=0;i<encryptedText.length;i++)
        {
            encryptedText[i] = AES.decrypt(encryptedText[i],shared[1]);
        }
        return  encryptedText;
    }

    private String[] getJsonArray(JSONObject json){
        String[] modelNames = null;
        try {
            JSONArray workouts = json.getJSONArray("workouts");
            modelNames = new String[workouts.length()];
            for (int i = 0; i< workouts.length() ; i++ ){
                modelNames[i] = workouts.getString(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelNames;
    }
}
package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class Activity_StorageWorkouts extends ActionBarActivity implements View.OnClickListener {

    ArrayList<Model_StorageItem> strArrStorage;
    private ArrayList<String> arrOfNames;
    private ListView listView_StorageWorkouts;
    private ArrayAdapter<String> adapter;
    final Context context = this;
    private PostHelper SHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_workouts_page);

        registerViews();

        try {
            ShowStorageWorkoutsList();
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

        defineAdapter();
    }

    public void registerViews()
    {
        listView_StorageWorkouts = (ListView) findViewById(R.id.list_Storage);
    }

    private  void  ShowStorageWorkoutsList() throws GeneralSecurityException, IOException {
        //String[] shared = sharedGet();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/GetStorageWorkoutsList","GetStorageWorkoutsList");
        try {
            checkPostResultStorageList(showResult());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void defineAdapter()
    {
        adapter = new ArrayAdapter<>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , arrOfNames);
        listView_StorageWorkouts.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Storage");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        registerForContextMenu(listView_StorageWorkouts);

        listView_StorageWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                String dataWorkoutName = (String) parentAdapter.getItemAtPosition(position);
                String dataUserName = strArrStorage.get(position).getUserName();

                sharedPutParametersStorageWorkout(dataUserName, dataWorkoutName);

                Intent intentItemPress_SW = null;
                intentItemPress_SW = new Intent(Activity_StorageWorkouts.this, Activity_Workout_Without_Action.class);

                if (intentItemPress_SW != null)
                    startActivity(intentItemPress_SW);
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Option of item " + arrOfNames.get(info.position));
        inflater.inflate(R.menu.add_to_favorites_menu, menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("Favorites");
        //menu.add("Top 10");

        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.add_to_favorites_menu:
                addItemToFavoritesList(info.position);
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // int id = item.getItemId();

        if(item.getTitle() == "Favorites"){
            Intent intentAddToFavorites = new Intent(this, Activity_Favorites.class);
            startActivity(intentAddToFavorites);
        }
        return super.onOptionsItemSelected(item);
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
    private void checkPostResultStorageList(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            ArrayList<JSONObject> jsonArr;
            ArrayList<Model_StorageItem> decryptedData;
            jsonArr = getJsonArray(json);
            decryptedData = aesDecrypt(jsonArr);
            strArrStorage = new ArrayList<>();
            for (int i = 0 ; i < jsonArr.size() ; i++){
                strArrStorage.add(decryptedData.get(i));
            }
        } else {
            Toast.makeText(this, "Problem get workouts list !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    private ArrayList<Model_StorageItem> aesDecrypt(ArrayList encryptedText) throws GeneralSecurityException, IOException, JSONException {
        String[] shared = sharedGet();

        //Model_StorageItem model = new Model_StorageItem();
        ArrayList<Model_StorageItem> decryptedText;
        decryptedText = new ArrayList<>();
        arrOfNames = new ArrayList<>();
        for(int i=0;i<encryptedText.size();i++)
        {
            Model_StorageItem model = new Model_StorageItem();
            JSONObject jsonObject = (JSONObject)encryptedText.get(i);
            model.setUserName(AES.decrypt(jsonObject.getString("userName"), shared[1]));
            model.setWorkoutName(AES.decrypt(jsonObject.getString("workoutName"), shared[1]));
            model.setInStorage(jsonObject.getBoolean("inStorage"));
            arrOfNames.add(model.getWorkoutName());
            decryptedText.add(i,model);
        }
        return  decryptedText;
    }

    private ArrayList<JSONObject> getJsonArray(JSONObject json){
        ArrayList<JSONObject> modelWorkouts;
        modelWorkouts = null;
        try {
            JSONArray workouts = json.getJSONArray("storageWorkouts");
            modelWorkouts = new ArrayList<>();
            for (int i = 0; i< workouts.length() ; i++ )
            {
                modelWorkouts.add(workouts.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelWorkouts;
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("userName", null) ,editor.getString("aesKey", null)};
        return  sharedData;
    }

    private void sharedPutParametersStorageWorkout(String storageUserName, String storageWorkoutName)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("storageUserName", storageUserName);
        editor.putString("storageWorkoutName", storageWorkoutName);
        editor.commit();
    }

    private String[] sharedGetParametersForStorageWorkout()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("storageUserName", null) ,editor.getString("storageWorkoutName", null)};
        return  sharedData;
    }

    // method to add item to favorites list
    protected void addItemToFavoritesList(int position) {
        //final int uploadPosition = position;
        ArrayList<Model_StorageItem> str = strArrStorage;
        String dataWorkoutName = arrOfNames.get(position);
        String dataUserName = strArrStorage.get(position).getUserName();

        sharedPutParametersStorageWorkout(dataUserName,dataWorkoutName);

        AlertDialog.Builder alert = new AlertDialog.Builder(Activity_StorageWorkouts.this);

        alert.setTitle("Upload");
        alert.setMessage("Do you want upload " + arrOfNames.get(position) + " ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] shared = sharedGet();
                String[] sharedGetParameter = sharedGetParametersForStorageWorkout();
                SHelper = new PostHelper(context);
                SHelper.execute("http://localhost:36301/api/AddWorkoutToFavorites","AddWorkoutToFavorites", shared[0], sharedGetParameter[0],sharedGetParameter[1]);
                try {

                    checkPostResultAfterAdded(showResult());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Storage");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        registerForContextMenu(listView_StorageWorkouts);
    }

    // check if workout upload success
    public void checkPostResultAfterAdded(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            Toast.makeText(this, "Workout Added !!!",
                    Toast.LENGTH_LONG).show();
            defineAdapter();
        } else {
            Toast.makeText(this, "This workout is already in favorites  !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_Back_Storage:
                Log.d(TAG, "Back-Storage Button Pressed");
                // Back Button in Storage Workouts page
                Intent intentBack_Storage = new Intent(this, HomeMenu.class);
                startActivity(intentBack_Storage);
                break;*/
            default:
                break;
        }

    }
}

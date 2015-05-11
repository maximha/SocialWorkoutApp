package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Activity_StorageWorkouts extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<Model_StorageItem> strArrStorage;
    private ArrayList<String> arr;
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
                , android.R.layout.simple_list_item_1 , arr);
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

                sharedPutParametersStorageWorkout(dataUserName ,dataWorkoutName);

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
        menu.setHeaderTitle("Option of item " + info.position);
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
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // int id = item.getItemId();

        if(item.getTitle() == "Favorites"){
            Intent intentAddToFavorites = new Intent(this, Activity_AddToFavorites.class);
            startActivity(intentAddToFavorites);
        }
        /*if (item.getTitle() == "Top 10"){
            Toast.makeText(Activity_StorageWorkouts.this, "TOP 10", Toast.LENGTH_SHORT).show();
            //Intent intentChangeProf = new Intent(this, ChangeProfile.class);
            //startActivity(intentChangeProf);
        }*/

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
            Toast.makeText(this, "This workout already exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    private ArrayList<Model_StorageItem> aesDecrypt(ArrayList encryptedText) throws GeneralSecurityException, IOException, JSONException {
        String[] shared = sharedGet();

        Model_StorageItem model = new Model_StorageItem();
        ArrayList<Model_StorageItem> decryptedText;
        decryptedText = new ArrayList<>();
        arr = new ArrayList<>();
        for(int i=0;i<encryptedText.size();i++)
        {
            JSONObject jsonObject = (JSONObject)encryptedText.get(i);
            model.setUserName(AES.decrypt(jsonObject.getString("userName"), shared[1]));
            model.setWorkoutName(AES.decrypt(jsonObject.getString("workoutName"), shared[1]));
            model.setInStorage(jsonObject.getBoolean("inStorage"));
            arr.add(model.getWorkoutName());
            decryptedText.add(model);
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

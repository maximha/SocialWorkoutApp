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

public class Activity_Favorites extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<Model_Favorites> strArrFavorites;
    private ArrayList<String> arrOfNames;
    private ListView listView_FavoritesWorkouts;
    private ArrayAdapter<String> adapter;
    final Context context = this;
    private PostHelper SHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_favorites_page);

        registerViews();

        try {
            ShowFavoritesWorkoutsList();
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
        listView_FavoritesWorkouts = (ListView) findViewById(R.id.list_Add_To_Favorites_Workouts);
    }

    private  void  ShowFavoritesWorkoutsList() throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/FavoritesList", "GetFavoritesWorkoutsList", shared[0]);
        try {
            checkPostResultFavoritesList(showResult());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void defineAdapter()
    {
        adapter = new ArrayAdapter<>(getApplicationContext()
                , android.R.layout.simple_list_item_1, arrOfNames);
        listView_FavoritesWorkouts.setAdapter(adapter);

        registerForContextMenu(listView_FavoritesWorkouts);

        listView_FavoritesWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                String dataWorkoutName = (String) parentAdapter.getItemAtPosition(position);
                String dataUserName = strArrFavorites.get(position).getUserName();

                sharedPutParametersStorageWorkout(dataUserName, dataWorkoutName);

                Intent intentItemPress_SW = null;
                intentItemPress_SW = new Intent(Activity_Favorites.this, Activity_Workout_Without_Action.class);

                if (intentItemPress_SW != null)
                    startActivity(intentItemPress_SW);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Option of item " + arrOfNames.get(info.position));
        inflater.inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_delete:
                removeItemFromList(info.position);
                return true;
            default:
                return false;
        }
    }

    // method to remove list item
    protected void removeItemFromList(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                Activity_Favorites.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete "+ arrOfNames.get(position)+" ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String[] shared = sharedGet();
                String dataUserName = strArrFavorites.get(deletePosition).getUserName();
                String dataWorkoutName = strArrFavorites.get(deletePosition).getWorkoutName();

                //sharedPutParametersStorageWorkout(dataUserName, dataWorkoutName);

                SHelper = new PostHelper(context);
                SHelper.execute("http://localhost:36301/api/DeleteWorkoutFromFavoritesList","DeleteWorkoutFromFavoritesList", shared[0], dataUserName, dataWorkoutName);
                try {
                    checkPostResultAfterDelete(showResult(),deletePosition);
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

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        registerForContextMenu(listView_FavoritesWorkouts);
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

    /*private String[] sharedGetParametersForStorageWorkout()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("storageUserName", null) ,editor.getString("storageWorkoutName", null)};
        return  sharedData;
    }*/

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

    public void checkPostResultAfterDelete(String result , int deletePosition) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            arrOfNames.remove(deletePosition);
            adapter.notifyDataSetChanged();
            adapter.notifyDataSetInvalidated();
            defineAdapter();
        } else {
            Toast.makeText(this, "Deleting Workout failure !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void checkPostResultFavoritesList(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            ArrayList<JSONObject> jsonArr;
            ArrayList<Model_Favorites> decryptedData;
            jsonArr = getJsonArray(json);
            decryptedData = aesDecrypt(jsonArr);
            strArrFavorites = new ArrayList<>();
            for (int i = 0 ; i < jsonArr.size() ; i++){
                strArrFavorites.add(decryptedData.get(i));
            }
        } else {
            Toast.makeText(this, "Problem get workouts list !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    private ArrayList<Model_Favorites> aesDecrypt(ArrayList encryptedText) throws GeneralSecurityException, IOException, JSONException {
        String[] shared = sharedGet();

        ArrayList<Model_Favorites> decryptedText;
        decryptedText = new ArrayList<>();
        arrOfNames = new ArrayList<>();
        for(int i=0;i<encryptedText.size();i++)
        {
            Model_Favorites model = new Model_Favorites();
            JSONObject jsonObject = (JSONObject)encryptedText.get(i);
            model.setMasterUserName(AES.decrypt(jsonObject.getString("masterUser"), shared[1]));
            model.setUserName(AES.decrypt(jsonObject.getString("userName"), shared[1]));
            model.setWorkoutName(AES.decrypt(jsonObject.getString("workoutName"), shared[1]));
            arrOfNames.add(model.getWorkoutName());
            decryptedText.add(model);
        }
        return  decryptedText;
    }

    private ArrayList<JSONObject> getJsonArray(JSONObject json){
        ArrayList<JSONObject> modelWorkouts;
        modelWorkouts = null;
        try {
            JSONArray workouts = json.getJSONArray("favoritesWorkouts");
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
    public void onClick(View v) {
    }
}

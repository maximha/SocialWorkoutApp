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

public class Activity_Workout extends ActionBarActivity  {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> strArr_Tasks;

    private ListView listView_Tasks;
    private PostHelper SHelper;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_page);

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
        String[] shared = sharedGet();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/ListOfTaskName", "ListOfTaskName", shared[0]);
        try {
            checkPostResultTaskList(showResult());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerViews() {
        listView_Tasks = (ListView) findViewById(R.id.list_tasks_name);
    }

    private void defineArrayAdapter(){

        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr_Tasks);
        listView_Tasks.setAdapter(adapter);

        registerForContextMenu(listView_Tasks);

        // React to user clicks on item
        listView_Tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                String data = (String) parentAdapter.getItemAtPosition(position);

                sharedPut(data);

                Intent intentItemPress_MW ;
                intentItemPress_MW = new Intent(Activity_Workout.this, Activity_Task.class);

                if (intentItemPress_MW != null)
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
        menu.setHeaderTitle("Option of item " + info.position);
        inflater.inflate(R.menu.delete_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId())
        {
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
        String dataWorkoutNAme = strArr_Tasks.get(position);

        sharedPut(dataWorkoutNAme);

        AlertDialog.Builder alert = new AlertDialog.Builder(
                Activity_Workout.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete "+strArr_Tasks.get(position)+" ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] shared = sharedGet();
                SHelper = new PostHelper(context);
                SHelper.execute("http://localhost:36301/api/DeleteTask","DeleteTask", shared[0], sharedGetTaskName());
                try {
                    checkPostResultAfterDelete(showResult(), deletePosition);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // main code on after clicking yes
                //strArr_Tasks.remove(deletePosition);
                //adapter.notifyDataSetChanged();
                //adapter.notifyDataSetInvalidated();

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
        actionBar.setTitle("Workout");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        registerForContextMenu(listView_Tasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_task, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.btn_add_task){
            Intent intentPlus_W = new Intent(this, Activity_CreateNewTask.class);
            startActivity(intentPlus_W);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sharedPut(String taskName)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("taskName", taskName);
        editor.commit();
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("workoutName", null) ,editor.getString("aesKey", null)};
        return  sharedData;
    }

    private String sharedGetTaskName()
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

    public void checkPostResultAfterDelete(String result , int deletePosition) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            strArr_Tasks.remove(deletePosition);
            adapter.notifyDataSetChanged();
            adapter.notifyDataSetInvalidated();
            defineArrayAdapter();
        } else {
            Toast.makeText(this, "This task not exist !!!",
                    Toast.LENGTH_LONG).show();
            return;
        }
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


}

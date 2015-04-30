package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

public class Activity_StorageWorkouts extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<String> strArrStorage;
    private ListView listView_StorageWorkouts;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "State";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_workouts_page);

        listView_StorageWorkouts = (ListView) findViewById(R.id.list_Storage);
        strArrStorage = new ArrayList<String>();
        for (int i = 0 ; i < 15 ; i++){
            strArrStorage.add("WORKOUT : " + i);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArrStorage);
        listView_StorageWorkouts.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Storage");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        registerForContextMenu(listView_StorageWorkouts);

        /*listView_StorageWorkouts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // setting onItemLongClickListener and passing the position to the function
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                return true;
            }
        });*/
        listView_StorageWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // We know the View is a TextView so we can cast it
                //TextView clickedView = (TextView) view;
                //Toast.makeText(MyWorkouts.this, "Item with id ["+id+"] - Position ["+position+"] - WORK ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
                Intent intentItemPress_SW = null;
                intentItemPress_SW = new Intent(Activity_StorageWorkouts.this, Activity_Workout_Without_Action.class);

                if(intentItemPress_SW != null)
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
        menu.add("Top 10");

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        if(item.getTitle() == "Favorites"){
            Intent intentAddToFavorites = new Intent(this, Activity_AddToFavorites.class);
            startActivity(intentAddToFavorites);
        }
        if (item.getTitle() == "Top 10"){
            Toast.makeText(Activity_StorageWorkouts.this, "TOP 10", Toast.LENGTH_SHORT).show();
            //Intent intentChangeProf = new Intent(this, ChangeProfile.class);
            //startActivity(intentChangeProf);
        }

        return super.onOptionsItemSelected(item);
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

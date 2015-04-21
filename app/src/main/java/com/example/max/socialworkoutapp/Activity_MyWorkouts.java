package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Activity_MyWorkouts extends ActionBarActivity implements View.OnClickListener {

    private Button btnActPlus;
    private ArrayList<String> strArr;
    private ListView listView_MyWorkouts;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_workouts_page);

        registerViews();

        strArr = new ArrayList<String>();
        for (int i = 0 ; i < 15 ; i++){
            strArr.add("WORKOUT : " + i);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr);
        listView_MyWorkouts.setAdapter(adapter);

        registerForContextMenu(listView_MyWorkouts);

        // React to user clicks on item
        listView_MyWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // We know the View is a TextView so we can cast it
                //TextView clickedView = (TextView) view;
                //Toast.makeText(MyWorkouts.this, "Item with id ["+id+"] - Position ["+position+"] - WORK ["+clickedView.getText()+"]", Toast.LENGTH_SHORT).show();
                Intent intentItemPress_MW = null;
                intentItemPress_MW = new Intent(Activity_MyWorkouts.this, Activity_Workout.class);

                if(intentItemPress_MW != null)
                    startActivity(intentItemPress_MW);
            }
        });
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Home");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    public void registerViews() {
        listView_MyWorkouts = (ListView) findViewById(R.id.list_MyWorkouts);

        btnActPlus = (Button) findViewById(R.id.btn_Plus_MW);
        btnActPlus.setOnClickListener(this);
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
                removeItemFromList(info.position);
                return true;
            default:
                return false;
        }
    }

    // method to remove item fom list
    protected void removeItemFromList(int position) {
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

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Workouts");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        registerForContextMenu(listView_MyWorkouts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_workout, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        /*if(item.getTitle() == "Add"){
            Log.d(TAG, "Plus Button Pressed");
            // Plus Button in My Workouts page
            Intent intentPlus_MW = new Intent(this, Activity_CreateNewWorkout.class);
            startActivity(intentPlus_MW);
        }*/

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_Back_MW:
                Log.d(TAG, "Back Button Pressed");
                // Back Button in My Workouts page
                Intent intentBack_MW = new Intent(this, HomeMenu.class);
                startActivity(intentBack_MW);
                break;*/
            case R.id.btn_Plus_MW:
                Log.d(TAG, "Plus Button Pressed");
                // Plus Button in My Workouts page
                Intent intentPlus_MW = new Intent(this, Activity_CreateNewWorkout.class);
                startActivity(intentPlus_MW);
                break;
            default:
                break;
        }
    }

    /*public void onListItemClick(ListView parent , View v , int position , long id){
        //Toast.makeText(this , "You have select" + parent. , Toast.LENGTH_SHORT);

        // Check what workout was pressed and go to page of Workout
        //if(v.getId() == parent.getCheckedItemPosition()){
            Intent intentItemPress_MW = new Intent(this, Workout.class);
            startActivity(intentItemPress_MW);
        //}
    }*/

}
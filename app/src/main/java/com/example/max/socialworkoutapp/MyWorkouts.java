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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyWorkouts extends ActionBarActivity implements View.OnClickListener {

    //private Button btnActBack;
    private Button btnActPlus;
    private ArrayList<String> strArr;
    private ListView listView_MyWorkouts;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_workouts_page);

        //btnActBack = (Button) findViewById(R.id.btn_Back_MW);
        //btnActBack.setOnClickListener(this);

        btnActPlus = (Button) findViewById(R.id.btn_Plus_MW);
        btnActPlus.setOnClickListener(this);

        listView_MyWorkouts = (ListView) findViewById(R.id.list_MyWorkouts);

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
                intentItemPress_MW = new Intent(MyWorkouts.this, Workout.class);

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
                MyWorkouts.this);

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


    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("Delete Workout");

        return true;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        if(item.getTitle() == "Delete Workout"){
            Toast.makeText(this , "Item was DELETED" , Toast.LENGTH_LONG).show();
        }

        return true;
    }*/


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
                Intent intentPlus_MW = new Intent(this, CreateNewWorkout.class);
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
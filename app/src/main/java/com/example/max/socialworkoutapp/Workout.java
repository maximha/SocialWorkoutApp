package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import org.json.JSONArray;

import java.util.ArrayList;

public class Workout  extends ActionBarActivity implements View.OnClickListener {

    private TextView taskView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> strArr_Tasks;

    public ProgressDialog pDialog;
    private Button btnActPlus_W;
    private ListView listView_Tasks;
    //ArrayList<WorkoutItem> workoutList;
    //WorkoutItemAdapter adapter;

    // contacts JSONArray
    //JSONArray jsonWorkout = null;

    private static final String TAG = "State";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_page);

        btnActPlus_W = (Button) findViewById(R.id.btn_Plus_W);
        btnActPlus_W.setOnClickListener(this);

        listView_Tasks = (ListView) findViewById(R.id.list_Tasks);

        //workoutList = new ArrayList<WorkoutItem>();

        strArr_Tasks = new ArrayList<String>();
        for (int i = 0 ; i < 15 ; i++){
            strArr_Tasks.add(i,"TASK : " + i);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_list_item_1 , strArr_Tasks);
        listView_Tasks.setAdapter(adapter);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Workout");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        //new GetWorkout().execute("");// Http or JSON path

        registerForContextMenu(listView_Tasks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Option of item "+info.position);
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

        AlertDialog.Builder alert = new AlertDialog.Builder(
                Workout.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete "+strArr_Tasks.get(position)+" ?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                strArr_Tasks.remove(deletePosition);
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
        actionBar.setTitle("Workout");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

        registerForContextMenu(listView_Tasks);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Plus_W:
                Log.d(TAG, "Plus Button Pressed");
                // Plus Button in Workout page
                Intent intentPlus_W = new Intent(this, CreateNewTask.class);
                startActivity(intentPlus_W);
                //strArr_Tasks.add("Task : " + 25);
                //adapter = new ArrayAdapter<String>(getApplicationContext()
                //        , android.R.layout.simple_list_item_1 , strArr_Tasks);
                //listView_Tasks.setAdapter(adapter);
                break;
            default:
                break;
        }
    }

    /*Override
    public void onListItemClick(ListView parent , View v , int position , long id){
        //Toast.makeText(this , "You have select" + parent. , Toast.LENGTH_SHORT);

    }*/

}

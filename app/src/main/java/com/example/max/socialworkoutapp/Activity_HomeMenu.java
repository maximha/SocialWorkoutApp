package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

public class Activity_HomeMenu extends ActionBarActivity implements View.OnClickListener {

    private Button btnActMyWorkouts;
    private TextView tv_FirstName ,tv_LastName ;
    final Context context = this;
    private PostHelper SHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_menu_page);

        btnActMyWorkouts = (Button) findViewById(R.id.btn_MyWorkouts);

        registerViews();

        try {
            ShowUserProperty();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        defineAdapter();
    }

    private  void  ShowUserProperty() throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        SHelper = new PostHelper(context);
        SHelper.execute("http://localhost:36301/api/GetUserProperty", "GetUserProperty", shared[0]);
        try {
            checkPostResultUserProperty(showResult());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void registerViews() {

        tv_FirstName = (TextView) findViewById(R.id.label_FirstName);
        tv_LastName = (TextView) findViewById(R.id.label_LastName);

        btnActMyWorkouts.setOnClickListener(this);

        Button btnActStorageWorkouts = (Button) findViewById(R.id.btn_Storage);
        btnActStorageWorkouts.setOnClickListener(this);
    }

    private void defineAdapter()
    {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add("Log Out");
        menu.add("Change Profile");

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();

        if(item.getTitle() == "Log Out"){
            Intent intentLogOut = new Intent(this, Activity_Login.class);
            startActivity(intentLogOut);
        }
        if (item.getTitle() == "Change Profile"){
            Intent intentChangeProf = new Intent(this, Activity_ChangeProfile.class);
            startActivity(intentChangeProf);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_MyWorkouts:
                // MyWorkouts Button in Home menu page
                Intent intentMyWorkouts = new Intent(this, Activity_MyWorkouts.class);
                startActivity(intentMyWorkouts);
                break;
            case R.id.btn_Storage:
                // Storage Button in Home menu page
                Intent intentStorageWorkouts = new Intent(this, Activity_StorageWorkouts.class);
                startActivity(intentStorageWorkouts);
                break;
            default:
                break;
        }
    }

    private String[] sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return new String[]{editor.getString("userName", null) ,editor.getString("aesKey", null)};
    }

    // get response from http request
    private String showResult() {
        if (SHelper == null)
            return null;
        try {
            return SHelper.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkPostResultUserProperty(String result) throws JSONException, GeneralSecurityException, IOException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            Model_Registration model ;
            model = getJsonArray(json);
            model = aesDecrypt(model);
            tv_FirstName.setText(model.getFirstName());
            tv_LastName.setText(model.getLastName());
        } else {
            Toast.makeText(this, "Problem get user property !!!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private Model_Registration getJsonArray(JSONObject json){
        Model_Registration model = new Model_Registration();
        try {
            JSONObject c = json.getJSONObject("userProperty");
            String fName = c.getString("firstName");
            String lName = c.getString("lastName");
            String usName = c.getString("userName");
            String pass = c.getString("password");

            model.setFirstName(fName);
            model.setLastName(lName);
            model.setUserName(usName);
            model.setPassword(pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    private Model_Registration aesDecrypt(Model_Registration encryptedModel) throws GeneralSecurityException, IOException {
        String[] shared = sharedGet();
        encryptedModel.setFirstName(AES.decrypt(encryptedModel.getFirstName(), shared[1]));
        encryptedModel.setLastName(AES.decrypt(encryptedModel.getLastName(), shared[1]));
        encryptedModel.setUserName(AES.decrypt(encryptedModel.getUserName(), shared[1]));
        encryptedModel.setPassword(AES.decrypt(encryptedModel.getPassword(), shared[1]));
        return  encryptedModel;
    }


}

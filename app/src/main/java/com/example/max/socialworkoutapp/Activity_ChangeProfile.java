package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Activity_ChangeProfile extends ActionBarActivity implements View.OnClickListener {

    EditText et_FirstName , et_LastName , et_Pass , et_ConfirmPass;

    private Button btnActSave;
    private PostHelper SHelper;
    private KeyPair pair;
    private String decryptedAesKey ;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_profile);

        registerViews();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Profile");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    public void registerViews() {
        et_FirstName = (EditText) findViewById(R.id.editText_FN_ChangeProf);
        et_LastName = (EditText) findViewById(R.id.editText_LN_ChangeProf);
        et_Pass = (EditText) findViewById(R.id.editText_Pass_ChangeProf);
        et_ConfirmPass = (EditText) findViewById(R.id.editText_ConfPass_ChangeProf);

        btnActSave = (Button) findViewById(R.id.btn_Save_ChangeProf);
        btnActSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save_ChangeProf:
                if(checkValidation()) {
                    changeProfileConnection();
                    try {
                        checkPostResult(showResult());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Form contains error", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                break;
        }
    }
    public  void changeProfileConnection(){
        String[] shared = sharedGetAES();
        String aesKey = shared[1];
        SHelper = new PostHelper(context);
        try {
            SHelper.execute("http://localhost:36301/api/ChangeProfile","ChangeProfile", AES.encrypt(et_FirstName.getText().toString(), aesKey),
                    AES.encrypt(et_LastName.getText().toString(), aesKey), AES.encrypt(sharedGet(), aesKey),
                    AES.encrypt(et_Pass.getText().toString(), aesKey));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    // check if user allow to register
    public void checkPostResult(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            Intent intentSaveChangeProf = new Intent(this, Activity_HomeMenu.class);
            startActivity(intentSaveChangeProf);
        } else {
            Toast.makeText(this, "Change Profile failure !!!",
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


    //check validation
    private boolean checkValidation() {
        boolean ret = true;

        if (!EditText_Validators.isName(et_FirstName, true)) ret = false;
        if (!EditText_Validators.isName(et_LastName, true)) ret = false;
        if (!EditText_Validators.isPassword(et_Pass, true)) ret = false;
        if (!EditText_Validators.isPassword(et_ConfirmPass, true)) ret = false;
        if (!EditText_Validators.isConfirm(et_Pass, et_ConfirmPass)) ret = false;
        return ret;
    }

    private String sharedGet()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        return  editor.getString("userName", null);
    }
    private String[] sharedGetAES()
    {
        SharedPreferences editor = getSharedPreferences("shared_Memory", MODE_PRIVATE);
        String[] sharedData = {editor.getString("workoutName", null) ,editor.getString("aesKey", null)};
        return  sharedData;
    }
}

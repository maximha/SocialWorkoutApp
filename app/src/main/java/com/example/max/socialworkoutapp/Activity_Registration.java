package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Activity_Registration extends ActionBarActivity implements View.OnClickListener {

    EditText et_FirstName , et_LastName , et_UserName , et_Pass , et_ConfirmPass;

    private PostHelper SHelper;
    final Context context = this;
    private KeyPair pair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        registerViews();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registration");

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );
    }

    public void registerViews() {
        et_FirstName = (EditText) findViewById(R.id.editText_FirstName);
        et_LastName = (EditText) findViewById(R.id.editText_LastName);
        et_UserName = (EditText) findViewById(R.id.editText_UserRegistr);
        et_Pass = (EditText) findViewById(R.id.editText_PassRegistr);
        et_ConfirmPass = (EditText) findViewById(R.id.editText_ConfirmPassRegistr);

        Button btnActRegistration = (Button) findViewById(R.id.btn_Registration);
        btnActRegistration.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Registration:
                if(checkValidation()) {
                    pair = RSA.generatePair(); //generate RSA private and public keys
                    PublicKey publicKey = pair.getPublic(); //get RSA public key
                    String pubKeyBytes = Base64.encodeToString(publicKey.getEncoded(), 0); //encode RSA public key to base 64 string format

                    SHelper = new PostHelper(context);
                    SHelper.execute("http://localhost:36301/api/registrationkey","RegistrationKey", pubKeyBytes);
                    try {
                        try {
                            checkPostResultPreRegistration(showResult());
                        } catch (GeneralSecurityException | IOException e) {
                            e.printStackTrace();
                        }
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

    public void checkPostResultPreRegistration(String result) throws JSONException, GeneralSecurityException, IOException//check if user allow to get stream
    {
        JSONObject json = new JSONObject(result);
        if (json.getBoolean("result")) {
            String aesKey;
            aesKey = getJesonArray(json); //get encrypted key from server
            String decryptedAesKey = decryptKey(aesKey);
            SHelper = new PostHelper(context);
            SHelper.execute("http://localhost:36301/api/registration","Registration", AES.encrypt(et_FirstName.getText().toString(), decryptedAesKey), AES.encrypt(et_LastName.getText().toString(), decryptedAesKey),AES.encrypt(et_UserName.getText().toString(), decryptedAesKey), AES.encrypt(et_Pass.getText().toString(), decryptedAesKey));
            try {
                        checkPostResult(showResult());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

        } else {
            Toast.makeText(
                    this,
                    "Try again !!!", Toast.LENGTH_LONG)
                    .show();
            // Registration Button
            Intent intentRegistration = new Intent(this , Activity_Registration.class);
            startActivity(intentRegistration);
        }
    }

    // check if user allow to register
    public void checkPostResult(String result) throws JSONException {
        JSONObject json = new JSONObject(result);
        if(json.getBoolean("result")){
            Intent intentRegistration = new Intent(this, Activity_Login.class);
            startActivity(intentRegistration);
            finish();
        } else Toast.makeText(this, "This user already exist !!!",
                Toast.LENGTH_LONG).show();
    }

    private String getJesonArray(JSONObject json) {
        String keyRsult = null;
        try {
            keyRsult = json.getString("publicKey");
        } catch (JSONException e) {
            e.printStackTrace();
        } // convert String to JSONObject
        return keyRsult;
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

    private String decryptKey(String key) {
        String decryptedKey;
        if (key != null) {

            // set key pair to RSA class to encrypt/decrypt
            RSA rsa = new RSA(pair.getPrivate());

            byte[] encr = Base64.decode(key, 0);
            byte[] dec = null;
            try {
                dec = rsa.decryptRSA(encr);
            } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (dec == null) throw new AssertionError();
            byte[] base64 = Base64.encode(dec, Base64.NO_WRAP);
            decryptedKey = new String(base64);
            return decryptedKey;
        }
        return null;
    }

    //check validation
    private boolean checkValidation() {
        boolean ret = true;

        if (!EditText_Validators.isName(et_FirstName, true)) ret = false;
        if (!EditText_Validators.isName(et_LastName, true)) ret = false;
        if (!EditText_Validators.isName(et_UserName, true)) ret = false;
        if (!EditText_Validators.isPassword(et_Pass, true)) ret = false;
        if (!EditText_Validators.isPassword(et_ConfirmPass, true)) ret = false;
        if (!EditText_Validators.isConfirm(et_Pass, et_ConfirmPass)) ret = false;
        return ret;
    }
}

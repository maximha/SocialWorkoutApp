package com.example.max.socialworkoutapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.concurrent.ExecutionException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Activity_Login extends ActionBarActivity implements View.OnClickListener {

    EditText et_User , et_Password;

    private Button btnActLogin;
    private Button btnActHomeRegistration;
    private PostHelper SHelper;
    private boolean flag = true;
    private KeyPair pair;
    private String decryptedAesKey ;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        registerViews();

        // Action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE );

    }

    public void registerViews() {

        et_User = (EditText) findViewById(R.id.editText_User);
        et_Password = (EditText) findViewById(R.id.editText_Password);

        btnActLogin = (Button) findViewById(R.id.btn_LogIn);
        btnActLogin.setOnClickListener(this);

        btnActHomeRegistration = (Button) findViewById(R.id.btn_homeRegistration);
        btnActHomeRegistration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_LogIn :
                if(/*checkValidation ()*/true)//check if data entered is correct
                {
                    pair = RSA.generatePair(); //generate RSA private and public keys
                    PublicKey publicKey = pair.getPublic(); //get RSA public key
                    String pubKeyBytes = Base64.encodeToString(publicKey.getEncoded(), 0); //encode RSA public key to base 64 string format

                    SHelper = new PostHelper(context);
                    SHelper.execute("http://localhost:36301/api/login","LogIn", et_User.getText().toString(), et_Password.getText().toString(),pubKeyBytes);
                    try {
                        checkPostResult(showResult());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_homeRegistration :
                // Registration Button
                Intent intentRegistration = new Intent(this , Activity_Registration.class);
                startActivity(intentRegistration);
                break;
            default:
                break;
        }

    }

    public void checkPostResult(String result) throws JSONException//check if user allow to get stream
    {
        JSONObject json = new JSONObject(result);
        if (json.getBoolean("result")) {
            String aesKey;
            aesKey = getJesonArray(json); //get encrypted key from server
            decryptedAesKey = decryptKey(aesKey); //decrypt aes key with RSA private
            if (!flag) {
                Toast.makeText(this, "Try again !!! "+aesKey, Toast.LENGTH_LONG).show();
                return;
            }
            sharedPut(et_User.getText().toString(),decryptedAesKey); //put user name and aes key to shared memory
            Intent intentLogIn = new Intent(this, Activity_HomeMenu.class);
            startActivity(intentLogIn);
        } else {
            Toast.makeText(
                    this,
                    "User name or password is wrong" + "\n"
                            + "Please enter data again !!!", Toast.LENGTH_LONG)
                    .show();
        }
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

        if (!EditText_Validators.isName(et_User, true)) ret = false;
        if (!EditText_Validators.isPassword(et_Password, true)) ret = false;
        return ret;
    }

    private void sharedPut(String userName, String aesKey)
    {
        SharedPreferences.Editor editor = getSharedPreferences("shared_Memory", MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.putString("aesKey", aesKey);
        editor.commit();
    }

    private String decryptKey(String key) {
        String decryptedKey;
        if (key != null) {

            // set key pair to RSA class to encrypt/decrypt
            RSA rsa = new RSA(pair.getPrivate(), pair.getPublic());

                byte[] encr = Base64.decode(key, 0);
                byte[] dec = null;
                try {
                    dec = rsa.decryptRSA(encr);
                } catch (InvalidKeyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                byte[] base64 = Base64.encode(dec, Base64.NO_WRAP);
                decryptedKey = new String(base64);
                return decryptedKey;
        }
        return null;
    }
}

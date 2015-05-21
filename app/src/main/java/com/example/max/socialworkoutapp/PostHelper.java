package com.example.max.socialworkoutapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PostHelper extends AsyncTask<String, Void, String> {

    private Model_Registration registration;
    private Model_Login logIn;
    private Model_TaskItem task;
    private Model_WorkoutItem workout;
    private Model_StorageItem addToStorage;
    private Model_Favorites addToFavorites;
    private JSONObject jsonObject;
    private Context mContext;
    private String aesKey;

    public PostHelper (Context context){
        mContext = context;
        aesKey = sharedGet();
    }

    @Override
    protected String doInBackground(String... urls) {

        int status = 0;
        if(urls[1].equals("LogIn"))
            status = 1;
        if(urls[1].equals("Registration"))
            status = 2;
        if(urls[1].equals("ChangeProfile"))
            status = 3;
        if(urls[1].equals("Workout"))
            status = 4;
        if(urls[1].equals("AddTask"))
            status = 5;
        if(urls[1].equals("ListOfWorkoutsName"))
            status = 6;
        if(urls[1].equals("ListOfTaskName"))
            status = 7;
        if(urls[1].equals("Task"))
            status = 8;
        if(urls[1].equals("RegistrationKey"))
            status = 9;
        if(urls[1].equals("DeleteWorkout"))
            status = 10;
        if(urls[1].equals("DeleteTask"))
            status = 11;
        if(urls[1].equals("AddToStorage"))
            status = 12;
        if(urls[1].equals("GetStorageWorkoutsList"))
            status = 13;
        if(urls[1].equals("GetStorageTaskList"))
            status = 14;
        if(urls[1].equals("StorageTaskProperty"))
            status = 15;
        if(urls[1].equals("AddWorkoutToFavorites"))
            status = 16;
        if(urls[1].equals("GetFavoritesWorkoutsList"))
            status = 17;
        if(urls[1].equals("DeleteWorkoutFromFavoritesList"))
            status = 18;
        if(urls[1].equals("GetUserProperty"))
            status = 19;

        switch(status){
            case 1:
                setModelLogIn(urls[2], urls[3], urls[4]);
                return POST(urls[0],urls[1]);
            case 2:
                setModelRegistration(urls[2], urls[3], urls[4], urls[5]);
                return POST(urls[0],urls[1]);
            case 3:
                setModelRegistration(urls[2], urls[3], urls[4], urls[5]);
                return POST(urls[0],urls[1]);
            case 4:
                setModelWorkout(urls[2], urls[3]);
                return POST(urls[0],urls[1]);
            case 5:
                setModelTask(urls[2], urls[3], urls[4], urls[5], urls[6]);
                return POST(urls[0],urls[1]);
            case 6:
                setUserName(urls[2]);
                return POST(urls[0],urls[1]);
            case 7:
                setWorkoutName(urls[2]);
                return POST(urls[0],urls[1]);
            case 8:
                setTaskName(urls[2]);
                return POST(urls[0],urls[1]);
            case 9:
                setRsaPublicKey(urls[2]);
                return  POST(urls[0],urls[1]);
            case 10:
                setModelWorkout(urls[2], urls[3]);
                return  POST(urls[0],urls[1]);
            case 11:
                setTaskParameters(urls[2], urls[3]);
                return  POST(urls[0],urls[1]);
            case 12:
                setModelAddToStorage(urls[2], urls[3], true);
                return  POST(urls[0],urls[1]);
            case 13:
                return  POST(urls[0],urls[1]);
            case 14:
                setModelWorkout(urls[2], urls[3]);
                return POST(urls[0],urls[1]);
            case 15:
                setTaskParameters(urls[2], urls[3]);
                return  POST(urls[0],urls[1]);
            case 16:
                setModelFavorites(urls[2], urls[3], urls[4]);
                return  POST(urls[0],urls[1]);
            case 17:
                setUserName(urls[2]);
                return POST(urls[0],urls[1]);
            case 18:
                setModelFavorites(urls[2], urls[3], urls[4]);
                return  POST(urls[0],urls[1]);
            case 19:
                setUserName(urls[2]);
                return POST(urls[0],urls[1]);
            default:
                return null;
        }
    }

    protected void onPreExecute() {

    }


    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

    }


    public String POST(String url, String cs){
        InputStream inputStream ;
        String result = "";
        try {

            // 1. create HttpClient
            //HttpClient httpclient = new DefaultHttpClient();
            HttpClient httpclient = new DefaultHttpClient();
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json;

            // 3. build jsonObject
            jsonObject = new JSONObject();

            int status = 0;
            if(cs.equals("LogIn"))
                status = 1;
            if(cs.equals("Registration"))
                status = 2;
            if(cs.equals("ChangeProfile"))
                status = 3;
            if(cs.equals("Workout"))
                status = 4;
            if(cs.equals("AddTask"))
                status = 5;
            if(cs.equals("ListOfWorkoutsName"))
                status = 6;
            if(cs.equals("ListOfTaskName"))
                status = 7;
            if(cs.equals("Task"))
                status = 8;
            if(cs.equals("RegistrationKey"))
                status = 9;
            if(cs.equals("DeleteWorkout"))
                status = 10;
            if(cs.equals("DeleteTask"))
                status = 11;
            if(cs.equals("AddToStorage"))
                status = 12;
            if(cs.equals("GetStorageTaskList"))
                status = 13;
            if(cs.equals("StorageTaskProperty"))
                status = 14;
            if(cs.equals("AddWorkoutToFavorites"))
                status = 15;
            if(cs.equals("GetFavoritesWorkoutsList"))
                status = 16;
            if(cs.equals("DeleteWorkoutFromFavoritesList"))
                status = 17;
            if(cs.equals("GetUserProperty"))
                status = 18;

            switch(status){
                case 1:
                    setJsonLogIn();
                    break;
                case 2:
                    setJsonRegistration();
                    break;
                case 3:
                    setJsonRegistration();
                    break;
                case 4:
                    setJsonWorkout();
                    break;
                case 5:
                    setJsonTask();
                    break;
                case 6:
                    setAES_JsonLogIn();
                    break;
                case 7:
                    setJsonWorkout();
                    break;
                case 8:
                    setJsonTask();
                    break;
                case 9:
                    setJsonLogIn();
                    break;
                case 10:
                    setJsonWorkout();
                    break;
                case 11:
                    setJsonTask();
                    break;
                case 12:
                    setJsonAddToStorage();
                    break;
                case 13:
                    setJsonWorkout();
                    break;
                case 14:
                    setJsonTask();
                    break;
                case 15:
                    setJsonFavorites();
                    break;
                case 16:
                    setAES_JsonLogIn();
                    break;
                case 17:
                    setJsonFavorites();
                    break;
                case 18:
                    setAES_JsonLogIn();
                default:
                    break;
            }

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private void setRsaPublicKey(String urls1)
    {
        logIn = new Model_Login();
        logIn.setUserName("");
        logIn.setPassword("");
        logIn.setRsaKey(urls1);
    }

    private void setModelLogIn(String urls1, String urls2, String urls3)
    {
        logIn = new Model_Login();
        logIn.setUserName(urls1);
        logIn.setPassword(urls2);
        logIn.setRsaKey(urls3);
    }

    private void setModelAddToStorage(String urls1, String urls2, boolean urls3)
    {
        addToStorage = new Model_StorageItem();
        addToStorage.setUserName(urls1);
        addToStorage.setWorkoutName(urls2);
        addToStorage.setInStorage(urls3);
    }

    private void setModelFavorites(String urls1, String urls2, String urls3)
    {
        addToFavorites = new Model_Favorites();
        addToFavorites.setMasterUserName(urls1);
        addToFavorites.setUserName(urls2);
        addToFavorites.setWorkoutName(urls3);
    }

    private void setModelRegistration(String urls1, String urls2, String urls3, String urls4)
    {
        registration = new Model_Registration();
        registration.setFirstName(urls1);
        registration.setLastName(urls2);
        registration.setUserName(urls3);
        registration.setPassword(urls4);
    }

    private void setModelWorkout(String urls1 , String urls2)
    {
        workout = new Model_WorkoutItem();
        workout.setUserName(urls1);
        workout.setWorkoutName(urls2);
    }

    private void setModelTask(String urls1, String urls2, String urls3, String urls4 ,String urls5)
    {
        task = new Model_TaskItem();
        task.setWorkoutName(urls1);
        task.setTaskName(urls2);
        task.setDescriptionTask(urls3);
        task.setTimeTask(urls4);
        task.setRevTask(urls5);
    }

    private void setUserName(String urls)
    {
        logIn = new Model_Login();
        logIn.setUserName(urls);
        logIn.setPassword("");
        logIn.setRsaKey("");
    }

    private void setWorkoutName(String urls)
    {
        workout = new Model_WorkoutItem();
        workout.setUserName("");
        workout.setWorkoutName(urls);
    }

    private void setTaskName(String urls)
    {
        task = new Model_TaskItem();
        task.setWorkoutName("");
        task.setTaskName(urls);
        task.setDescriptionTask("");
        task.setTimeTask("");
        task.setRevTask("");
    }

    private void setTaskParameters(String urls , String urls1)
    {
        task = new Model_TaskItem();
        task.setWorkoutName(urls);
        task.setTaskName(urls1);
        task.setDescriptionTask("");
        task.setTimeTask("");
        task.setRevTask("");
    }

    private void setJsonLogIn() throws JSONException
    {
        jsonObject.accumulate("userName", logIn.getUserName());
        jsonObject.accumulate("password", logIn.getPassword());
        jsonObject.accumulate("publicKey", logIn.getRsaKey());
    }

    private void setAES_JsonLogIn() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, JSONException {
        jsonObject.accumulate("userName", AES.encrypt(logIn.getUserName(), aesKey));
        jsonObject.accumulate("password", AES.encrypt(logIn.getPassword(), aesKey));
        jsonObject.accumulate("publicKey", AES.encrypt(logIn.getRsaKey(), aesKey));
    }

    private void setJsonRegistration() throws JSONException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        jsonObject.accumulate("firstName",registration.getFirstName());
        jsonObject.accumulate("lastName", registration.getLastName());
        jsonObject.accumulate("userName", registration.getUserName());
        jsonObject.accumulate("password", registration.getPassword());
    }

    private void setJsonWorkout() throws JSONException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        jsonObject.accumulate("userName", AES.encrypt(workout.getUserName(), aesKey));
        jsonObject.accumulate("workoutName", AES.encrypt(workout.getWorkoutName(), aesKey));
    }

    private void setJsonTask() throws JSONException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        jsonObject.accumulate("workoutName", AES.encrypt(task.getWorkoutName(),aesKey));
        jsonObject.accumulate("taskName", AES.encrypt(task.getTaskName(),aesKey));
        jsonObject.accumulate("description", AES.encrypt(task.getDescriptionTask(),aesKey));
        jsonObject.accumulate("time", AES.encrypt(task.getTimeTask(),aesKey));
        jsonObject.accumulate("rev", AES.encrypt(task.getRevTask(),aesKey));
    }

    private void setJsonAddToStorage() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, JSONException {
        jsonObject.accumulate("userName", AES.encrypt(addToStorage.getUserName(), aesKey));
        jsonObject.accumulate("workoutName", AES.encrypt(addToStorage.getWorkoutName(), aesKey));
        jsonObject.accumulate("inStorage", addToStorage.isInStorage());
    }

    private void setJsonFavorites() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, JSONException {
        jsonObject.accumulate("masterUserName", AES.encrypt(addToFavorites.getMasterUserName(), aesKey));
        jsonObject.accumulate("userName", AES.encrypt(addToFavorites.getUserName(), aesKey));
        jsonObject.accumulate("workoutName", AES.encrypt(addToFavorites.getWorkoutName(), aesKey));
    }

    private String sharedGet()
    {
        SharedPreferences editor;
        editor = mContext.getSharedPreferences("shared_Memory", mContext.MODE_PRIVATE);
        return  editor.getString("aesKey", null);
    }
}

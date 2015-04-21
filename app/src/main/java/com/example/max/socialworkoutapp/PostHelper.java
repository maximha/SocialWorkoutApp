package com.example.max.socialworkoutapp;

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

public class PostHelper extends AsyncTask<String, Void, String> {

    private Model_Registration registration;
    private Model_Login logIn;
    private Model_TaskItem task;
    private Model_WorkoutItem workout;
    private JSONObject jsonObject;

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
        if(urls[1].equals("Task"))
            status = 5;

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
            case 5:
                setModelWorkout(urls[2]);
                return POST(urls[0],urls[1]);
            case 4:
                setModelTask(urls[2], urls[3], urls[4], urls[5]);
                return POST(urls[0],urls[1]);
            default:
                return null;
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        //checkPostResult(result);//check result answer
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

            String json = "";

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
            if(cs.equals("Task"))
                status = 5;

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
                case 5:
                    setJsonWorkout();
                    break;
                case 4:
                    setJsonTask();
                    break;
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
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private void setModelLogIn(String urls1, String urls2, String urls3)
    {
        logIn = new Model_Login();
        logIn.setUserName(urls1);
        logIn.setPassword(urls2);
        logIn.setRsaKey(urls3);
    }

    private void setModelRegistration(String urls1, String urls2, String urls3, String urls4)
    {
        registration = new Model_Registration();
        registration.setFirstName(urls1);
        registration.setLastName(urls2);
        registration.setUserName(urls3);
        registration.setPassword(urls4);
    }

    private void setModelWorkout(String urls1)
    {
        workout = new Model_WorkoutItem();
        workout.setWorkoutName(urls1);
    }

    private void setModelTask(String urls1, String urls2, String urls3, String urls4)
    {
        task = new Model_TaskItem();
        task.setTaskName(urls1);
        task.setDescriptionTask(urls2);
        task.setTimeTask(urls3);
        task.setRevTask(urls4);
    }

    private void setJsonLogIn() throws JSONException
    {
        jsonObject.accumulate("userName", logIn.getUserName());
        jsonObject.accumulate("password", logIn.getPassword());
        jsonObject.accumulate("publicKey", logIn.getRsaKey());
    }

    private void setJsonRegistration() throws  JSONException
    {
        jsonObject.accumulate("firstName", registration.getFirstName());
        jsonObject.accumulate("lastName", registration.getLastName());
        jsonObject.accumulate("userName", registration.getUserName());
        jsonObject.accumulate("password", registration.getPassword());
    }

    private void setJsonWorkout() throws JSONException
    {
        jsonObject.accumulate("workoutName", workout.getWorkoutName());
    }

    private void setJsonTask() throws JSONException
    {
        jsonObject.accumulate("taskName", task.getTaskName());
        jsonObject.accumulate("description", task.getDescriptionTask());
        jsonObject.accumulate("time", task.getTimeTask());
        jsonObject.accumulate("rev", task.getRevTask());
    }
}
package com.example.hospital.firstmenu.Server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 2018-05-16.
 */

public class PushFriendData extends AsyncTask<String, Void, String> {
    public static final String TAG = "PushFriendData";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "POST response  - " + result);
    }


    @Override
    protected String doInBackground(String... params) {

        String userid = (String)params[0];
        String friendid = (String)params[1];

        String serverURL = "http://220.67.231.92/~genie/user_friends_register.php?";
        serverURL += "user_id=" + userid + "&user_friends=" + friendid;

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString();

        } catch (Exception e) {
            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }
    }
}
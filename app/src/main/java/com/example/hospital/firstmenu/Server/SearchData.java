package com.example.hospital.firstmenu.Server;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hospital.firstmenu.listView.AddFriendAdapter;
import com.example.hospital.firstmenu.listView.AddFriendItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class SearchData extends AsyncTask<String, Void, String> {
    private static String TAG = "SearchData";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_SORE = "score";
    private static final String TAG_URL = "url";

    String errorString = null;
    private String mJsonString;
    List<AddFriendItem> searchlist;
    AddFriendAdapter adapter;

    public SearchData(List<AddFriendItem> searchlist, AddFriendAdapter adapter) {
        this.searchlist = searchlist;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "response - " + result);

        if (result == null) {
            Log.d(TAG, "response - " + result);

        } else {
            mJsonString = result;
            showResult();
        }
    }


    @Override

    protected String doInBackground(String... params) {

        String searchKeyword = params[0];
        String serverURL = "http://220.67.231.92/~genie/user_users_search.php?";
        serverURL += "user_name=" + searchKeyword;
        //String postParameters = "country=" + searchKeyword;

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            //httpURLConnection.setRequestMethod("GET");
            //httpURLConnection.setDoInput(true);
            //httpURLConnection.connect();


            //OutputStream outputStream = httpURLConnection.getOutputStream();
            //outputStream.write(postParameters.getBytes("UTF-8"));
            //outputStream.flush();
            //outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            return sb.toString().trim();
        } catch (Exception e) {
            Log.d(TAG, "Error ", e);
            errorString = e.toString();
            return null;
        }
    }

    private void showResult(){

        Log.i(TAG, mJsonString);
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Log.i(TAG, "JSONArray="+jsonArray.length());

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Log.i(TAG, item.toString());
                String id = item.getString(TAG_ID);
                String name = item.getString(TAG_NAME);
                String score = item.getString(TAG_SORE);
                String url = item.getString(TAG_URL);

                String str = "<" + id + "," + name + "," + score + "," + url + ">\n";
                Log.i(TAG, str);

                searchlist.add(new AddFriendItem(name, Uri.parse(url), id));
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {

            Log.d(TAG, "showResult Error : ", e);

        }
    }
}

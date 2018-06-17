package com.example.hospital.firstmenu.Server;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.hospital.firstmenu.listView.FriendAdapter;
import com.example.hospital.firstmenu.listView.FriendItem;
import com.example.hospital.firstmenu.listView.RecyclerAdapter;
import com.example.hospital.firstmenu.listView.RecyclerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Admin on 2018-05-20.
 */

public class GetFriendData extends AsyncTask<String, Void, String> {
    public String TAG = "GetFriendData";
    public String JSON_TAG = "webnautes";

    String errorString = null;
    String mJsonString;
    ArrayList<FriendItem> mItems = null;
    List<FriendItem> searchList = null;
    FriendAdapter adapter;

    public GetFriendData(ArrayList<FriendItem> mItems, List<FriendItem> searchList, FriendAdapter adapter) {
        this.mItems = mItems;
        this.searchList = searchList;
        this.adapter = adapter;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "onPostExecute response  - " + result);

        if (result == null) {
            Log.d(TAG, "onPostExecute result is null");
        } else {
            mJsonString = result;
            Log.i(TAG, "onPostExecute result" + result.toString());
            showResult();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String serverURL = params[0];

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "doInBackground response code - " + responseStatusCode);

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

            Log.d(TAG, "doInBackground InsertData: Error ", e);
            errorString = e.toString();

            return null;
        }

    }

    private void showResult() {
        try {
            List<FriendItem> recyclerItems = new ArrayList<FriendItem>();
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_TAG);
            // result를 잘 변환해서 출력
            Log.i(TAG, "showResult : " + jsonArray.toString());

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("id");
                String name = object.getString("name");
                int score = object.getInt("score");
                String url = object.getString("url");

                recyclerItems.add(new FriendItem(Uri.parse(url), name, score, id));
                Log.i("List count ", Integer.toString(recyclerItems.size()));
            }

            mItems.clear();
            mItems.addAll(recyclerItems);
            Collections.sort(mItems);

            searchList.clear();
            searchList.addAll(mItems);
            adapter.notifyDataSetChanged();
            Log.i(TAG, "showResult mList : " + mItems.size());

            // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
}

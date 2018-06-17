package com.example.hospital.firstmenu;

import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TabHost;

import com.example.hospital.firstmenu.Server.GetData;
import com.example.hospital.firstmenu.listView.RecyclerAdapter;
import com.example.hospital.firstmenu.listView.RecyclerItem;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {
    public static String TAG = "RankingActivity";

    RecyclerView recyclerView1, recyclerView2;
    RecyclerAdapter adapter1, adapter2;
    RecyclerView.LayoutManager layoutManager1, layoutManager2;

    private ArrayList<RecyclerItem> mItems = new ArrayList<>();
    private ArrayList<RecyclerItem> mFriendItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Game TabSpec");
        tabSpec1.setContent(R.id.content1);
        tabSpec1.setIndicator("Game Ranking");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Friend TabSpec");
        tabSpec2.setContent(R.id.content2);
        tabSpec2.setIndicator("Friend Ranking");
        tabHost.addTab(tabSpec2);

        setGameRanking();
        setFreindRanking();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goToMainActivity();
    }

    private void setGameRanking() {
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
        // recyclerView의 사이즈를 고정. 일단은 true로
        recyclerView1.setHasFixedSize(true);
        // 가로 또는 세로 스크롤 목록 형식
        layoutManager1 = new LinearLayoutManager(this);
        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
        recyclerView1.setLayoutManager(layoutManager1);

        adapter1 = new RecyclerAdapter(mItems);
        recyclerView1.setAdapter(adapter1);

        setGameData();
        adapter1.notifyDataSetChanged();
    }

    private void setFreindRanking() {
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter2 = new RecyclerAdapter(mFriendItems);
        recyclerView2.setAdapter(adapter2);

        setFriendData();
        adapter2.notifyDataSetChanged();
    }

    public void setGameData() {
        mItems.clear();
        GetData task = new GetData(mItems, adapter1);
        task.execute("http://220.67.231.92/~genie/user_users.php");
        Log.i(TAG, "setGameData " + mItems.size());
    }

    public void setFriendData() {
        mFriendItems.clear();
        GetData task = new GetData(mFriendItems, adapter2);
        String id = Long.toString(UserProfile.loadFromCache().getId());
        task.execute("http://220.67.231.92/~genie/user_friends.php?user_id=" +id);
        Log.i(TAG, "setFriendData " + mFriendItems.size());
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        startActivity(intent);
    }
}

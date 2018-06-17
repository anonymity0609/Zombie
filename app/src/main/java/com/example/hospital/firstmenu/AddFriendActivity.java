package com.example.hospital.firstmenu;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hospital.firstmenu.Server.SearchData;
import com.example.hospital.firstmenu.listView.AddFriendAdapter;
import com.example.hospital.firstmenu.listView.AddFriendItem;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AddFriendActivity extends AppCompatActivity{
    private final String TAG = "AddFriendActivity";
    ListView listView;
    AddFriendAdapter adapter;
    private EditText editSearch;
    private TextInputLayout inputlayout;
    private Button searchBtn;
    private List<AddFriendItem> searchlist = new ArrayList<>();
    private ArrayList<String> friendIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Intent intent = getIntent();
        friendIDList = intent.getStringArrayListExtra("friends_id");
        Log.i(TAG, "send friendList : " + friendIDList.size());
        setView();
    }

    private void setView() {
        listView = (ListView) findViewById(R.id.addFriendView);

        searchBtn = (Button) findViewById(R.id.searchAddFriend);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        inputlayout = (TextInputLayout) findViewById(R.id.textinputlayout);
        inputlayout.setHintEnabled(true);
        inputlayout.setHint("특수문자는 입력할 수 없습니다");
        editSearch = (EditText) findViewById(R.id.addFriendEditText);
        editSearch.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        }});
        adapter = new AddFriendAdapter(searchlist, friendIDList);
        listView.setAdapter(adapter);
    }

    private void search() {
        searchlist.clear();
        // 서버에 검색어 전달 후 결과 출력
        String searchName = editSearch.getText().toString();
        Log.i(TAG, "Search Name ; " + searchName);

        SearchData task = new SearchData(searchlist, adapter);
        task.execute(searchName);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

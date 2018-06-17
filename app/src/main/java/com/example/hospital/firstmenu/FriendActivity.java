package com.example.hospital.firstmenu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hospital.firstmenu.Server.DeleteFriendData;
import com.example.hospital.firstmenu.Server.GetData;
import com.example.hospital.firstmenu.Server.GetFriendData;
import com.example.hospital.firstmenu.listView.*;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FriendActivity extends AppCompatActivity {
    private final String TAG = "FriendActivity";
    ListView listView;
    FriendAdapter adapter;
    private EditText editSearch;
    private Button addBtn;
    private Button removeBtn;
    private Button selectAllBtn;
    private Button cancelAllBtn;
    private ArrayList<FriendItem> mItems = new ArrayList<>();   // 실제 데이터 친구 목록
    private List<FriendItem> searchlist = new ArrayList<>();    // 화면에 보이는 목록. 검색 결과.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setView();

        editSearch = (EditText) findViewById(R.id.searchEditText);
        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });
        setFriendList();

        addBtn = (Button) findViewById(R.id.addFriend);
        removeBtn = (Button) findViewById(R.id.removeFriend);
        selectAllBtn = (Button) findViewById(R.id.selectAll);
        cancelAllBtn = (Button) findViewById(R.id.cancelAll);
        setBtnClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFriendList();
    }

    private void setView() {
        listView = (ListView) findViewById(R.id.friendView);
        adapter = new FriendAdapter(searchlist);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        };
        listView.setOnItemClickListener(itemClickListener);
    }

    // 친구 목록을 서버에서 받아옴
    private void setFriendList() {
        GetFriendData task = new GetFriendData(mItems, searchlist, adapter);
        String id = Long.toString(UserProfile.loadFromCache().getId());
        task.execute("http://220.67.231.92/~genie/user_friends.php?user_id=" +id);
        Log.i(TAG, "mItmes " + mItems.size());
        Log.i(TAG, "searchList" + searchlist.size());

        adapter.notifyDataSetChanged();
    }

    private void setBtnClick() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItem();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeItem();
            }
        });

        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectItemAll();
            }
        });

        cancelAllBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancaelItemAll();
            }
        });
    }

    private void cloneItems() {
        searchlist.clear();
        searchlist.addAll(mItems);
        adapter.notifyDataSetChanged();
    }

    private void cancaelItemAll() {
        int count = 0;
        count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            listView.setItemChecked(i, false);
        }
    }

    private void selectItemAll() {
        int count = 0;
        count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            listView.setItemChecked(i, true);
        }
    }

    private void removeItem() {
        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        int count = adapter.getCount();
        for (int i = count - 1; i >= 0; i--) {
            if (checkedItems.get(i)) {
                // 서버에서 친구 삭제
                DeleteFriendData task = new DeleteFriendData();
                String user_id = Long.toString(UserProfile.loadFromCache().getId());
                String friend_id = mItems.get(i).getId();
                task.execute(user_id, friend_id);

                // 목록에서 친구 삭제
                mItems.remove(i);
            }
        } // 모든 선택 상태 초기화.
        listView.clearChoices();
        cloneItems();
    }

    private void addItem() {
        // 게임 유저를 검색해서 친구 추가
        Intent intent = new Intent(this, AddFriendActivity.class);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (FriendItem item : mItems) {
            stringArrayList.add(item.getId());
        }
        intent.putStringArrayListExtra("friends_id", stringArrayList);
        Log.i(TAG, "send friendList : " + stringArrayList.size());
        overridePendingTransition(R.anim.anim_slide_in_left, 0);
        startActivity(intent);
    }

    private void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        searchlist.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            searchlist.addAll(mItems);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < mItems.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (mItems.get(i).getName().toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    searchlist.add(mItems.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        finish();
    }
}

package com.example.hospital.firstmenu.listView;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hospital.firstmenu.R;
import com.example.hospital.firstmenu.Server.PushData;
import com.example.hospital.firstmenu.Server.PushFriendData;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 2018-05-22.
 */

public class AddFriendAdapter extends BaseAdapter {
    private static final String TAG = "AddFriendAdapter";
    List<AddFriendItem> resultList;
    ArrayList<String> friendIDList;

    public AddFriendAdapter(List<AddFriendItem> resultList, ArrayList<String> friendIDList) {
        this.resultList = resultList;
        this.friendIDList = friendIDList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        final String user_id = Long.toString(UserProfile.loadFromCache().getId());

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.addfriend_view, parent, false);
        }
        CircleImageView friendIamge = (CircleImageView) convertView.findViewById(R.id.addfriend_profile);
        TextView friendName = (TextView) convertView.findViewById(R.id.addfriend_name);
        final Button addBtn = (Button) convertView.findViewById(R.id.addFriend_button);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        AddFriendItem addFriendItem = resultList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(addFriendItem.getImage()).into(friendIamge);
        friendName.setText(addFriendItem.getName());

        final String name = friendName.getText().toString();
        final String friend_id = addFriendItem.getId();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick : " + name);
                setButtonEnableFalse(addBtn);
                // 서버에 친구 추가 전달
                PushFriendData task = new PushFriendData();
                task.execute(user_id, friend_id);
            }
        });

        // 이미 등록되어 있는 친구는 친구 추가 버튼 비활성화
        for (int i=0; i<friendIDList.size(); i++) {
            if (addFriendItem.getId().equals(friendIDList.get(i))) {
                setButtonEnableFalse(addBtn);
            }
        }

        // 내 자신이 검색되어도 친구 추가 버튼 비활성화
        if (addFriendItem.getId().equals(user_id))
            setButtonEnableFalse(addBtn);

        return convertView;
    }

    public void setButtonEnableFalse(Button button) {
        Log.i(TAG, "setButtonEnableFalse called");
        button.setBackgroundResource(R.drawable.blackbox);
        button.setTextColor(Color.WHITE);
        button.setClickable(false);
        button.setEnabled(false);
    }
}

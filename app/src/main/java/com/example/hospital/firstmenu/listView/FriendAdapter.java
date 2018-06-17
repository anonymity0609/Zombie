package com.example.hospital.firstmenu.listView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hospital.firstmenu.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 2018-05-20.
 */

public class FriendAdapter extends BaseAdapter {
    List<FriendItem> searchlist;

    public FriendAdapter(List<FriendItem> searchlist) {
        this.searchlist = searchlist;
    }

    @Override
    public int getCount() {
        return searchlist.size();
    }

    @Override
    public Object getItem(int position) {
        return searchlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.frienditem_recycler_view, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        CircleImageView mIamge = (CircleImageView) convertView.findViewById(R.id.friend_profile);
        TextView mName = (TextView) convertView.findViewById(R.id.friend_name);
        TextView mScore = (TextView) convertView.findViewById(R.id.friend_score);
        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FriendItem friendItem = searchlist.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(context).load(friendItem.getImage()).into(mIamge);
        mName.setText(friendItem.getName());
        mScore.setText(Integer.toString(friendItem.getScore()));
        checkbox.setChecked(((ListView) parent).isItemChecked(position));
        checkbox.setFocusable(false);
        checkbox.setClickable(false);

        return convertView;
    }
}

package com.example.hospital.firstmenu.listView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hospital.firstmenu.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 2018-04-13.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    ArrayList<RecyclerItem> mItems;

    public RecyclerAdapter(ArrayList<RecyclerItem> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.ItemViewHolder holder, int position) {
        RecyclerItem item = mItems.get(position);

        // int값은 int라서 string으로 변환이 필요함
        String temp = String.valueOf(item.getRank());
        holder.mRank.setText(temp);

        // 프로필 사진
        Glide.with(holder.mIamge).load(item.getImage()).into(holder.mIamge);

        holder.mName.setText(item.getName());

        // int값은 int라서 string으로 변환이 필요함22
        temp = String.valueOf(item.getScore());
        holder.mScore.setText(temp);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mRank;
        private CircleImageView mIamge;
        private TextView mName;
        private TextView mScore;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mRank = (TextView) itemView.findViewById(R.id.user_rank);
            mIamge = (CircleImageView) itemView.findViewById(R.id.user_profile);
            mName = (TextView) itemView.findViewById(R.id.user_name);
            mScore = (TextView) itemView.findViewById(R.id.user_score);

        }
    }
}

package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.RecentSearch;

import java.util.List;

/**
 * Created by csangharsha on 5/18/18.
 */

public class RecentSearchListAdapter extends RecyclerView.Adapter<RecentSearchListAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecentSearch> recentSearchList;

    public RecentSearchListAdapter(Context mContext, List<RecentSearch> recentSearchList) {
        this.mContext = mContext;
        this.recentSearchList = recentSearchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_search_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecentSearch task = recentSearchList.get(position);
        holder.keywordTextView.setText(task.getKeyword());
    }

    @Override
    public int getItemCount() {
        return recentSearchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView keywordTextView;

        public MyViewHolder(View view) {
            super(view);
            keywordTextView = view.findViewById(R.id.keyword_text_word);
        }
    }

}

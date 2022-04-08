package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.group_by_pos.DataItem;
import com.codebee.tradethrust.view.interfaces.OnGroupPosClickedListener;

import java.util.List;

public class GroupByPOSListAdapter extends RecyclerView.Adapter<GroupByPOSListAdapter.MyViewHolder> {

    private Context mContext;
    private List<DataItem> posList;
    private OnGroupPosClickedListener onGroupPosClickedListener;

    public GroupByPOSListAdapter(Context mContext, List<DataItem> posList, OnGroupPosClickedListener onGroupPosClickedListener) {
        this.mContext = mContext;
        this.posList = posList;
        this.onGroupPosClickedListener = onGroupPosClickedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pos_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DataItem dataItem = posList.get(position);
        holder.title.setText(dataItem.getName());

        holder.taskCountTextView.setBackgroundResource(R.drawable.text_count_text_view_drawable);
        holder.taskCountTextView.setTextColor(mContext.getResources().getColor(R.color.new_status_text_color));
        holder.taskCountTextView.setText("Task Count: " + dataItem.getTaskCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGroupPosClickedListener.onGroupPOSClicked(dataItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView taskCountTextView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            taskCountTextView = view.findViewById(R.id.task_count_text_view);
        }
    }

}

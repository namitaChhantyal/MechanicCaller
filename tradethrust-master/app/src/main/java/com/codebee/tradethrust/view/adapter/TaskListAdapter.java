package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.task.list.Datum;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.interfaces.OnTaskClickedListener;

import java.util.List;

/**
 * Created by csangharsha on 5/17/18.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Datum> taskList;
    private OnTaskClickedListener onTaskClickedListener;

    public TaskListAdapter(Context mContext, List<Datum> taskList, OnTaskClickedListener onTaskClickedListener) {
        this.mContext = mContext;
        this.taskList = taskList;
        this.onTaskClickedListener = onTaskClickedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Datum task = taskList.get(position);
        holder.title.setText(task.getTitle());

        if(task.getBit() != null && task.getBit().getName() != null) {
            holder.bitNameTextView.setText(task.getBit().getName());
        }else {
            holder.bitNameTextView.setText("-");
        }

        if(task.getPos() != null && task.getPos().getName() != null) {
            holder.posNameTextView.setText(task.getPos().getName());
        }else {
            holder.posNameTextView.setText("-");
        }

        setTaskStatusLayout(holder.status, task.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTaskClickedListener.onTaskClicked(task);
            }
        });
    }

    private void setTaskStatusLayout(TextView status, String status1) {

        status.setText(status1);

        if(status1.equalsIgnoreCase(ThrustConstant.TASK_STATUS_NEW)) {
            status.setBackgroundResource(R.drawable.new_status_drawable);
            status.setTextColor(mContext.getResources().getColor(R.color.new_status_text_color));
        }else if(status1.equalsIgnoreCase(ThrustConstant.TASK_STATUS_IN_PROGRESS)){
            status.setBackgroundResource(R.drawable.in_progress_status_drawable);
            status.setTextColor(mContext.getResources().getColor(R.color.in_progress_status_text_color));
        }else if(status1.equalsIgnoreCase(ThrustConstant.TASK_STATUS_COMPLETED)){
            status.setBackgroundResource(R.drawable.completed_status_drawable);
            status.setTextColor(mContext.getResources().getColor(R.color.completed_status_text_color));
        }else {
            status.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView status;
        public TextView bitNameTextView;
        public TextView posNameTextView;


        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            status = view.findViewById(R.id.status);
            bitNameTextView = view.findViewById(R.id.bit_name_text_view);
            posNameTextView = view.findViewById(R.id.pos_name_text_view);
        }
    }

}

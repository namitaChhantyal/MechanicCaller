package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.task.details.Bit;
import com.codebee.tradethrust.view.interfaces.OnGroupBitClickedListener;

import java.util.List;

public class GroupByBitListAdapter extends RecyclerView.Adapter<GroupByBitListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Bit> bitList;
    private OnGroupBitClickedListener onGroupBitClickedListener;

    public GroupByBitListAdapter(Context mContext, List<Bit> bitList, OnGroupBitClickedListener onGroupBitClickedListener) {
        this.mContext = mContext;
        this.bitList = bitList;
        this.onGroupBitClickedListener = onGroupBitClickedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bit_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Bit bit = bitList.get(position);
        holder.title.setText(bit.getName());

        holder.posCountTextView.setBackgroundResource(R.drawable.text_count_text_view_drawable);
        holder.posCountTextView.setTextColor(mContext.getResources().getColor(R.color.new_status_text_color));
        holder.posCountTextView.setText("POS Count: " + bit.getPosProfileCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGroupBitClickedListener.onGroupBitClicked(bit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bitList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView posCountTextView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            posCountTextView = view.findViewById(R.id.pos_count_text_view);
        }
    }
}

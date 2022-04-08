package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.notification_details.DataItem;

import org.joda.time.Instant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {

    public static DateFormat dateformat = new SimpleDateFormat("MMMM dd, yyyy HH:mm");

    private Context mContext;
    private List<DataItem> dataItemList = new ArrayList<>();

    public NotificationListAdapter(Context mContext, List<DataItem> dataItemList) {
        this.mContext = mContext;
        this.dataItemList = dataItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataItem dataItem = dataItemList.get(position);
        holder.messageTextView.setText(dataItem.getMessage());
        holder.createdDateTextView.setText(getTimeInAgoFormat(dataItem.getCreatedAt()));
//        holder.createdDateTextView.setText(dataItem.getCreatedAt());

        if(!dataItem.isRead()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    private String getTimeInAgoFormat(String createdDate) {
        Instant instant = Instant.parse(createdDate);
        Date past = instant.toDate();
        Date now = new Date();
        long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

        if(seconds<60)
        {
            return seconds + " seconds ago" ;
        }
        else if(minutes<60)
        {
            return minutes+" minutes ago";
        }
        else if(hours<24)
        {
            return hours+" hours ago";
        }
        else
        {
            return dateformat.format(past);
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView createdDateTextView;

        public MyViewHolder(View view) {
            super(view);
            messageTextView = view.findViewById(R.id.message_text_view);
            createdDateTextView = view.findViewById(R.id.created_date_text_view);
        }
    }
}

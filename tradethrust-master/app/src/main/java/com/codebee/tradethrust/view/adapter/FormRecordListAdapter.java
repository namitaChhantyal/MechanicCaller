package com.codebee.tradethrust.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.records_details.DataItem;
import com.codebee.tradethrust.view.interfaces.OnFormRecordClickedListener;

import org.joda.time.Instant;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FormRecordListAdapter extends RecyclerView.Adapter<FormRecordListAdapter.MyViewHolder> {

    public static DateFormat dateformat = new SimpleDateFormat("MMMM dd, yyyy HH:mm");

    private Context mContext;
    private List<DataItem> dataItemList;
    private OnFormRecordClickedListener onFormRecordClickedListener;

    public FormRecordListAdapter(Context mContext, List<DataItem> dataItemList, OnFormRecordClickedListener onFormRecordClickedListener) {
        this.mContext = mContext;
        this.dataItemList = dataItemList;
        this.onFormRecordClickedListener = onFormRecordClickedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.form_record_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DataItem dataItem = dataItemList.get(position);
        holder.serialNumberTextView.setText(String.valueOf(position + 1));
        holder.posNameTextView.setText(dataItem.getData().get("pos_name"));

        Instant instant = Instant.parse(dataItem.getData().get("created_at"));
        Date date = instant.toDate();
        holder.createdDateTextView.setText(dateformat.format(date));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFormRecordClickedListener.onFormRecordClickedListener(dataItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView serialNumberTextView;
        public TextView posNameTextView;
        public TextView createdDateTextView;

        public MyViewHolder(View view) {
            super(view);
            serialNumberTextView = view.findViewById(R.id.serial_number_text_view);
            posNameTextView = view.findViewById(R.id.pos_name_text_view);
            createdDateTextView = view.findViewById(R.id.created_date_text_view);
        }
    }
}

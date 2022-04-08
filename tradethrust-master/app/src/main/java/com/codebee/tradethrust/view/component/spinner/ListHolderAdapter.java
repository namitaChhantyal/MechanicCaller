package com.codebee.tradethrust.view.component.spinner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.province.ListHolder;

import java.util.ArrayList;
import java.util.List;

public class ListHolderAdapter extends RecyclerView.Adapter<ListHolderAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<ListHolder> provinceList;
    private List<ListHolder> provinceListFiltered;
    private OnProvincesAdapterListener onProvincesAdapterListener;


    public ListHolderAdapter(Context context, List<ListHolder> provinceList, OnProvincesAdapterListener onProvincesAdapterListener) {
        this.context = context;
        this.provinceList = provinceList;
        this.provinceListFiltered = provinceList;
        this.onProvincesAdapterListener = onProvincesAdapterListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.spinner_row_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ListHolder holder = provinceListFiltered.get(i);
        myViewHolder.textView.setText(holder.getLabel());
    }

    @Override
    public int getItemCount() {
        return provinceListFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    provinceListFiltered = provinceList;
                } else {
                    List<ListHolder> filteredList = new ArrayList<>();
                    for (ListHolder row : provinceList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLabel().toLowerCase().contains(charString.toLowerCase()) || row.getLabel().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    provinceListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = provinceListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                provinceListFiltered = (ArrayList<ListHolder>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onProvincesAdapterListener.onItemSelected(provinceListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnProvincesAdapterListener {
        void onItemSelected(ListHolder contact);
    }
}

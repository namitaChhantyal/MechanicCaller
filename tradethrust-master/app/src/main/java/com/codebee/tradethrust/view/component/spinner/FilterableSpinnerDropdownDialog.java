package com.codebee.tradethrust.view.component.spinner;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.province.ListHolder;

import java.util.List;

public class FilterableSpinnerDropdownDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener, ListHolderAdapter.OnProvincesAdapterListener {

    public final static String _LIST_KEY = "list";

    private SearchView _searchView;

    private RecyclerView recyclerView;

    private OnProvinceItemSelectedListener onProvinceItemSelectedListener;

    private List<ListHolder> list;
    private ListHolderAdapter mAdapter;

    public FilterableSpinnerDropdownDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View rootView = inflater.inflate(R.layout.filterable_spinner_dropdown_dialog, null);

        list = (List<ListHolder>) getArguments().get(_LIST_KEY);

        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setView(rootView);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    public void setOnProvinceItemSelectedListener(OnProvinceItemSelectedListener onProvinceItemSelectedListener) {
        this.onProvinceItemSelectedListener = onProvinceItemSelectedListener;
    }

    public void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        _searchView = (SearchView) rootView.findViewById(R.id.search_view);
        _searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        _searchView.setIconifiedByDefault(false);
        _searchView.setOnQueryTextListener(this);
        _searchView.setOnCloseListener(this);
        _searchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);

        mAdapter = new ListHolderAdapter(getContext(), list, this);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, (int) (0 * getContext().getResources().getDisplayMetrics().density)));
        recyclerView.setAdapter(mAdapter);

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    public void notifyDataSetChanged(List<ListHolder> listHolders) {
        list.clear();
        list.addAll(listHolders);
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onItemSelected(ListHolder listHolder) {
        onProvinceItemSelectedListener.onProvinceItemSelected(listHolder);
        this.dismiss();
    }

    public interface OnProvinceItemSelectedListener {
        void onProvinceItemSelected(ListHolder listHolder);
    }
}

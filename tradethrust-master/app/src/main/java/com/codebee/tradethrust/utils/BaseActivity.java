package com.codebee.tradethrust.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codebee.tradethrust.R;

/**
 * Created by csangharsha on 5/13/18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected  AlertDialog b;
    protected AlertDialog.Builder dialogBuilder;
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        if(!showActionBar()) {
            actionBar.hide();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mErrorString = new SparseIntArray();

        setContentView(getContentView());

        onViewReady(savedInstanceState, getIntent());
    }

    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        // Implemented by Child Class
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean showActionBar() {
        return false;
    }

    public void showProgressDialog(String message) {
        dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null);
        if(message != null) {
            ((TextView) (dialogView.findViewById(R.id.message))).setText(message);
        } else {
             dialogView.findViewById(R.id.message).setVisibility(View.GONE);
        }
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
    }

    public void hideProgressDialog(){
        b.dismiss();
    }

    protected abstract int getContentView();

}

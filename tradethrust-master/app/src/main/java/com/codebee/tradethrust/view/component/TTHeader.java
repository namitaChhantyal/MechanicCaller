package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;

/**
 * Created by csangharsha on 6/11/18.
 */

public class TTHeader extends LinearLayout {

    private Context context;

    private TextView headerTextView;

    private float mDensity;

    public TTHeader(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);
        setHeaderLayout();

    }

    private void setHeaderLayout() {
        headerTextView = new TextView(context);
        LinearLayout.LayoutParams headerTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        headerTextView.setLayoutParams(headerTextViewLayoutParams);
        headerTextView.setTextColor(Color.BLACK);
        this.addView(headerTextView);
    }

    public void setSchema(Schema schema) {
        String subtype = schema.getSubtype()!=null ? schema.getSubtype() : "h1";
        String textToShow = "<" + subtype + ">" + schema.getLabel() + "</" + subtype + ">";
        headerTextView.setText(Html.fromHtml(textToShow));
    }

    public void setId(int id) {
        headerTextView.setId(id);
    }

    public void setText(String value) {
        headerTextView.setText(value);
    }
}
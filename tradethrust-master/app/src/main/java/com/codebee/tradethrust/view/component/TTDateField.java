package com.codebee.tradethrust.view.component;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Exchanger;

/**
 * Created by csangharsha on 6/7/18.
 */

public class TTDateField extends LinearLayout {

    private Context context;

    private TextView dateFieldTextView;

    private float mDensity;
    private SimpleDateFormat simpleDateFormat;

    public TTDateField(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (16 * mDensity);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        setDateFieldLayout();

    }

    private void setDateFieldLayout() {
        LinearLayout dateFieldViewMainContainer = new LinearLayout(context);
        LinearLayout.LayoutParams dateFieldMainContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateFieldMainContainerParams.bottomMargin = (int) (16 * mDensity);
        dateFieldViewMainContainer.setLayoutParams(dateFieldMainContainerParams);
        dateFieldViewMainContainer.setOrientation(LinearLayout.VERTICAL);
        dateFieldViewMainContainer.setPadding((int) (2 * mDensity), 0 , (int) (2 * mDensity), 0);

        LinearLayout dateFieldViewSubContainer = new LinearLayout(context);
        dateFieldViewSubContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dateFieldViewSubContainer.setOrientation(LinearLayout.HORIZONTAL);

        dateFieldTextView = new TextView(context);
        LinearLayout.LayoutParams dateFieldTextViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateFieldTextViewLayoutParams.weight=1;
        dateFieldTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        dateFieldTextView.setLayoutParams(dateFieldTextViewLayoutParams);

        dateFieldTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.component_label_size));

        dateFieldViewSubContainer.addView(dateFieldTextView);

        ImageView calenderImageView = new ImageView(context);
        LinearLayout.LayoutParams calenderViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        calenderViewParams.gravity = Gravity.RIGHT;
        calenderImageView.setLayoutParams(calenderViewParams);
        calenderImageView.setImageResource(R.drawable.ic_calender);

        dateFieldViewSubContainer.addView(calenderImageView);

        dateFieldViewMainContainer.addView(dateFieldViewSubContainer);

        View view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.view_color));
        LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (context.getResources().getDimension(R.dimen.view_size)));
        view.setLayoutParams(viewLayoutParams);

        dateFieldViewMainContainer.addView(view);
        this.addView(dateFieldViewMainContainer);

        dateFieldViewMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                Calendar c = null;
                if(dateFieldTextView.getText().toString() == null) {
                    c = Calendar.getInstance();
                }else {
                    c = Calendar.getInstance();
                    try {
                        c.setTime(simpleDateFormat.parse(dateFieldTextView.getText().toString()));
                    }catch (Exception e){

                    }
                }
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar cal = Calendar.getInstance();
                                cal.set(year, monthOfYear, dayOfMonth);

                                simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                dateFieldTextView.setText( simpleDateFormat.format(cal.getTime()));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public void setSchema(Schema schema) {

        dateFieldTextView.setHint(schema.getLabel());
        dateFieldTextView.setTag(schema.getName());
    }

    public void setId(int id) {
        dateFieldTextView.setId(id);
    }


    public void setDate(String value) {
        dateFieldTextView.setText( value );
    }
}

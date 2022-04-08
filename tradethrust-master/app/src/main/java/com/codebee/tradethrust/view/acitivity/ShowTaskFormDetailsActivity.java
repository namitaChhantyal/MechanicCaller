package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.task.details.Data;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.ComponentGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by csangharsha on 6/17/18.
 */
public class ShowTaskFormDetailsActivity extends BaseActivity {

    @BindView(R.id.main_container)
    public LinearLayout mainContainer;

    @BindView(R.id.new_revisit_form_btn)
    public Button newRevisitFormBtn;

    @BindView(R.id.next_form_btn)
    public Button nextFormBtn;

    private LinearLayout imageLayoutContainer;

    private Map<String, Object> componentValue;

    private List<Schema> schemaList;
    private float mDensity;
    private Long formId;
    private int taskId;
    private double markerLat;
    private double markerLong;
    private Picasso picasso;
    private int posId;
    private ArrayList<Data> formList;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

        ButterKnife.bind(this);

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();
        picasso = application.getPicasso();

        mDensity = getResources().getDisplayMetrics().density;

        componentValue = (HashMap<String, Object>) intent.getSerializableExtra("formValueMap");
        schemaList = (ArrayList<Schema>) intent.getSerializableExtra("schemaList");

        formId = intent.getLongExtra("formId", 0L);
        taskId = intent.getIntExtra("taskId", 0);

        if (intent.hasExtra("markerLong")) {
            markerLong = intent.getDoubleExtra("markerLong", 0);
        }

        if (intent.hasExtra("posId")) {
            posId = intent.getIntExtra("posId", -1);
        }

        if(intent.hasExtra("markerLat")) {
            markerLat = intent.getDoubleExtra("markerLat", 0);
        }

        if(intent.hasExtra("formType")) {
            String formType = intent.getStringExtra("formType");
            if(formType != null && formType.equals(ThrustConstant.FORM_TYPE_REVISIT_FORM)) {
                newRevisitFormBtn.setVisibility(View.VISIBLE);
            }else {
                newRevisitFormBtn.setVisibility(View.GONE);
            }

        }
        if(intent.hasExtra("formList")) {
            formList = (ArrayList<Data>) intent.getSerializableExtra("formList");
        }

        if(formList == null || formList != null && formList.size() < 2) {
            nextFormBtn.setVisibility(View.GONE);
        }

        newRevisitFormBtn.setVisibility(View.GONE);

        initializePhotoLayout();

        setUpView();

    }

    private void initializePhotoLayout() {
        imageLayoutContainer = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = (int) (10 * mDensity);
        imageLayoutContainer.setLayoutParams(layoutParams);
        imageLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        imageLayoutContainer.setBackgroundColor(getResources().getColor(R.color.black));

        imageLayoutContainer.setVisibility(View.GONE);

        mainContainer.addView(imageLayoutContainer);
    }

    private void setUpView() {
        for(int i = 0; i < schemaList.size(); i++) {
            Schema schema = schemaList.get(i);

            if(!schema.getType().equals("hidden")) {
                if(schema.getType().equals("file")) {
                    setUpFileLayoutView(schema);
                } else {
                    setUpLayoutView(schema);
                }
            }

        }
    }

    private void setUpFileLayoutView(final Schema schema) {
        if(componentValue.containsKey(schema.getName())) {

            imageLayoutContainer.setVisibility(View.VISIBLE);

            TextView titleTextView = new TextView(this);
            LinearLayout.LayoutParams titleTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleTextView.setLayoutParams(titleTextViewParams);
            titleTextView.setPadding((int) (5 * mDensity), (int) (5 * mDensity), (int) (5 * mDensity), (int) (3 * mDensity));
            titleTextView.setText(schema.getLabel());
            titleTextView.setTextSize(6 * mDensity);
            titleTextView.setVisibility(View.GONE);

            final ImageView valueTextView = new ImageView(this);
            LinearLayout.LayoutParams valueTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (250 * mDensity));
            valueTextView.setLayoutParams(valueTextViewParams);
            valueTextView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            File imageFile = new File((String) componentValue.get(schema.getName()));

            if(imageFile.exists()) {
                picasso.load(imageFile)
                        .into(valueTextView);
            }else {
                String imageUrl = (String) componentValue.get(schema.getName());
                if(!((String)componentValue.get(schema.getName())).startsWith("http")) {
                    imageUrl = "http://" + imageUrl;
                }
                picasso.load(imageUrl)
                        .into(valueTextView);
            }

            valueTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowTaskFormDetailsActivity.this, ViewImageActivity.class);
                    intent.putExtra("path", (String) componentValue.get(schema.getName()));
                    ShowTaskFormDetailsActivity.this.startActivity(intent);
                }
            });

            imageLayoutContainer.addView(titleTextView);
            imageLayoutContainer.addView(valueTextView);

        }
    }

    private void setUpLayoutView(Schema schema) {

        if(componentValue.containsKey(schema.getName())) {
            if(schema.getType().equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_CLONEABLE)) {

                TextView titleTextView = new TextView(this);
                LinearLayout.LayoutParams titleTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                titleTextView.setLayoutParams(titleTextViewParams);
                titleTextView.setPadding((int)(5 * mDensity), (int)(5 * mDensity), (int)(5 * mDensity), (int)(5 * mDensity));
                titleTextView.setText(schema.getLabel());
                titleTextView.setTextColor(getResources().getColor(R.color.black));
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.show_component_label_size));

                mainContainer.addView(titleTextView);

                LinearLayout linearLayout = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                layoutParams.topMargin = (int) (10 * mDensity);
                layoutParams.leftMargin = (int) (10 * mDensity);
                layoutParams.bottomMargin = (int) (10 * mDensity);
                layoutParams.rightMargin = (int) (10 * mDensity);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout subLinearLayout = null;

                try {
                    List list = (List) componentValue.get(schema.getName());
                    for (Object object : list) {
                        for (int i = 0; i < schema.getCloneable().size(); i++) {
                            Schema innerSchema = schema.getCloneable().get(i);
                            Map<String, Object> map = (HashMap<String, Object>) object;
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                if (innerSchema.getName().equalsIgnoreCase(entry.getKey())) {
                                    subLinearLayout = setSingleLineView(innerSchema.getLabel(), entry.getValue().toString());
                                }
                            }

                            if (subLinearLayout != null) {
                                linearLayout.addView(subLinearLayout);
                            }
                        }
                        if (list.get(list.size() - 1) != object) {
                            View view = new View(this);
                            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * mDensity));
                            view.setLayoutParams(layoutParams1);
                            view.setBackgroundColor(Color.parseColor("#CCCCCC"));
                            linearLayout.addView(view);
                        }
                    }

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        linearLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.dependable_component_drawable));
                    } else {
                        linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.dependable_component_drawable));
                    }

                    mainContainer.addView(linearLayout);
                }catch (Exception e) {
                    LinearLayout linearLayout1 = setSingleLineView(schema.getLabel(), componentValue.get(schema.getName()).toString());

                    mainContainer.addView(linearLayout1);
                }
            }else {

                LinearLayout linearLayout = setSingleLineView(schema.getLabel(), componentValue.get(schema.getName()).toString());

                mainContainer.addView(linearLayout);

            }

            View view = new View(this);
            LinearLayout.LayoutParams viewLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * mDensity));
            view.setLayoutParams(viewLayout);
            view.setBackgroundColor(getResources().getColor(R.color.view_color));
            mainContainer.addView(view);

        }

    }

    private LinearLayout setSingleLineView(String key, String value) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.topMargin = (int) (10 * mDensity);
        layoutParams.leftMargin = (int) (10 * mDensity);
        layoutParams.bottomMargin = (int) (10 * mDensity);
        layoutParams.rightMargin = (int) (10 * mDensity);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView titleTextView = new TextView(this);
        LinearLayout.LayoutParams titleTextViewParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        titleTextView.setLayoutParams(titleTextViewParams);
        titleTextView.setText(key);
        titleTextView.setTextColor(getResources().getColor(R.color.title_text_color));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.show_component_label_size));

        TextView valueTextView = new TextView(this);

        LinearLayout.LayoutParams valueTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        valueTextView.setLayoutParams(valueTextViewParams);
        valueTextView.setText(value);
        valueTextView.setGravity(Gravity.RIGHT);
        valueTextView.setTextColor(getResources().getColor(R.color.black));
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.show_component_value_size));

        linearLayout.addView(titleTextView);

        linearLayout.addView(valueTextView);

        return linearLayout;

    }

    private void setSingleText(LinearLayout linearLayout, String name) {

    }

    //    @OnClick(R.id.edit_pos_btn)
    public void editPos(View view) {
        Intent intent = new Intent(ShowTaskFormDetailsActivity.this, TaskFormActivity.class);
        intent.putExtra("formId", formId);
        intent.putExtra("taskId", taskId);
        intent.putExtra("markerLat", markerLat);
        intent.putExtra("markerLong", markerLong);
        intent.putExtra("componentValue", (HashMap<String, Object>) componentValue);
        startActivity(intent);
    }

    @OnClick(R.id.back_btn)
    public void backBtn(View view) {
        super.onBackPressed();
    }

    @OnClick(R.id.new_revisit_form_btn)
    public void newRevisitForm(View view) {
        Intent intent = new Intent(ShowTaskFormDetailsActivity.this, TaskFormActivity.class);
        intent.putExtra("formId", formId);
        intent.putExtra("taskId", taskId);
        intent.putExtra("posId", posId);
        intent.putExtra("formType", ThrustConstant.FORM_TYPE_REVISIT_FORM);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.next_form_btn)
    public void nextForm(View view) {

        Data nextFormData = null;
        boolean shouldBreakLoop = false;
        for(int i = 0; i < formList.size(); i++) {
            Data data = formList.get(i);
            if(data.getForm().getId().longValue() == formId.longValue()) {
                if(i + 1 < formList.size()) {
                    nextFormData = formList.get(i+1);
                }else {
                    nextFormData = formList.get(0);
                }
                shouldBreakLoop = true;
            }
            if(shouldBreakLoop) {
                break;
            }
        }

        Intent intent = new Intent(ShowTaskFormDetailsActivity.this, TaskFormActivity.class);
        intent.putExtra("formId", nextFormData.getForm().getId());
        intent.putExtra("taskId", nextFormData.getId().intValue());
        intent.putExtra("posId", posId);
        intent.putExtra("formType", nextFormData.getForm().getFormType());
        intent.putExtra("formList", formList);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_show_task_form_details;
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }
}

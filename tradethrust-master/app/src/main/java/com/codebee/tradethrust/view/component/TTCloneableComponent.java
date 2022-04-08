package com.codebee.tradethrust.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.view.interfaces.CloneableComponent;
import com.codebee.tradethrust.view.interfaces.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TTCloneableComponent extends LinearLayout implements OnItemSelectedListener {

    private Context context;

    private TextView labelTextView;
    private ViewGroup parentView;

    private RelativeLayout buttonLayout;
    private LinearLayout layout;

    private Button addButton;
    private Button removeButton;
//    private View topViewBorder;
    private View bottomViewBorder;

    private TTCloneableComponent previousCloneableComponent;
    private ComponentGenerator componentGenerator;
    private CloneableComponent cloneableComponent;

    private Schema schema;

    private float density;
    private boolean isRemoveNeeded = false;


    public TTCloneableComponent(Context context, CloneableComponent cloneableComponent) {
        super(context);
        this.cloneableComponent = cloneableComponent;
        initLayout(context);
    }

    public TTCloneableComponent(Context context, boolean isRemoveNeeded, TTCloneableComponent previousMultiSpinner) {
        super(context);
        this.isRemoveNeeded = isRemoveNeeded;
        this.previousCloneableComponent = previousMultiSpinner;
        initLayout(context);
    }

    public TTCloneableComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        density = context.getResources().getDisplayMetrics().density;

        configMainLayout(context);
        if(previousCloneableComponent == null) {
            configLabelTextView();
        }

        configComponentLayout(context);

        setVisibility(View.GONE);
    }

    private void configButtonLayout(Context context) {
        buttonLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        buttonLayout.setLayoutParams(layoutParams);

        layout.addView(buttonLayout);

        createAddButton();

        if(isRemoveNeeded) {
            createRemoveButton();
        }
    }

    private void configMainLayout(Context context) {
        LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (16 * context.getResources().getDisplayMetrics().density);
        mainParams.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

    }

    private void configLabelTextView() {
        labelTextView = new TextView(context);
        LinearLayout.LayoutParams fileNameTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileNameTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        fileNameTextViewLayoutParams.bottomMargin = (int) (5 * density);
        fileNameTextViewLayoutParams.topMargin = (int) (5 * density);
        fileNameTextViewLayoutParams.leftMargin = (int) (5 * density);
        labelTextView.setTypeface(Typeface.DEFAULT_BOLD);
        labelTextView.setLayoutParams(fileNameTextViewLayoutParams);
        labelTextView.setTextColor(Color.parseColor("#000000"));
        labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.cloneable_component_label_size));
        addView(labelTextView);
    }

    private void configComponentLayout(Context context) {
        layout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setLayoutParams(layoutParams);
        layout.setOrientation(VERTICAL);
        layout.setPadding((int) (8 * density), (int) (8 * density), (int) (8 * density), (int) (8 * density));

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dependable_component_drawable) );
        } else {
            layout.setBackground(ContextCompat.getDrawable(context, R.drawable.dependable_component_drawable));
        }

        layout.setPadding((int) (5 * density),(int) (5 * density), (int) (5 * density), (int) (5 * density));

        addView(layout);

    }

    private void createAddButton() {
        addButton = new Button(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (40 * density), (int) (40 * density));
        layoutParams.topMargin = (int) (5 * density);
        layoutParams.rightMargin = (int) (5 * density);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addButton.setLayoutParams(layoutParams);
        addButton.setGravity(Gravity.CENTER_VERTICAL);
        addButton.setBackground(context.getResources().getDrawable(R.drawable.ic_add_box));

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                TTCloneableComponent.this.getAddButton().setVisibility(View.GONE);
                TTCloneableComponent.this.getBottomViewBorder().setVisibility(View.GONE);

                if(TTCloneableComponent.this.getRemoveButton() != null) {
                    TTCloneableComponent.this.getRemoveButton().setVisibility(View.GONE);
                }

                TTCloneableComponent ttCloneableComponent = new TTCloneableComponent(context, true, TTCloneableComponent.this);
                ttCloneableComponent.setTag(schema.getName());
                ttCloneableComponent.setSchema(schema, parentView);
                int index = parentView.indexOfChild(TTCloneableComponent.this);
                parentView.addView(ttCloneableComponent, index + 1);

            }
        });

        buttonLayout.addView(addButton);
    }

    private void createRemoveButton() {
        removeButton = new Button(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (40 * density), (int) (40 * density));
        layoutParams.topMargin = (int) (5 * density);
        layoutParams.leftMargin = (int) (5 * density);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        removeButton.setLayoutParams(layoutParams);
        removeButton.setGravity(Gravity.CENTER_VERTICAL);
        removeButton.setBackground(context.getResources().getDrawable(R.drawable.ic_minus_box));

        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Manage the visibility and disability of the view
                if (previousCloneableComponent != null && previousCloneableComponent.getAddButton() != null) {
                    previousCloneableComponent.getAddButton().setVisibility(View.VISIBLE);
                }

                if (previousCloneableComponent != null && previousCloneableComponent.getRemoveButton() != null) {
                    previousCloneableComponent.getRemoveButton().setVisibility(VISIBLE);
                }

                if(previousCloneableComponent != null && previousCloneableComponent.getBottomViewBorder() != null) {
                    previousCloneableComponent.getBottomViewBorder().setVisibility(View.VISIBLE);
                }
                parentView.removeView(TTCloneableComponent.this);

            }
        });

        buttonLayout.addView(removeButton);
    }

    private View addBorderView() {
        View view = new View(context);
        view.setBackgroundColor(Color.parseColor("#CCCCCC"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * density));
        view.setLayoutParams(params);
        layout.addView(view);

        return view;

    }

    private void setFormView(ArrayList<Schema> schemaList) {
        componentGenerator = new ComponentGenerator((Activity) context, this);

        for (Schema schema : schemaList) {

            if(previousCloneableComponent == null) {
                removeViews(schema);
            }

            componentGenerator.getComponent((Activity) context, schema, layout);
        }

        bottomViewBorder = addBorderView();

        configButtonLayout(context);

        if(isRemoveNeeded && removeButton != null) {
            removeButton.setVisibility(View.VISIBLE);
        }

        componentGenerator.manageView();
    }

    private void removeViews(Schema schema) {
        if(cloneableComponent != null) {
            cloneableComponent.manageCloneableComponent(schema);
        }

        ArrayList<View> views = new ArrayList<>();
        parentView.findViewsWithText(views, schema.getName(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        for(View v: views) {
            ((ViewGroup) v.getParent()).removeView(v);
        }

        View view = parentView.findViewWithTag(schema.getName());
        if(view != null && view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }


    }


    public void setSchema(Schema schema, ViewGroup parentView) {
        this.parentView = parentView;
        setSchema(schema);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        String textToShow = schema.getLabel();

        if (labelTextView != null && textToShow != null) {
            labelTextView.setText(textToShow);
        }

        setFormView(schema.getCloneable());

        setVisibility(VISIBLE);

        parentView.requestChildFocus(TTCloneableComponent.this,TTCloneableComponent.this);

    }

    public void setTag(String tag) {
        super.setTag(tag);
        this.setContentDescription(tag);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public View getBottomViewBorder() {
        return bottomViewBorder;
    }

    @Override
    public void onItemSelected(String componentTag) {

    }

    public List<Map<String, Object>> getAllSelectedValues(LinearLayout activityTaskFormContainer){

        List<Map<String, Object>> values = new ArrayList<>();

        ArrayList<View> views = new ArrayList<>();

        parentView.findViewsWithText(views, this.getTag().toString(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

        for(View view: views) {
            if(view instanceof TTCloneableComponent) {

                TTCloneableComponent component = (TTCloneableComponent) view ;

                Map<String, Object> value =  new HashMap<>();

                for(Schema schema:this.schema.getCloneable()) {

                    View subComponent = component.findViewWithTag(schema.getName());

                    component.componentGenerator.validateAndGetCloneableComponentRecords(value, schema, schema.getType(), subComponent, activityTaskFormContainer);

                }

                values.add(value);
            }
        }

        return values;
    }
}
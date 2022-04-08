package com.codebee.tradethrust.view.component;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.utils.Utils;
import com.codebee.tradethrust.view.acitivity.TaskFormActivity;
import com.codebee.tradethrust.view.acitivity.ViewImageActivity;
import com.codebee.tradethrust.view.interfaces.CloneableComponent;
import com.codebee.tradethrust.view.interfaces.OnDistrictSelectedListener;
import com.codebee.tradethrust.view.interfaces.OnItemSelectedListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by csangharsha on 5/25/18.
 */

public class ComponentGenerator implements OnDistrictSelectedListener, CloneableComponent {

    public static final String COMPONENT_TYPE_TEXT_VIEW = "textView";
    public static final String COMPONENT_TYPE_EDIT_TEXT = "text";
    public static final String COMPONENT_TYPE_CHECKBOX = "checkbox-group";
    public static final String COMPONENT_TYPE_RADIO = "radio-group";
    public static final String COMPONENT_TYPE_FILE = "file";
    public static final String COMPONENT_TYPE_SELECT = "select";
    public static final String COMPONENT_TYPE_HIDDEN = "hidden";
    public static final String COMPONENT_TYPE_AUTOCOMPLETE = "autocomplete";
    public static final String COMPONENT_TYPE_DATE_FIELD = "date";
    public static final String COMPONENT_TYPE_NUMBER = "number";
    public static final String COMPONENT_TYPE_HEADER = "header";
    public static final String COMPONENT_TYPE_TEXT_AREA = "textarea";
    public static final String COMPONENT_TYPE_DEPENDENT_SELECT = "dependentSelect";
    public static final String COMPONENT_TYPE_MULTI_SELECT = "multiSelect";
    public static final String COMPONENT_TYPE_AUTOCOMPLETE_SUBTYPE_MUNICIPALITY = "municipality";
    public static final String COMPONENT_TYPE_CLONEABLE = "cloneable";

    @Deprecated
    public static final String COMPONENT_TYPE_SELECT_SUBTYPE_MUNICIPALITY = "municipality";

    public static final int SELECTED_PIC_FROM_GALLERY = 200;
    public static final int SELECTED_PIC_FROM_CAMERA = 300;
    private Uri imageURI;

    private float mDensity;
    private Activity activity;

    private OnItemSelectedListener onItemSelectedListener;

    private DependentSelectView dependentSelectView = null;
    private ProvinceZoneDistrictComponent provinceZoneDistrictComponent = null;
    private TTAutoCompleteEditText autoCompleteEditText;
    private TTFilterableSpinner filterableSpinnerComponent;
    private CloneableComponent cloneableComponent;

    public ComponentGenerator(Activity activity, OnItemSelectedListener onItemSelectedListener, CloneableComponent cloneableComponent) {
        this.activity = activity;
        this.onItemSelectedListener = onItemSelectedListener;
        this.cloneableComponent = cloneableComponent;
        mDensity = activity.getResources().getDisplayMetrics().density;
    }

    public ComponentGenerator(Activity activity, OnItemSelectedListener onItemSelectedListener) {
        this.activity = activity;
        this.onItemSelectedListener = onItemSelectedListener;
        mDensity = activity.getResources().getDisplayMetrics().density;
    }

    public void getComponent(Activity taskFormActivity, Schema schema, LinearLayout formContainer) {
        getComponent(taskFormActivity, schema, formContainer, null);
    }

    public void getComponent(Context context, Schema schema, ViewGroup parentView, String value) {

        String viewType = schema.getType();
        int componentId;
        if (schema.getName() != null) {
            componentId = context.getResources().getIdentifier(schema.getName(), "id", activity.getPackageName());
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                componentId = Utils.generateViewId();
            } else {
                componentId = View.generateViewId();
            }
        }

        if (viewType.equals(COMPONENT_TYPE_TEXT_VIEW)) {
            createTextViewTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_EDIT_TEXT) || viewType.equals(COMPONENT_TYPE_TEXT_AREA)) {
            createEditText(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_NUMBER)) {
            createNumberTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_CHECKBOX)) {
            createCheckBoxTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_RADIO)) {
            createRadioTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_FILE)) {
            createFileTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_SELECT)) {
            if(ThrustConstant.PROVINCE.equalsIgnoreCase(schema.getLabel())) {
                provinceZoneDistrictComponent = new ProvinceZoneDistrictComponent(context);
                parentView.addView(provinceZoneDistrictComponent);
                provinceZoneDistrictComponent.setProvince(schema);
            }else if(ThrustConstant.ZONE.equalsIgnoreCase(schema.getLabel())){
                if(provinceZoneDistrictComponent != null) {
                    provinceZoneDistrictComponent.setZone(schema);
                }
            }else if(ThrustConstant.DISTRICT.equalsIgnoreCase(schema.getLabel())) {
                if(provinceZoneDistrictComponent != null) {
                    provinceZoneDistrictComponent.setDistrict(schema);
                }
            }else if(ThrustConstant.MUNICIPALITY.equalsIgnoreCase(schema.getLabel())) {
                if(provinceZoneDistrictComponent != null) {
                    provinceZoneDistrictComponent.setMunicipality(schema);
                }
            }else{
                createSelectTypeView(context, schema, parentView, componentId, value);
            }
        } else if (viewType.equals(COMPONENT_TYPE_AUTOCOMPLETE)) {
            createAutoCompleteTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_DATE_FIELD)) {
            createDateFieldTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_HIDDEN)) {
            createHiddenFieldTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_HEADER)) {
            createHeaderTypeView(context, schema, parentView, componentId, value);
        } else if (viewType.equals(COMPONENT_TYPE_DEPENDENT_SELECT)) {
            dependentSelectView = createDependentSelectType(context, schema, parentView, componentId, value);
        } else if(viewType.equals(COMPONENT_TYPE_MULTI_SELECT)) {
            createMultiSelectType(context, schema, parentView, componentId, value);
        } else if(viewType.equals(COMPONENT_TYPE_CLONEABLE)) {
            createCloneableType(context, schema, parentView, componentId, value);
        }

    }

    private void createCloneableType(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TTCloneableComponent cloneableComponent = new TTCloneableComponent(context, this);
        cloneableComponent.setTag(schema.getName());
        cloneableComponent.setSchema(schema, parentView);
        parentView.addView(cloneableComponent);
    }

    public boolean validateAndGetComponentRecords(Map<String, String> records, Schema schema, String componentType, View component, LinearLayout activityTaskFormContainer) {
        if(component != null && component.getVisibility() == View.GONE){
            return true;
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_HIDDEN)) {

        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_SELECT)) {
            if(ThrustConstant.PROVINCE.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedProvinceValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedProvinceValue().getValue());
                }
            }else if(ThrustConstant.ZONE.equalsIgnoreCase(schema.getName())){
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedZoneValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedZoneValue().getValue());
                }
            }else if(ThrustConstant.DISTRICT.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedDistrictValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedDistrictValue().getValue());
                }
            }else if(ThrustConstant.MUNICIPALITY.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedMunicipality() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedMunicipality().getValue());
                }
            }else {
                if(component != null) {
                    if (component != null) {
                        Value value = ((Value) ((Spinner) component).getSelectedItem());
                        records.put(schema.getName(), value.getValue());
                    } else {
                        String message = "Please check the form schema";
                        showErrorMessage(activityTaskFormContainer, message);
                        return false;
                    }
                }else {
                    String message = "Please check the form schema";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
            }
        }

        if(componentType.equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_MULTI_SELECT)) {
            List<String> selectedValues = new ArrayList<>();

            ArrayList<View> allmultiSelectViews = new ArrayList<>();
            LinearLayout formContainer = activityTaskFormContainer.findViewById(R.id.form_container);

            formContainer.findViewsWithText(allmultiSelectViews, component.getTag().toString(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

            for(int i=0; i < allmultiSelectViews.size();i++) {
                View view = allmultiSelectViews.get(i);

                if(view instanceof Spinner) {
                    Spinner spinner = (Spinner) view;
                    selectedValues.add( ((Value)spinner.getSelectedItem()).getValue() );
                }

            }
            records.put(schema.getName(), new Gson().toJson(selectedValues));
        }

        if(componentType.equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_CLONEABLE)) {
            if(component instanceof TTCloneableComponent) {
                TTCloneableComponent ttCloneableComponent = (TTCloneableComponent) component;
                List<Map<String, Object>> values = ttCloneableComponent.getAllSelectedValues(activityTaskFormContainer);
                if(values != null) {
                    records.put(schema.getName(), new Gson().toJson(values));
                }else {
                    return false;
                }
            }
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_CHECKBOX)) {
            List<String> checkedValue = ((TTCheckBox) component).getCheckedValue();
            if(schema.isRequired() && checkedValue.size() == 0) {
                String message = schema.getLabel() + " is a required field.";
                showErrorMessage(activityTaskFormContainer, message);
                return false;
            }
            records.put(schema.getName(), new Gson().toJson(checkedValue));
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_RADIO)) {
            String selectedValue = ((TTRadioButton) component).getSelectedValue();
            if(schema.isRequired() && selectedValue == null) {
                String message = schema.getLabel() + " is a required field.";
                showErrorMessage(activityTaskFormContainer, message);
                return false;
            }
            records.put(schema.getName(), selectedValue);
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_EDIT_TEXT)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_TEXT_VIEW)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_DATE_FIELD)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_NUMBER)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_HIDDEN)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_TEXT_AREA)) {
            if(component != null) {
                if (schema.isRequired() && ((TextView) component).getText().toString().isEmpty()) {
                    String message = schema.getLabel() + " is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if (schema.getMin() != null && schema.getMin() > ((TextView) component).getText().toString().length()) {
                    String message = "The min length of " + schema.getLabel() + " is " + schema.getMin() + ".";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if (schema.getMax() != null && schema.getMax() < ((TextView) component).getText().toString().length()) {
                    String message = "The max length of " + schema.getLabel() + " is " + schema.getMin() + ".";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if(schema.getName().equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE_SUBTYPE_MUNICIPALITY) && ((TextView) component).getText().toString().isEmpty()) {
                    String message = schema.getName() + " is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                records.put(schema.getName(), ((TextView) component).getText().toString());
            }else {
                if (filterableSpinnerComponent.getSelectedItem() != null && !filterableSpinnerComponent.getSelectedItemLabel().isEmpty()) {
                    records.put(schema.getName(), filterableSpinnerComponent.getSelectedItemLabel());
                } else {
                    String message = "Municipality is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
            }

        }

        if(componentType.equals(ComponentGenerator.COMPONENT_TYPE_DEPENDENT_SELECT)) {

                records.put(schema.getName(), dependentSelectView.getSelectedValue().getValue());

        }
        return true;
    }

    public boolean validateAndGetCloneableComponentRecords(Map<String, Object> records, Schema schema, String componentType, View component, LinearLayout activityTaskFormContainer) {
        if(component != null && component.getVisibility() == View.GONE){
            return true;
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_HIDDEN)) {

        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_SELECT)) {
            if(ThrustConstant.PROVINCE.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedProvinceValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedProvinceValue().getValue());
                }
            }else if(ThrustConstant.ZONE.equalsIgnoreCase(schema.getName())){
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedZoneValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedZoneValue().getValue());
                }
            }else if(ThrustConstant.DISTRICT.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedDistrictValue() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedDistrictValue().getValue());
                }
            }else if(ThrustConstant.MUNICIPALITY.equalsIgnoreCase(schema.getName())) {
                if(provinceZoneDistrictComponent != null && provinceZoneDistrictComponent.getSelectedMunicipality() != null) {
                    records.put(schema.getName(), provinceZoneDistrictComponent.getSelectedMunicipality().getValue());
                }
            }else {
                if(component != null) {
                    if (component != null) {
                        Value value = ((Value) ((Spinner) component).getSelectedItem());
                        records.put(schema.getName(), value.getValue());
                    } else {
                        String message = "Please check the form schema";
                        showErrorMessage(activityTaskFormContainer, message);
                        return false;
                    }
                }else {
                    String message = "Please check the form schema";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
            }
        }

        if(componentType.equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_MULTI_SELECT)) {
            List<String> selectedValues = new ArrayList<>();

            ArrayList<View> allmultiSelectViews = new ArrayList<>();
            LinearLayout formContainer = activityTaskFormContainer.findViewById(R.id.form_container);

            formContainer.findViewsWithText(allmultiSelectViews, component.getTag().toString(), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

            for(int i=0; i < allmultiSelectViews.size();i++) {
                View view = allmultiSelectViews.get(i);

                if(view instanceof Spinner) {
                    Spinner spinner = (Spinner) view;
                    selectedValues.add( ((Value)spinner.getSelectedItem()).getValue() );
                }

            }
            records.put(schema.getName(), selectedValues);
        }

        if(componentType.equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_CLONEABLE)) {
            if(component instanceof TTCloneableComponent) {
                TTCloneableComponent ttCloneableComponent = (TTCloneableComponent) component;
                List<Map<String, Object>> values = ttCloneableComponent.getAllSelectedValues(activityTaskFormContainer);
                if(values != null) {
                    records.put(schema.getName(), values);
                }else {
                    return false;
                }
            }
            if(records.containsKey(schema.getName())) {
                // Log.d("Sangharsha", records.get(schema.getName()));
            }
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_CHECKBOX)) {
            List<String> checkedValue = ((TTCheckBox) component).getCheckedValue();
            if(schema.isRequired() && checkedValue.size() == 0) {
                String message = schema.getLabel() + " is a required field.";
                showErrorMessage(activityTaskFormContainer, message);
                return false;
            }
            records.put(schema.getName(), checkedValue);
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_RADIO)) {
            String selectedValue = ((TTRadioButton) component).getSelectedValue();
            if(schema.isRequired() && selectedValue == null) {
                String message = schema.getLabel() + " is a required field.";
                showErrorMessage(activityTaskFormContainer, message);
                return false;
            }
            records.put(schema.getName(), selectedValue);
        }

        if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_EDIT_TEXT)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_TEXT_VIEW)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_DATE_FIELD)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_NUMBER)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_HIDDEN)
                || componentType.equals(ComponentGenerator.COMPONENT_TYPE_TEXT_AREA)) {
            if(component != null) {
                if (schema.isRequired() && ((TextView) component).getText().toString().isEmpty()) {
                    String message = schema.getLabel() + " is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if (schema.getMin() != null && schema.getMin() > ((TextView) component).getText().toString().length()) {
                    String message = "The min length of " + schema.getLabel() + " is " + schema.getMin() + ".";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if (schema.getMax() != null && schema.getMax() < ((TextView) component).getText().toString().length()) {
                    String message = "The max length of " + schema.getLabel() + " is " + schema.getMin() + ".";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                if(schema.getName().equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE_SUBTYPE_MUNICIPALITY) && ((TextView) component).getText().toString().isEmpty()) {
                    String message = schema.getName() + " is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
                records.put(schema.getName(), ((TextView) component).getText().toString());
            }else {
                if (filterableSpinnerComponent.getSelectedItem() != null && !filterableSpinnerComponent.getSelectedItemLabel().isEmpty()) {
                    records.put(schema.getName(), filterableSpinnerComponent.getSelectedItemLabel());
                } else {
                    String message = "Municipality is a required field.";
                    showErrorMessage(activityTaskFormContainer, message);
                    return false;
                }
            }

        }

        if(componentType.equals(ComponentGenerator.COMPONENT_TYPE_DEPENDENT_SELECT)) {

            records.put(schema.getName(), dependentSelectView.getSelectedValue().getValue());

        }
        return true;
    }


    private void showErrorMessage(LinearLayout activityTaskFormContainer, String s) {
        Snackbar snackbar = Snackbar
                .make(activityTaskFormContainer, s, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params=(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
    }

    private DependentSelectView createDependentSelectType(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        DependentSelectView dependentSelectView= new DependentSelectView(context, schema, parentView, componentId, value);
        return dependentSelectView;
    }

    private void createHeaderTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        if(schema.getName() != null && !schema.getName().isEmpty()) {
            TTHeader ttHeader = new TTHeader(context);
            ttHeader.setId(componentId);
            ttHeader.setSchema(schema);
            if (value != null) {
                ttHeader.setText(value);
            }

            parentView.addView(ttHeader);
        }
    }

    private void createHiddenFieldTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TextView hiddenField = new TextView(context);
        hiddenField.setVisibility(View.GONE);
        hiddenField.setId(componentId);
        hiddenField.setTag(schema.getName());
        if(value != null) {
            hiddenField.setText(value);
        }

        parentView.addView(hiddenField);
    }

    private void createTextViewTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TextView textView = new TextView(context);
        textView.setId(componentId);
        textView.setTag(schema.getName());

        if(value != null) {
            textView.setText(value);
        }

        parentView.addView(textView);
    }

    private void createNumberTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TextInputLayout textInputLayout = new TextInputLayout(context, null, R.style.TextLabel);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.bottomMargin = (int) (5 * mDensity);
        textInputLayout.setLayoutParams(editTextParams);

        EditText editText = new EditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setId(componentId);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.setTag(schema.getName());
        editText.setHint(schema.getLabel());

        if(value != null) {
            editText.setText(value);
        }

        textInputLayout.addView(editText);
        parentView.addView(textInputLayout);
    }

    private void createDateFieldTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TTDateField dateField = new TTDateField(context);
        dateField.setSchema(schema);
        dateField.setId(componentId);

        if(value != null) {
            dateField.setDate(value);
        }

        parentView.addView(dateField);
    }

    private void createAutoCompleteTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        autoCompleteEditText = new TTAutoCompleteEditText(context);
        autoCompleteEditText.setSchema(schema);
        autoCompleteEditText.setId(componentId);

        if(value != null) {
            autoCompleteEditText.setText(value);
        }

        parentView.addView(autoCompleteEditText);
    }

    private void createRadioTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TTRadioButton radioBtn = new TTRadioButton(context);
        radioBtn.setSchema(schema);
        radioBtn.setId(componentId);
        radioBtn.setTag(schema.getName());
        if(value != null) {
            radioBtn.setChecked(value);
        }

        parentView.addView(radioBtn);
    }

    private void createCheckBoxTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TTCheckBox checkBox = new TTCheckBox(context);
        checkBox.setSchema(schema);
        checkBox.setId(componentId);
        checkBox.setTag(schema.getName());
        if(value != null) {
            checkBox.setChecked(value);
        }
        parentView.addView(checkBox);
    }

    private void createSelectTypeView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        ThrustSpinner spinner;
        if(value == null) {
            spinner = new ThrustSpinner(context);
        }else {
            spinner = new ThrustSpinner(context, value);
        }

        spinner.setSchema(schema, parentView);
        spinner.setId(componentId);
        spinner.setTag(schema.getName());

        parentView.addView(spinner);
    }

    private void createMultiSelectType(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        TTMultiSpinner spinner = new TTMultiSpinner(context);
        spinner.setSchema(schema, parentView);
        spinner.setId(componentId);
        spinner.setTag(schema.getName());

        parentView.addView(spinner);
    }

    private void createEditText(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {

        TextInputLayout textInputLayout = new TextInputLayout(context, null, R.style.TextLabel);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.bottomMargin = (int) (5 * mDensity);
        textInputLayout.setLayoutParams(editTextParams);

        EditText editText = new EditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editText.setId(componentId);
        editText.setTag(schema.getName());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setMaxLines(1);
        editText.setHint(schema.getLabel());
        if(value != null) {
            editText.setText(value);
        }

        textInputLayout.addView(editText);
        parentView.addView(textInputLayout);
    }

    private void createFileTypeView(final Context context, final Schema schema, ViewGroup parentView, int componentId, String value) {
        LinearLayout fileTypeViewMainContainer = new LinearLayout(context);
        LinearLayout.LayoutParams fileTypeMainContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileTypeMainContainerParams.bottomMargin = (int) (5 * mDensity);
        fileTypeViewMainContainer.setLayoutParams(fileTypeMainContainerParams);
        fileTypeViewMainContainer.setOrientation(LinearLayout.VERTICAL);
        fileTypeViewMainContainer.setPadding((int) (2 * mDensity), 0, (int) (2 * mDensity), 0);

        LinearLayout fileTypeViewSubContainer = new LinearLayout(context);
        fileTypeViewSubContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        fileTypeViewSubContainer.setOrientation(LinearLayout.HORIZONTAL);

        final TextView fileNameTextView = new TextView(context);
        LinearLayout.LayoutParams fileNameTextViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileNameTextViewLayoutParams.weight = 1;
        fileNameTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        fileNameTextView.setLayoutParams(fileNameTextViewLayoutParams);
        fileNameTextView.setHint(schema.getLabel());
        fileNameTextView.setId(componentId);
        fileNameTextView.setTag(schema.getName());
        fileNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.component_label_size));

        fileTypeViewSubContainer.addView(fileNameTextView);

        ImageView cameraImageView = new ImageView(context);
        LinearLayout.LayoutParams cameraViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cameraViewParams.gravity = Gravity.RIGHT;
        cameraImageView.setLayoutParams(cameraViewParams);
        cameraImageView.setImageResource(R.drawable.ic_camera);

        fileTypeViewSubContainer.addView(cameraImageView);

        fileTypeViewMainContainer.addView(fileTypeViewSubContainer);

        View view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.view_color));
        LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (context.getResources().getDimension(R.dimen.view_size)));
        viewLayoutParams.topMargin = (int) (2 * mDensity);
        view.setLayoutParams(viewLayoutParams);

        fileTypeViewMainContainer.addView(view);
        parentView.addView(fileTypeViewMainContainer);

        fileTypeViewMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageToUpload(context, fileNameTextView);
            }
        });
    }

    private void openImageToUpload(Context context, TextView textView) {
        if (textView.getText().toString() != null && !textView.getText().toString().isEmpty()) {
            openOpenDialog(context, textView);
        } else {
            selectImageFromCamera(context, textView);
        }
    }

    public Uri getImageURI() {
        return imageURI;
    }

    private void selectImageFromCamera(Context context, TextView textView) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageURI = activity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
        onItemSelectedListener.onItemSelected(textView.getTag().toString());

        activity.startActivityForResult(intent, SELECTED_PIC_FROM_CAMERA);
    }

    private void openOpenDialog(final Context context, final TextView textView) {
        CharSequence options[] = new CharSequence[]{"Take new Photo", "View Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    selectImageFromCamera(context, textView);
                } else {
                    Intent intent = new Intent(activity, ViewImageActivity.class);
                    intent.putExtra("path", textView.getText().toString());
                    context.startActivity(intent);
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();
    }

    public void manageView() {
        if(dependentSelectView != null) {
            dependentSelectView.manageDependentView();
        }

    }

    @Override
    public void onDistrictSelected(String district) {
        if(autoCompleteEditText != null) {
            autoCompleteEditText.setDistrict(district);
        }
        if(filterableSpinnerComponent != null){
            filterableSpinnerComponent.setDistrict(district);
        }
    }

    @Override
    public void manageCloneableComponent(Schema schema) {
        if(cloneableComponent != null) {
            cloneableComponent.manageCloneableComponent(schema);
        }
    }
}

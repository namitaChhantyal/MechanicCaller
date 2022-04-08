package com.codebee.tradethrust.view.acitivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.create.FormDetailsCreate;
import com.codebee.tradethrust.model.form_details.create.Record;
import com.codebee.tradethrust.model.form_details.list.Datum;
import com.codebee.tradethrust.model.form_details.list.FormDetails;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.model.task.details.Data;
import com.codebee.tradethrust.network.FormDetailsForBitAPI;
import com.codebee.tradethrust.network.FormDetailsForTaskAPI;
import com.codebee.tradethrust.network.FormDetailsCreateAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.GPSTracker;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.ComponentGenerator;
import com.codebee.tradethrust.view.interfaces.CloneableComponent;
import com.codebee.tradethrust.view.interfaces.OnItemSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskFormActivity extends BaseActivity implements OnItemSelectedListener, EasyPermissions.PermissionCallbacks, CloneableComponent, LocationListener {

    private static final int RC_CAMERA_PERM = 100;

    private static final String[] PERMISSIONS =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };

    @BindView(R.id.activity_task_form_container)
    public LinearLayout activityTaskFormContainer;

    @BindView(R.id.form_container)
    public LinearLayout formContainer;

    @BindView(R.id.form_container_scroll_view)
    public ScrollView formContainerScrollView;

    @BindView(R.id.pos_name_edit_text)
    public EditText posNameEditText;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBarContainer;

    @BindView(R.id.longitude_text_view)
    public TextView longitudeTextView;

    @BindView(R.id.latitude_text_view)
    public TextView latitudeTextView;

    @BindView(R.id.task_not_found_container)
    public LinearLayout formNotFoundContainer;

    @BindView(R.id.save_pos_btn)
    public Button savePosBtn;

    private FormDetails formDetails;

    private static final String TAG = "log";
    private SharedPreferences preferences;
    private Retrofit retrofit;

    private static final int REQUEST_PERMISSIONS = 20;

    private GPSTracker gps;
    private LocationManager locationManager;
    private String provider;

    private Long formId;
    private int taskId = 0;
    private int posId = -1;

    private String selectedItemTag;

    private Map<String, Bitmap> imageFromForm = new HashMap<>();
    private double markerLong;
    private double markerLat;
    private Map<String, Object> componentValue;
    private ArrayList<Data> formList;
    private ComponentGenerator componentGenerator;
    private String formType;
    private List<Schema> cloneableComponent = new ArrayList<>();

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();

        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        formNotFoundContainer.setVisibility(View.GONE);

        if(intent.hasExtra("taskId")) {
            formId = intent.getLongExtra("formId", 0L);
            taskId = intent.getIntExtra("taskId", 0);
        }

        if(intent.hasExtra("posId")) {
            posId = intent.getIntExtra("posId", -1);
        }

        if(intent.hasExtra("formType")) {
            formType = intent.getStringExtra("formType");
        }

        if (intent.hasExtra("markerLong")) {
            markerLong = intent.getDoubleExtra("markerLong", 0);
        }

        if(intent.hasExtra("markerLat")) {
            markerLat = intent.getDoubleExtra("markerLat", 0);
        }

        if(intent.hasExtra("componentValue")) {
            componentValue = (HashMap<String, Object>) intent.getSerializableExtra("componentValue");
        }

        if(intent.hasExtra("formList")) {
            formList = (ArrayList<Data>) intent.getSerializableExtra("formList");
        }

        hideAllViews();

        grantPermissionAndSetView();
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void grantPermissionAndSetView() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
//            gps = new GPSTracker(this);
//            if (gps.canGetLocation()) {
//                final double latitude = gps.getLatitude();
//                final double longitude = gps.getLongitude();
//
//                longitudeTextView.setText(String.valueOf(longitude));
//                latitudeTextView.setText(String.valueOf(latitude));
//            }


            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the location provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {

            }

//            MOCK_setUpFormForTask();

            if(taskId != 0) {
                setUpFormForTask();
            }else {
                setUpFormForBit();
            }
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access your location, storage and camera to function properly.", RC_CAMERA_PERM, PERMISSIONS);
        }
    }

    private void hideAllViews() {
        formContainerScrollView.setVisibility(View.GONE);
        posNameEditText.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
    }

    private void setUpFormForTask() {
        FormDetailsForTaskAPI formDetailsForTaskAPI = retrofit.create(FormDetailsForTaskAPI.class);

        Map<String, String> headersInfo = getHeaderInfo();

        Call<FormDetails> formDetailsCall = formDetailsForTaskAPI.getFormDetails(String.valueOf(formId), headersInfo);
        progressBarContainer.setVisibility(View.VISIBLE);

        formDetailsCall.enqueue(new Callback<FormDetails>() {
            @Override
            public void onResponse(Call<FormDetails> call, Response<FormDetails> response) {
                formDetails = response.body();
                progressBarContainer.setVisibility(View.GONE);
                formContainerScrollView.setVisibility(View.VISIBLE);

                setFormView(formDetails);
            }

            @Override
            public void onFailure(Call<FormDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(TaskFormActivity.this, "Something went wrong. Please Contact Administrator.", Toast.LENGTH_SHORT).show();
                progressBarContainer.setVisibility(View.GONE);
            }
        });

    }

    private void setUpFormForBit() {
        FormDetailsForBitAPI formDetailsForBitAPI = retrofit.create(FormDetailsForBitAPI.class);

        Map<String, String> headersInfo = getHeaderInfo();

        Call<FormDetails> formDetailsCall = formDetailsForBitAPI.getFormDetails(headersInfo);
        progressBarContainer.setVisibility(View.VISIBLE);

        formDetailsCall.enqueue(new Callback<FormDetails>() {
            @Override
            public void onResponse(Call<FormDetails> call, Response<FormDetails> response) {
                formDetails = response.body();
                progressBarContainer.setVisibility(View.GONE);
                formContainerScrollView.setVisibility(View.VISIBLE);
                if(formDetails!=null) {
                    setFormView(formDetails);
                }else {
                    formContainerScrollView.setVisibility(View.GONE);
                    formNotFoundContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<FormDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(TaskFormActivity.this, "Something went wrong. Please Contact Administrator.", Toast.LENGTH_SHORT).show();
                progressBarContainer.setVisibility(View.GONE);
            }
        });
    }

    @NonNull
    private Map<String, String> getHeaderInfo() {
        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        headersInfo.put("platform", "mobileapp");
        return headersInfo;
    }

    private void setFormView(FormDetails formDetails) {
        componentGenerator = new ComponentGenerator(this, this, this);

//        MOCK_formSchema(formDetails);

        for (Schema schema : formDetails.getData().getSchema()) {
            if(componentValue != null && componentValue.containsKey(schema.getName())) {
                componentGenerator.getComponent(this, schema, formContainer, (String) componentValue.get(schema.getName()));
            } else {
                componentGenerator.getComponent(this, schema, formContainer);
            }
        }

        // remove cloneable component from schema
        for(Schema schema: cloneableComponent) {
            formDetails.getData().getSchema().remove(schema);
        }

        componentGenerator.manageView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_task_form;
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @OnClick(R.id.save_pos_btn)
    public void savePOS(View view) {
        savePosBtn.setEnabled(false);

        showProgressDialog("Saving...");
        
        if (formDetails != null) {

            FormDetailsCreate formDetailsCreate = new FormDetailsCreate();

            Record record = new Record();
            record.setFormId(formDetails.getData().getId());
            record.setTaskId((long) taskId);

            final Map<String, Object> records = new HashMap<>();
            List<MultipartBody.Part> body = null;
            record.setPosId(posId);

            for (Schema schema : formDetails.getData().getSchema()) {
                String componentType = schema.getType();
                View component = formContainer.findViewWithTag(schema.getName());

                if (componentType.equals(ComponentGenerator.COMPONENT_TYPE_FILE)) {
                    if (body == null) {
                        body = new ArrayList<>();
                    }
                    if (((TextView) component).getText().toString() != null && !((TextView) component).getText().toString().isEmpty()) {
                        body.add(prepareFilePart("record[data][" + schema.getName() + "]", ((TextView) component).getText().toString()));
                        records.put(schema.getName(), ((TextView) component).getText().toString());
                    }

                }

                if(!componentGenerator.validateAndGetCloneableComponentRecords(records, schema, componentType, component, activityTaskFormContainer)) {
                    records.clear();
                    body.clear();
                    hideProgressDialog();
                    savePosBtn.setEnabled(true);
                    return;
                }

            }

            records.put("longitude", longitudeTextView.getText().toString());
            records.put("latitude", latitudeTextView.getText().toString());

            if(markerLat == 0  || markerLong == 0) {
                records.put("pos_longitude", longitudeTextView.getText().toString());
                records.put("pos_latitude", latitudeTextView.getText().toString());
            }else {
                records.put("pos_longitude", String.valueOf(markerLong));
                records.put("pos_latitude", String.valueOf(markerLat));
            }

            record.setData(records);
            formDetailsCreate.setRecord(record);

            Map<String, RequestBody> map = getRequestBody(formDetailsCreate);
            Map<String, Object> testMap = getRequestBodyObjectForm(formDetailsCreate);
            if(componentValue == null) { // Adding new Form
                FormDetailsCreateAPI formDetailsAPI = retrofit.create(FormDetailsCreateAPI.class);

                Map<String, String> headersInfo = getHeaderInfo();

                Call<FormDetails> formDetailsCall;

                if(taskId==0) {
                    if (body == null || (body != null && body.size() == 0)) {
                        formDetailsCall = formDetailsAPI.createFormDetailsFromBit(formDetailsCreate, headersInfo);
                    } else {
                        formDetailsCall = formDetailsAPI.createFormDetailsFromBit(map, body, headersInfo);
                    }
                }else {
                    if (body == null || (body != null && body.size() == 0)) {
                        formDetailsCall = formDetailsAPI.createFormDetailsFromTask(formDetailsCreate, headersInfo);
                    } else {
                        formDetailsCall = formDetailsAPI.createFormDetailsFromTask(map, body, headersInfo);
                    }
                }


                // TODO DELETE THIS CODE: FROM
//                Intent intent = new Intent(TaskFormActivity.this, ShowTaskFormDetailsActivity.class);
//                intent.putExtra("formValueMap", (HashMap<String, Object>) records);
//                intent.putExtra("schemaList", (ArrayList<Schema>) formDetails.getData().getSchema());
//                intent.putExtra("formId", formId);
//                intent.putExtra("taskId", taskId);
//                intent.putExtra("posId", posId);
//                intent.putExtra("markerLat", markerLat);
//                intent.putExtra("markerLong", markerLong);
//                intent.putExtra("formType", formType);
//                intent.putExtra("formList", formList);
//                startActivity(intent);
//                finish();
//
//                if(true) {
//                    return;
//                }
                // TODO DELETE THIS CODE: TO

                formDetailsCall.enqueue(new Callback<FormDetails>() {
                    @Override
                    public void onResponse(Call<FormDetails> call, Response<FormDetails> response) {

                        hideProgressDialog();
                        if (response.raw().code() == 201) {
                            Toast.makeText(TaskFormActivity.this, "Form Saved", Toast.LENGTH_SHORT).show();

                            if(taskId != 0) {
                                Intent intent = new Intent(TaskFormActivity.this, ShowTaskFormDetailsActivity.class);
                                intent.putExtra("formValueMap", (HashMap<String, Object>) records);
                                intent.putExtra("schemaList", (ArrayList<Schema>) formDetails.getData().getSchema());
                                intent.putExtra("formId", formId);
                                intent.putExtra("taskId", taskId);
                                intent.putExtra("posId", posId);
                                intent.putExtra("markerLat", markerLat);
                                intent.putExtra("markerLong", markerLong);
                                intent.putExtra("formType", formType);
                                intent.putExtra("formList", formList);
                                startActivity(intent);
                                finish();
                            }else {
                                TaskFormActivity.this.onBackPressed();
                            }

                        } else {

                            Toast.makeText(TaskFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                        savePosBtn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<FormDetails> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        hideProgressDialog();
                        savePosBtn.setEnabled(true);
                    }
                });
            } else {
                // Editing Form
            }
        }
    }

    private Map<String, RequestBody> getRequestBody(FormDetailsCreate formDetailsCreate) {
        Map<String, Object> data = formDetailsCreate.getRecord().getData();
        Map<String, RequestBody> requestBodyMap = new HashMap<>();

        requestBodyMap.put("record[form_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getFormId())));
        requestBodyMap.put("record[task_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getTaskId())));
        requestBodyMap.put("record[pos_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getPosId())));
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if(entry.getValue() instanceof String) {
                requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
            }else {
//                requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
                if(((List) entry.getValue()).size() != 0 && ((List) entry.getValue()).get(0) instanceof Map) {
                    int count = 0;
                    for(Map<String, Object> cloneable : (List<HashMap<String, Object>>) entry.getValue()){
//                        requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
                        List<MultipartBody.Part> partList = new ArrayList<>();
                        MultipartBody.Builder builder = new MultipartBody.Builder();

                        for(Map.Entry<String, Object> cloneableObj: cloneable.entrySet()) {
//                            MultipartBody.Part part = MultipartBody.Part.createFormData(cloneableObj.getKey(), cloneableObj.getValue().toString());
//                            partList.add(part);
//                            builder.addFormDataPart(cloneableObj.getKey(), cloneableObj.getValue().toString());
                            requestBodyMap.put("record[data][" + entry.getKey() + "][" + cloneableObj.getKey() + "]["+ count +"]", createPartFromString(cloneableObj.getValue().toString()));
                        }

//                        requestBodyMap.put("record[data][" + entry.getKey() + "]", builder.build());
                        count++;
                    }
                }else {
                    requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
                }
            }
        }

        return requestBodyMap;
    }

    private Map<String, Object> getRequestBodyObjectForm(FormDetailsCreate formDetailsCreate) {
        Map<String, Object> data = formDetailsCreate.getRecord().getData();
        Map<String, Object> requestBodyMap = new HashMap<>();

        requestBodyMap.put("record[form_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getFormId())));
        requestBodyMap.put("record[task_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getTaskId())));
        requestBodyMap.put("record[pos_id]", createPartFromString(String.valueOf(formDetailsCreate.getRecord().getPosId())));
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if(entry.getValue() instanceof String) {
                requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
            }else if(entry.getValue() instanceof List){
                if(((List) entry.getValue()).size() != 0 && ((List) entry.getValue()).get(0) instanceof Map) {
                    for(Map<String, Object> cloneable : (List<HashMap<String, Object>>)entry.getValue()){
//                        requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
                        List<MultipartBody.Part> partList = new ArrayList<>();
                        for(Map.Entry<String, Object> cloneableObj: cloneable.entrySet()) {
                            MultipartBody.Part part = MultipartBody.Part.createFormData(cloneableObj.getKey(), cloneableObj.getValue().toString());
                            partList.add(part);
                        }
                        requestBodyMap.put("record[data][" + entry.getKey() + "]", partList);
                    }
                }else {
                    requestBodyMap.put("record[data][" + entry.getKey() + "]", createPartFromString(entry.getValue().toString()));
                }
            }
        }


        return requestBodyMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ComponentGenerator.SELECTED_PIC_FROM_GALLERY:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        String[] projection = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String filepath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap bitmap = shrinkBitmap(BitmapFactory.decodeFile(filepath));

                        if (selectedItemTag != null && !selectedItemTag.isEmpty()) {
                            TextView textView = formContainer.findViewWithTag(selectedItemTag);
                            textView.setText(filepath);
                            imageFromForm.put(selectedItemTag, bitmap);
                        }
                    }
                    break;
                case ComponentGenerator.SELECTED_PIC_FROM_CAMERA:
                    Uri imageUri = componentGenerator.getImageURI();
                    String imageurl = "";
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);

                        imageurl = getRealPathFromURI(imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(thumbnail != null && !imageurl.isEmpty()) {
                        if (selectedItemTag != null && !selectedItemTag.isEmpty()) {
                            TextView textView = formContainer.findViewWithTag(selectedItemTag);
                            textView.setText(imageurl);
                            imageFromForm.put(selectedItemTag, thumbnail);
                        }
                    }else {
                        Toast.makeText(this, "Image Could not be saved.", Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(String componentTag) {
        this.selectedItemTag = componentTag;
    }

    private Bitmap shrinkBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] BYTE;

        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytearrayoutputstream);

        BYTE = bytearrayoutputstream.toByteArray();

        return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileName) {
        File file = new File(fileName);

        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(getImageUri(this, bitmap))),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, TaskFormActivity.this);
    }


    private void MOCK_setUpFormForTask() {
        formDetails = new FormDetails();

        Datum data = new Datum();
        data.setId(1L);
        data.setName("Mock Task Form");
        data.setSchema(new ArrayList<Schema>());

        formDetails.setData(data);
        MOCK_formSchema(formDetails);
        setFormView(formDetails);
    }

    private void MOCK_formSchema(FormDetails formDetails) {
        Schema schema1;

//        schema1 = addCheckBoxSchema();
//        formDetails.getData().getSchema().add(schema1);


//        schema1 = addHeaderTextViewSchema();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addRadioBtnSchema();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addAutoCompleteTextViewSchema();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addDateFieldTextViewSchema();
//        formDetails.getData().getSchema().add(schema1);
//
        schema1 = addNumberTextViewSchema();
        formDetails.getData().getSchema().add(schema1);

//        schema1 = addProvinceSchema();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addZoneSchema();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addDistrictSchema();
//        formDetails.getData().getSchema().add(schema1);

//        schema1 = addCloneableComponent();
//        formDetails.getData().getSchema().add(schema1);
//
//        schema1 = addMultiSelectSchema();
//        formDetails.getData().getSchema().add(schema1);
    }

    private Schema addMultiSelectSchema() {
        Schema schema1 = new Schema();
        schema1.setType("multiSelect");
        schema1.setLabel("Multi Select");
        schema1.setName("multiselect");

        List<Value> values = new ArrayList<>();
        Value value = new Value();
        value.setLabel("Option 1");
        value.setValue("option-1");
        value.setSelected(true);
        values.add(value);

        value = new Value();
        value.setLabel("Option 2");
        value.setValue("option-2");
        values.add(value);


        value = new Value();
        value.setLabel("Option 3");
        value.setValue("option-3");
        values.add(value);

        schema1.setValues((ArrayList<Value>) values);
        return schema1;
    }

    @NonNull
    private Schema addCheckBoxSchema() {
        Schema schema1 = new Schema();
        schema1.setType("checkbox-group");
        schema1.setLabel("Checkbox Group");
        schema1.setName("checkbox-group-1528267583303");

        List<Value> values = new ArrayList<>();
        Value value = new Value();
        value.setLabel("Option 1");
        value.setValue("option-1");
        value.setSelected(true);
        values.add(value);

        value = new Value();
        value.setLabel("Option 2");
        value.setValue("option-1");
//        value.setSelected(true);
        values.add(value);

        schema1.setValues((ArrayList<Value>) values);
        return schema1;
    }

    private Schema addRadioBtnSchema() {
        Schema schema1 = new Schema();
        schema1.setType("radio-group");
        schema1.setLabel("Radio Group");
        schema1.setName("radio-group-1528272120480");

        List<Value> values = new ArrayList<>();
        Value value = new Value();
        value.setLabel("Option 1");
        value.setValue("option-1");
        value.setSelected(true);
        values.add(value);

        value = new Value();
        value.setLabel("Option 2");
        value.setValue("option-1");
//        value.setSelected(true);
        values.add(value);

        schema1.setValues((ArrayList<Value>) values);
        return schema1;
    }

    private Schema addProvinceSchema() {
        Schema schema1 = new Schema();
        schema1.setType("select");
        schema1.setLabel("Province");
        schema1.setName("province");

        return schema1;
    }

    private Schema addZoneSchema() {
        Schema schema1 = new Schema();
        schema1.setType("select");
        schema1.setLabel("Zone");
        schema1.setName("zone");

        return schema1;
    }

    private Schema addDistrictSchema() {
        Schema schema1 = new Schema();
        schema1.setType("select");
        schema1.setLabel("District");
        schema1.setName("district");

        return schema1;
    }


    @NonNull
    private Schema addAutoCompleteTextViewSchema() {
        Schema schema1 = new Schema();
        schema1.setType("autocomplete");
        schema1.setLabel(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE_SUBTYPE_MUNICIPALITY);
        schema1.setName("autocomplete-1528378222032");

//        List<Value> values = new ArrayList<>();
//        Value value = new Value();
//        value.setLabel("Option 1");
//        value.setValue("option-1");
//        values.add(value);
//
//        value = new Value();
//        value.setLabel("Option 2");
//        value.setValue("option-2");
//        values.add(value);

//        schema1.setValues((ArrayList<Value>) values);
        return schema1;
    }

    @NonNull
    private Schema addDateFieldTextViewSchema() {
        Schema schema1 = new Schema();
        schema1.setType("date");
        schema1.setLabel("Date Field");
        schema1.setName("date-1528386458420");

        return schema1;
    }

    @NonNull
    private Schema addNumberTextViewSchema() {
        Schema schema1 = new Schema();
        schema1.setType("number");
        schema1.setLabel("Number");
        schema1.setName("number-1528388689461");

        return schema1;
    }

    @NonNull
    private Schema addHeaderTextViewSchema() {
        Schema schema1 = new Schema();
        schema1.setType("header");
        schema1.setLabel("Header");
        schema1.setSubtype("h1");

        return schema1;
    }

    private Schema addCloneableComponent() {
        Schema schema1 = new Schema();
        schema1.setType("cloneable");
        schema1.setLabel("Maxon");
        schema1.setName("cloneable");

        ArrayList<Schema> clonedComponentArray = new ArrayList<>();

        Schema clonedComponent = addNumberTextViewSchema();
        clonedComponentArray.add(clonedComponent);

//        clonedComponent = addRadioBtnSchema();
//        clonedComponentArray.add(clonedComponent);
//
//        clonedComponent = addCheckBoxSchema();
//        clonedComponentArray.add(clonedComponent);

        schema1.setCloneable(clonedComponentArray);
        return schema1;
    }

    @Override
    public void manageCloneableComponent(Schema schema) {
        if(schema.getName() != null) {
            if(schema.getType().equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_HEADER)) {
                cloneableComponent.add(schema);
            }
            for (Schema outerSchema : formDetails.getData().getSchema()) {
                if (outerSchema.getName() != null && outerSchema.getName().equalsIgnoreCase(schema.getName())) {
                    cloneableComponent.add(outerSchema);
                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(locationManager != null) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();

        longitudeTextView.setText(String.valueOf(longitude));
        latitudeTextView.setText(String.valueOf(latitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
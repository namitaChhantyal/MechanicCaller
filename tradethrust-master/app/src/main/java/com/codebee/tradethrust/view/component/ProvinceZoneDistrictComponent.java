package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.model.province.ListHolder;
import com.codebee.tradethrust.model.province.Province;
import com.codebee.tradethrust.network.ProvinceListAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.spinner.FilterableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProvinceZoneDistrictComponent extends LinearLayout {

    private Context context;

    private float density;
    private Retrofit retrofit;
    private SharedPreferences preferences;

    private TTFilterableSpinner provinceSpinner;
    private TTFilterableSpinner zoneSpinner;
    private TTFilterableSpinner districtSpinner;
    private TTFilterableSpinner municipalitySpinner;

    private LinearLayout provinceContainer;
    private LinearLayout zoneContainer;
    private LinearLayout districtContainer;
    private LinearLayout municipalityContainer;

    public ProvinceZoneDistrictComponent(Context context) {
        super(context);

        initLayout(context);
    }

    private void initLayout(final Context context) {
        this.context = context;

        density = context.getResources().getDisplayMetrics().density;

        TradeThrustApplication application = (TradeThrustApplication) context.getApplicationContext();
        retrofit = application.getRetrofit();
        preferences = application.getPreferences();

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        setProvinceLayout();
        setZoneLayout();
        setDistrictLayout();
        setMunicipalityLayout();

        ProvinceListAPI provinceListAPI = retrofit.create(ProvinceListAPI.class);
        Call<Province> provinceListHolder = provinceListAPI.getProvinceList(getHeaderInfo());

        provinceListHolder.enqueue(new Callback<Province>() {
            @Override
            public void onResponse(Call<Province> call, Response<Province> response) {

                Province province = response.body();

                if(province != null) {

                    List<ListHolder> provinceList = province.getProvinces();

                    ListHolder defaultHolder = new ListHolder();
                    defaultHolder.setId(-1);
                    defaultHolder.setLabel("Select Province");

                    provinceList.add(0 ,defaultHolder);

                    provinceSpinner.notifyDataSetChanged(provinceList);

                    setComponentEnabled(zoneSpinner, zoneContainer,false);
                    setComponentEnabled(districtSpinner, districtContainer, false);
                    setComponentEnabled(municipalitySpinner, municipalityContainer,false);


                }else {
                    Toast.makeText(context, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private Map<String, String> getHeaderInfo() {

        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        headersInfo.put("Serializer", "Index::RecordSerializer");

        return headersInfo;
    }

    private void setProvinceLayout() {

        provinceContainer = new LinearLayout(context);
        LinearLayout.LayoutParams provinceContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        provinceContainerParams.bottomMargin = (int) (16 * density);
        provinceContainer.setLayoutParams(provinceContainerParams);
        provinceContainer.setOrientation(LinearLayout.VERTICAL);
        provinceContainer.setPadding((int) (2 * density), 0, (int) (2 * density), 0);

        provinceSpinner = new TTFilterableSpinner(context);

        provinceContainer.addView(provinceSpinner);

        this.addView(provinceContainer);
        provinceContainer.setVisibility(View.GONE);

    }

    private void setZoneLayout() {
        if(provinceSpinner != null){

            zoneContainer = new LinearLayout(context);
            LinearLayout.LayoutParams zoneContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            zoneContainerParams.bottomMargin = (int) (16 * density);
            zoneContainer.setLayoutParams(zoneContainerParams);
            zoneContainer.setOrientation(LinearLayout.VERTICAL);
            zoneContainer.setPadding((int) (2 * density), 0, (int) (2 * density), 0);

            zoneSpinner = new TTFilterableSpinner(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            zoneSpinner.setLayoutParams(params);

            zoneContainer.addView(zoneSpinner);

            this.addView(zoneContainer);
            zoneContainer.setVisibility(View.GONE);
        }
    }

    private void setDistrictLayout() {
        if(zoneSpinner != null){

            districtContainer = new LinearLayout(context);
            LinearLayout.LayoutParams districtContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            districtContainerParams.bottomMargin = (int) (16 * density);
            districtContainer.setLayoutParams(districtContainerParams);
            districtContainer.setOrientation(LinearLayout.VERTICAL);
            districtContainer.setPadding((int) (2 * density), 0, (int) (2 * density), 0);

            districtSpinner = new TTFilterableSpinner(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            districtSpinner.setLayoutParams(params);

            districtContainer.addView(districtSpinner);

            this.addView(districtContainer);
            districtContainer.setVisibility(View.GONE);
        }
    }

    private void setMunicipalityLayout() {
        if(districtSpinner != null){

            municipalityContainer = new LinearLayout(context);
            LinearLayout.LayoutParams districtContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            districtContainerParams.bottomMargin = (int) (16 * density);
            municipalityContainer.setLayoutParams(districtContainerParams);
            municipalityContainer.setOrientation(LinearLayout.VERTICAL);
            municipalityContainer.setPadding((int) (2 * density), 0, (int) (2 * density), 0);

            municipalitySpinner = new TTFilterableSpinner(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            municipalitySpinner.setLayoutParams(params);

            municipalityContainer.addView(municipalitySpinner);

            this.addView(municipalityContainer);
            municipalityContainer.setVisibility(View.GONE);
        }
    }

    public void setProvince(Schema schema){
        provinceSpinner.setSchema(schema);
        provinceContainer.setVisibility(View.VISIBLE);

        provinceSpinner.setOnItemSelectedListener(new FilterableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ListHolder holder) {
                int selectedProvinceId = provinceSpinner.getSelectedItem().getId();
                if(selectedProvinceId != -1) {
                    ProvinceListAPI zoneListAPI = retrofit.create(ProvinceListAPI.class);
                    Call<Province> zoneListCall = zoneListAPI.getZoneList(selectedProvinceId, getHeaderInfo());
                    zoneListCall.enqueue(new Callback<Province>() {
                        @Override
                        public void onResponse(Call<Province> call, Response<Province> response) {

                            Province zone = response.body();

                            if (zone != null) {

                                List<ListHolder> zoneList = zone.getProvinces();
                                zoneSpinner.clear();

                                ListHolder defaultHolder = new ListHolder();
                                defaultHolder.setId(-1);
                                defaultHolder.setLabel("Select Zone");
                                zoneList.add(0, defaultHolder);

                                zoneSpinner.notifyDataSetChanged(zoneList);

                                setComponentEnabled(zoneSpinner, zoneContainer, true);
                                setComponentEnabled(districtSpinner, districtContainer,false);
                                setComponentEnabled(municipalitySpinner, municipalityContainer, false);

                            } else {
                                Toast.makeText(context, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Province> call, Throwable t) {


                        }
                    });
                }else {
                    setComponentEnabled(zoneSpinner, zoneContainer, false);
                    setComponentEnabled(districtSpinner, districtContainer, false);
                    setComponentEnabled(municipalitySpinner, municipalityContainer,false);
                }
            }
        });
    }

    public void setZone(Schema schema){
        zoneSpinner.setSchema(schema);
        zoneContainer.setVisibility(View.VISIBLE);

        zoneSpinner.setOnItemSelectedListener(new FilterableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ListHolder holder) {
                int selectedZoneId = zoneSpinner.getSelectedItem().getId();
                if(selectedZoneId != -1) {
                    ProvinceListAPI districtListAPI = retrofit.create(ProvinceListAPI.class);
                    Call<Province> districtListCall = districtListAPI.getDistrictList(selectedZoneId, getHeaderInfo());
                    districtListCall.enqueue(new Callback<Province>() {
                        @Override
                        public void onResponse(Call<Province> call, Response<Province> response) {

                            Province district = response.body();
                            districtSpinner.clear();

                            if (district != null) {

                                List<ListHolder> districtList = district.getProvinces();

                                ListHolder defaultHolder = new ListHolder();
                                defaultHolder.setId(-1);
                                defaultHolder.setLabel("Select District");

                                districtList.add(0, defaultHolder);

                                districtSpinner.notifyDataSetChanged(districtList);

                                setComponentEnabled(districtSpinner, districtContainer, true);

                                setComponentEnabled(municipalitySpinner, municipalityContainer, false);

                            } else {
                                Toast.makeText(context, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Province> call, Throwable t) {

                        }
                    });
                }else {
                    setComponentEnabled(districtSpinner, districtContainer, false);
                    setComponentEnabled(municipalitySpinner, municipalityContainer,false);
                }
            }
        });

    }

    public void setDistrict(Schema schema){
        districtSpinner.setSchema(schema);
        districtContainer.setVisibility(View.VISIBLE);
        districtSpinner.setOnItemSelectedListener(new FilterableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ListHolder holder) {
                int selectedDistrictId = districtSpinner.getSelectedItem().getId();
                if(selectedDistrictId != -1) {
                    ProvinceListAPI districtListAPI = retrofit.create(ProvinceListAPI.class);
                    Call<Province> districtListCall = districtListAPI.getMunicipalityList(selectedDistrictId, getHeaderInfo());
                    districtListCall.enqueue(new Callback<Province>() {
                        @Override
                        public void onResponse(Call<Province> call, Response<Province> response) {
                            municipalitySpinner.clear();
                            Province municipality = response.body();
                            if (municipality != null) {
                                List<ListHolder> municipalityList = municipality.getProvinces();

                                ListHolder defaultHolder = new ListHolder();
                                defaultHolder.setId(-1);
                                defaultHolder.setLabel("Select Municipality");

                                municipalityList.add(0, defaultHolder);

                                municipalitySpinner.notifyDataSetChanged(municipalityList);

                                setComponentEnabled(municipalitySpinner, municipalityContainer, true);


                            } else {
                                Toast.makeText(context, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Province> call, Throwable t) {

                        }
                    });
                }else {
                    setComponentEnabled(municipalitySpinner, municipalityContainer,false);
                }
            }
        });

    }

    public void setMunicipality(Schema schema) {
        municipalitySpinner.setSchema(schema);
        municipalityContainer.setVisibility(View.VISIBLE);
        municipalitySpinner.setOnItemSelectedListener(new FilterableSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ListHolder holder) {

            }
        });
    }

    public Value getSelectedProvinceValue() {
        if(provinceSpinner != null && provinceSpinner.getSelectedItem() != null && provinceSpinner.getSelectedItem().getId() != -1) {
            String selectedProvinceName = provinceSpinner.getSelectedItem().getLabel();
            Value selectedProvinceValue = new Value();
            selectedProvinceValue.setLabel(selectedProvinceName);
            selectedProvinceValue.setValue(selectedProvinceName);
            return selectedProvinceValue;
        }
        return null;
    }

    public Value getSelectedZoneValue() {
        if(zoneSpinner != null && zoneSpinner.getSelectedItem() != null && zoneSpinner.getSelectedItem().getId() != -1) {
            String selectedZoneName = zoneSpinner.getSelectedItem().getLabel();
            Value selectedZoneValue = new Value();
            selectedZoneValue.setLabel(selectedZoneName);
            selectedZoneValue.setValue(selectedZoneName);
            return selectedZoneValue;
        }
        return null;
    }

    public Value getSelectedDistrictValue() {
        if(districtSpinner != null && districtSpinner.getSelectedItem() != null && districtSpinner.getSelectedItem().getId() != -1) {
            String selectedDistrictName = districtSpinner.getSelectedItem().getLabel();
            Value selectedDistrictValue = new Value();
            selectedDistrictValue.setLabel(selectedDistrictName);
            selectedDistrictValue.setValue(selectedDistrictName);
            return selectedDistrictValue;
        }
        return null;
    }

    public Value getSelectedMunicipality() {
        if(municipalitySpinner != null && municipalitySpinner.getSelectedItem() != null && municipalitySpinner.getSelectedItem().getId() != -1) {
            String selectedMunicipalityName = municipalitySpinner.getSelectedItem().getLabel();
            Value selectedMunicipalityValue = new Value();
            selectedMunicipalityValue.setLabel(selectedMunicipalityName);
            selectedMunicipalityValue.setValue(selectedMunicipalityName);
            return selectedMunicipalityValue;
        }
        return null;
    }

    public void setComponentEnabled(TTFilterableSpinner spinner, ViewGroup spinnerContainer, boolean enabled) {
        if(enabled) {
            spinner.setEnabled(true);
            spinner.setVisibility(View.VISIBLE);
            spinnerContainer.setVisibility(View.VISIBLE);
        }else {
            spinner.clear();
            spinner.notifyDataSetChanged(new ArrayList<ListHolder>());
            spinner.setEnabled(false);
            spinner.setVisibility(View.GONE);
            spinnerContainer.setVisibility(View.GONE);
        }
    }

}

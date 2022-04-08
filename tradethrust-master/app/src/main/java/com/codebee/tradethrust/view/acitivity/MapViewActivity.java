package com.codebee.tradethrust.view.acitivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.map_pos.Data;
import com.codebee.tradethrust.model.map_pos.Datum;
import com.codebee.tradethrust.model.map_pos.MapPOS;
import com.codebee.tradethrust.network.MapPosAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.CustomKMLStyler;
import com.codebee.tradethrust.utils.GPSTracker;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.interfaces.OnItemSelectedListener;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String[] PERMISSIONS =
            {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };
    private static final int RC_CAMERA_PERM = 100;

    @BindView(R.id.map)
    public MapView map = null;

    @BindView(R.id.main_container)
    public LinearLayout mainContainer;

    @BindView(R.id.create_pos_btn)
    public Button createPOSBtn;

    private String wkt;
    private String formType;
    private Long formId;
    private int taskId;
    private double markerLat;
    private double markerLong;

    protected AlertDialog b;
    protected AlertDialog.Builder dialogBuilder;

    private SharedPreferences preferences;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private GPSTracker gps;
    private Marker clickedMarker;
    private Double centerLatOfBit;
    private Double centerLongOfBit;

    private Geometry bitGeometry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();

        setContentView(R.layout.activity_map_view);

        ButterKnife.bind(this);

        formId = getIntent().getLongExtra("formId", 0L);
        taskId = getIntent().getIntExtra("taskId", 0);
        centerLatOfBit = getIntent().getDoubleExtra("centerLatOfBit", -1);
        centerLongOfBit = getIntent().getDoubleExtra("centerLongOfBit", -1);
        formType = getIntent().getStringExtra("formType");

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();
        preferences = application.getPreferences();
        okHttpClient = application.okHttpClient();
        gson = application.gson();

        map.setBuiltInZoomControls(false);

        if(formType.equals(ThrustConstant.FORM_TYPE_REVISIT_FORM)) {
            createPOSBtn.setVisibility(View.GONE);
        }

        grantPermissionAndSetView();
    }

    private void drawMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        wkt = getIntent().getStringExtra("wkt");

        double latitude = 27.708777961505447;
        double longitude = 85.31455636024475;

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }

        IMapController mapController = map.getController();
        mapController.setZoom(19);

        GeoPoint p = new GeoPoint(latitude, longitude);

        Marker marker = new Marker(map);
        marker.setPosition(p);
        marker.setTitle("You are here.");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        testBits();
        testTouch();

        GeoPoint startPoint;
        if(centerLatOfBit == -1 || centerLongOfBit == -1) {
            startPoint = new GeoPoint(latitude, longitude);
            mapController.setCenter(startPoint);
        }else {
            startPoint = new GeoPoint(bitGeometry.getCentroid().getY(), bitGeometry.getCentroid().getX());
            mapController.setCenter(startPoint);
        }

    }

    private void testTouch() {

        MapEventsReceiver eventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                map.getOverlays().remove(clickedMarker);

                clickedMarker = new Marker(map);
                clickedMarker.setPosition(p);
                clickedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(clickedMarker);

                markerLat = p.getLatitude();
                markerLong = p.getLongitude();
                createPOSBtn.setEnabled(true);

                map.invalidate();

                return true;

            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), eventsReceiver);
        map.getOverlays().add(OverlayEvents);
    }

    private void testBits() {

//        String wktString = "LINESTRING (85.31455636024475 27.708777961505447, 85.31543612480165 27.71367906650603, 85.31788229942322 27.71328014758761, 85.31736731529237 27.709860782724263, 85.31717419624329 27.709328871889696, 85.31455636024475 27.708777961505447)";
//        String wktString = "POLYGON((-274.6827721595764 27.71810506898258,-274.68401670455927 27.71681337866697,-274.6846175193786 27.713736054803846,-274.68221426010126 27.71324215523356,-274.6798968315124 27.713660070400152,-274.6774935722351 27.713660070400152,-274.6763348579407 27.71339412457033,-274.67513322830195 27.71730726206482,-274.67663526535034 27.717801143225927,-274.6827721595764 27.71810506898258))";

        String geoJsonString = convertWKTToGeoJson(wkt);//"{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[85.31455636024475,27.708777961505447],[85.31543612480165,27.71367906650603],[85.31788229942322,27.71328014758761],[85.31736731529237,27.709860782724263],[85.31717419624329,27.709328871889696],[85.31455636024475,27.708777961505447],[85.31455636024475,27.708777961505447]]]}}";

        if(geoJsonString != null) {
            KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseGeoJSON(geoJsonString);

            CustomKMLStyler styler = new CustomKMLStyler();
            FolderOverlay geoJsonOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, styler, kmlDocument);

            map.getOverlayManager().add(geoJsonOverlay);

//            List<GeoPoint> geoPoints = getGeoPointsFrom(bitGeometry);
//            BoundingBox bb = BoundingBox.fromGeoPoints(geoPoints);
//            map.zoomToBoundingBox(bb,false);


            map.invalidate();
        }
    }

    private List<GeoPoint> getGeoPointsFrom(Geometry bitGeometry) {
        List<GeoPoint> geoPointList = new ArrayList<>();
        for(Coordinate coordinate:bitGeometry.getCoordinates()) {
            GeoPoint geoPoint = new GeoPoint(coordinate.getOrdinate(1), coordinate.getOrdinate(0));
            geoPointList.add(geoPoint);
        }
        return geoPointList;
    }

    private String convertWKTToGeoJson(String wktString) {

        try {
            WKTReader wktReader = new WKTReader();
            Geometry geometry = wktReader.read(wktString);

            if(wktString.contains("((")) {

                GeoJsonPolygonGeometryClass geoJson = new GeoJsonPolygonGeometryClass();
                geoJson.setType(geometry.getGeometryType());

                for (int lineIndex = 0; lineIndex < geometry.getNumGeometries(); lineIndex++) {
                    Geometry lineGeometry = geometry.getGeometryN(lineIndex);
                    bitGeometry = lineGeometry;
                    double[][] geoCoordinates = new double[lineGeometry.getCoordinates().length][];
                    int index = 0;
                    for (Coordinate coordinate : lineGeometry.getCoordinates()) {
                        double[] geoCoordinate = new double[]{coordinate.getOrdinate(0), coordinate.getOrdinate(1)};
                        geoCoordinates[index] = geoCoordinate;
                        index++;
                    }

                    double[][][] finalCoordinateFormat = new double[1][][];
                    finalCoordinateFormat[0] = geoCoordinates;

                    geoJson.setCoordinates(finalCoordinateFormat);
                }

                GeoPolygonJsonClass geo = new GeoPolygonJsonClass();
                geo.setType("Feature");
                geo.setGeometry(geoJson);
                return new Gson().toJson(geo);
            }else {
                GeoJsonLineStringGeometryClass geoJson = new GeoJsonLineStringGeometryClass();
                geoJson.setType(geometry.getGeometryType());

                for (int lineIndex = 0; lineIndex < geometry.getNumGeometries(); lineIndex++) {
                    Geometry lineGeometry = geometry.getGeometryN(lineIndex);
                    bitGeometry = lineGeometry;
                    double[][] geoCoordinates = new double[lineGeometry.getCoordinates().length][];
                    int index = 0;
                    for (Coordinate coordinate : lineGeometry.getCoordinates()) {
                        double[] geoCoordinate = new double[]{coordinate.getOrdinate(0), coordinate.getOrdinate(1)};
                        geoCoordinates[index] = geoCoordinate;
                        index++;
                    }

                    geoJson.setCoordinates(geoCoordinates);
                }

                GeoLineStringJsonClass geo = new GeoLineStringJsonClass();
                geo.setType("Feature");
                geo.setGeometry(geoJson);
                return new Gson().toJson(geo);
            }

        }catch(ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    @OnClick(R.id.create_pos_btn)
    public void openFormActivity(View view){
        Intent intent = new Intent(MapViewActivity.this, TaskFormActivity.class);
        intent.putExtra("formId", formId);
        intent.putExtra("taskId", taskId);
        intent.putExtra("markerLat", markerLat);
        intent.putExtra("markerLong", markerLong);
        startActivity(intent);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void grantPermissionAndSetView() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {

            drawMap();

            drawPOSOnMap();

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access your location, storage and camera to function properly.", RC_CAMERA_PERM, PERMISSIONS);
        }
    }

    private void drawPOSOnMap() {

        showProgressDialog("Retrieving Map Data...");

        Map<String, String> headersInfo = getHeaderInfo();

        MapPosAPI mapPosAPI = getRetrofit().create(MapPosAPI.class);

        Call<MapPOS> mapPOSCall = mapPosAPI.getTaskDetails(String.valueOf(taskId), String.valueOf(formId), headersInfo);

        mapPOSCall.enqueue(new Callback<MapPOS>() {
            @Override
            public void onResponse(Call<MapPOS> call, Response<MapPOS> response) {
                MapPOS mapPOS = response.body();
                if(mapPOS == null){
                    Toast.makeText(MapViewActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }else {
                    for(Datum data: mapPOS.getData()){
                        drawMarkerOnMap(data);
                    }
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<MapPOS> call, Throwable t) {
                hideProgressDialog();
            }
        });

    }

    private void drawMarkerOnMap(Datum posDatum) {

        String status = posDatum.getStatus();

        int color = R.color.pending_color;

        if(status.equals(ThrustConstant.BIT_STATUS_APPROVED)) {
            color = R.color.approved_color;
        } else if(status.equals(ThrustConstant.BIT_STATUS_REJECTED)) {
            color = R.color.rejected_color;
        }

        GeoPoint posPoint = new GeoPoint(Double.valueOf(posDatum.getData().getPosLatitude()), Double.valueOf(posDatum.getData().getPosLongitude()));

        Marker posMarker = new Marker(map);
        posMarker.setPosition(posPoint);

        Drawable newMarker = this.getResources().getDrawable(R.drawable.ic_marker);
        newMarker.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(color), PorterDuff.Mode.MULTIPLY));
        posMarker.setIcon(newMarker);

        posMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(posMarker);
        map.invalidate();
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

    private Retrofit getRetrofit() {

        String mBaseUrl = ThrustConstant.BASE_URL;

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
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

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @OnClick(R.id.close_detail_btn)
    public void closeDetailBtn(View view) {
        super.onBackPressed();
    }

    class GeoPolygonJsonClass {
        @SerializedName("type")
        private String type;
        @SerializedName("geometry")
        private GeoJsonPolygonGeometryClass geometry;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public GeoJsonPolygonGeometryClass getGeometry() {
            return geometry;
        }

        public void setGeometry(GeoJsonPolygonGeometryClass geometry) {
            this.geometry = geometry;
        }
    }

    class GeoJsonPolygonGeometryClass {

        @SerializedName("type")
        private String type;
        @SerializedName("coordinates")
        private double[][][] coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double[][][] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[][][] coordinates) {
            this.coordinates = coordinates;
        }
    }

    class GeoLineStringJsonClass {
        @SerializedName("type")
        private String type;
        @SerializedName("geometry")
        private GeoJsonLineStringGeometryClass geometry;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public GeoJsonLineStringGeometryClass getGeometry() {
            return geometry;
        }

        public void setGeometry(GeoJsonLineStringGeometryClass geometry) {
            this.geometry = geometry;
        }
    }

    class GeoJsonLineStringGeometryClass {

        @SerializedName("type")
        private String type;
        @SerializedName("coordinates")
        private double[][] coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double[][] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[][] coordinates) {
            this.coordinates = coordinates;
        }
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MapViewActivity.this);
    }

}

package com.example.platform.Activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.platform.R;

import com.example.platform.databinding.ActivitySzczegolyPolaBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SzczegolyPolaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivitySzczegolyPolaBinding binding;
    private String URL = "http://sensor332.atwebpages.com/pins/retrievePoligon.php";
    private String URL1 = "http://sensor332.atwebpages.com/pins/retrieveSensors.php";
    public static String sensorAlfa = "x";
    public static String sensorBeta = "x";

    Polygon polygon = null;
    LatLng latLng;
    List<LatLng> latLngPolygonList = new ArrayList<>();
    List<Marker> markerPolygonList = new ArrayList<>();
    List<Marker> markerSensorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("SzczegolyPolaActivity");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding = ActivitySzczegolyPolaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // getSupportFragmentManager().beginTransaction().replace(R.id.map).commit();

        retrivePolygon();
        Delay(100);
        retriveSensors();
    }

    private void retrivePolygon() {

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "odp " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (sucess.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String latitude = object.getString("latitude");
                                    String longitude = object.getString("longitude");

                                    double latitudeD = Double.parseDouble(latitude);
                                    double longitudeD = Double.parseDouble(longitude);

                                    latLng = new LatLng(latitudeD, longitudeD);

                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                                    Marker marker = mMap.addMarker(markerOptions);

                                    latLngPolygonList.add(latLng);
                                    markerPolygonList.add(marker);

                                    if (polygon != null) {
                                        polygon.remove();
                                    }
                                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngPolygonList).clickable(true);
                                    polygon = mMap.addPolygon(polygonOptions);

                                    polygon.setFillColor(0x3C00FF00);
                                    polygon.setStrokeColor(Color.GREEN);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internet Connection ERROR", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("userId", MainActivity.userId.trim());
                data.put("regionId", MainPageActivity.PokazPolaActivity.regionId.trim());
                Log.d(TAG, "UserIdko " + MainActivity.userId);
                return data;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void retriveSensors() {
        sensorAlfa = "";
        sensorBeta = "";
        StringRequest request = new StringRequest(Request.Method.POST, URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (sucess.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String nazwa_sensora = object.getString("nazwa_sensora");
                                    String latitude = object.getString("latitude");
                                    String longitude = object.getString("longitude");
                                    String wilgotnosc = object.getString("wilgotnosc");
                                    String temperatura = object.getString("temperatura");
                                    String swiatlo = object.getString("naslonecznienie");
                                    String data = object.getString("data");

                                    double latitudeDSensor = Double.parseDouble(latitude);
                                    double longitudeDSensor = Double.parseDouble(longitude);

                                    latLng = new LatLng(latitudeDSensor, longitudeDSensor);

                                    PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngPolygonList).clickable(true);

                                    if (isPointOnPolygon(latLng, polygonOptions, true, 1)) {
                                        if (nazwa_sensora.equals("ALFA")) {
                                            sensorAlfa = "ALFA";
                                        }
                                        if (nazwa_sensora.equals("BETA")) {
                                            sensorBeta = "BETA";
                                        }
                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).snippet("Data Pomiaru: " + data).title("Sensor: " + nazwa_sensora + " " + " Wilgotność:" + wilgotnosc + "%|" + "Temp:" + temperatura + "\u2103|" + "Swiatło:" + swiatlo + "%");

                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                                        Marker marker = mMap.addMarker(markerOptions);

                                        markerSensorList.add(marker);

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internet Connection ERROR", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("userId", MainActivity.userId.trim());

                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public static boolean isPointOnPolygon(LatLng point, PolygonOptions polygon, boolean geodesic, double tolerance) {

        boolean onPolygon = PolyUtil.containsLocation(point, polygon.getPoints(), geodesic) ||
                PolyUtil.isLocationOnEdge(point, polygon.getPoints(), geodesic, tolerance);

        if (onPolygon) {
            for (List<LatLng> hole : polygon.getHoles()) {
                if (PolyUtil.containsLocation(point, hole, geodesic)) {
                    onPolygon = false;
                    break;
                }
            }
        }

        return onPolygon;
    }

    private void Delay(int i) {
        try {
            Thread.sleep(i);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng warszawa = new LatLng(52, 21);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(warszawa, 6f));
    }

    public void btnDane(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Intent intent = new Intent(SzczegolyPolaActivity.this, DaneActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnWykres(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Intent intent = new Intent(SzczegolyPolaActivity.this, WykresActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainPageActivity.PokazPolaActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
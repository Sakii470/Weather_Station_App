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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.platform.R;
import com.example.platform.databinding.ActivityDodajPoleBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DodajPoleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDodajPoleBinding binding;
    private Button clear, send, send1;
    private String URL="http://sensor332.atwebpages.com/pins/latlon.php", lat, lon;
    private String URL1="http://sensor332.atwebpages.com/pins/addregion.php";
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    String currentTime = "";
    private String regionId="";

    Polygon polygon=null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    List<Double> LatListDoub = new ArrayList<>();
    List<Double> LonListDoub = new ArrayList<>();
    List<String> LatListStr = new ArrayList<>();
    List<String> LonListStr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DodajPoleActivity");
        binding = ActivityDodajPoleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findBy();
        btnListener();
    }
    private void findBy(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        clear = findViewById(R.id.btn_clear_polygon);
        send = findViewById(R.id.btn_send_data);
    }
    private void btnListener(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shake = AnimationUtils.loadAnimation(DodajPoleActivity.this, R.anim.shake);
                view.startAnimation(shake);
                if (polygon != null) {
                    polygon.remove();
                }
                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shake = AnimationUtils.loadAnimation(DodajPoleActivity.this, R.anim.shake);
                view.startAnimation(shake);
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("failure")){
                            Toast.makeText(getApplicationContext(), "Add region failure!", Toast.LENGTH_SHORT).show();
                        }
                        else if (response.equals(response)){

                            regionId=response.trim();
                            Toast.makeText(getApplicationContext(), "Successfuly region added!", Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < latLngList.size(); i++) {
                                LatListDoub.add(latLngList.get(i).latitude);
                                LonListDoub.add(latLngList.get(i).longitude);
                                LatListStr.add(LatListDoub.get(i).toString());
                                LonListStr.add(LonListDoub.get(i).toString());
                            }
                            for (int j = 0; j < latLngList.size(); j++) {
                                int finalJ = j;
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.trim().equals("success"))
                                            Log.d(TAG, finalJ + " Sukcess " + LatListStr.get(finalJ));

                                        else if (response.trim().equals("failure"))
                                            Log.d(TAG, finalJ + " failure " + LatListStr.get(finalJ));
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("userId", MainActivity.userId);
                                        data.put("regionId", regionId);
                                        data.put("latitude", LatListStr.get(finalJ));
                                        data.put("longitude", LonListStr.get(finalJ));
                                        data.put("date", currentTime);

                                        return data;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                requestQueue.add(stringRequest);
                                currentTime = dateFormat.format(new Date());
                                Log.d(TAG, "Data" + currentTime );
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();

                        data.put("userId", MainActivity.userId);

                        return data;
                    }
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                requestQueue1.add(stringRequest1);
                currentTime = dateFormat.format(new Date());
            }
        });
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = mMap.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);
                if(polygon != null){
                    polygon.remove();
                }
                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList).clickable(true);
                polygon = mMap.addPolygon(polygonOptions);
                polygon.setFillColor(0x3C00FF00);
                polygon.setStrokeColor(Color.GREEN);
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng warszawa = new LatLng(52, 21);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(warszawa,6f));
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPageActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}




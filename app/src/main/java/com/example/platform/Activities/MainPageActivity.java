package com.example.platform.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.platform.Adapters.RegionAdapter;
import com.example.platform.Models.RegionModel;
import com.example.platform.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainPageActivity");
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void btnDodajPole(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Intent intent= new Intent(MainPageActivity.this, DodajPoleActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnPokazPola(View view) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);
        Intent intent= new Intent(MainPageActivity.this, PokazPolaActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public static class PokazPolaActivity extends AppCompatActivity {

        //variable declaration and initialization
        ListView listView;
        RegionAdapter adapter;
        public static ArrayList<RegionModel> regionArrayList = new ArrayList<>();
        String URL = "http://sensor332.atwebpages.com/pins/retrieveRegions.php";
        String URL1 = "http://sensor332.atwebpages.com/pins/delete.php";
        RegionModel region;
        public static String regionId;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTitle("PokazPolaActivity");
            setContentView(R.layout.activity_pokaz_pola);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Initialize variable.Views are find by Id (XML).
            listView = findViewById(R.id.myListView);
    //Created an adapter to convert the array to views
            adapter = new RegionAdapter(this, regionArrayList);
    //Attach the adapter to a ListView
            listView.setAdapter(adapter);

            listViewListener();
            retriveRegion();

        }
    // method describe what activity doing after click on listView row.
        private void listViewListener() {
    // implementation listener on listView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    Animation shake = AnimationUtils.loadAnimation(PokazPolaActivity.this, R.anim.shake);
                    view.startAnimation(shake);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    CharSequence[] dialogItem = {"Pokaż Szczegóły Pola", "Usuń Pole"};
                    regionId = regionArrayList.get(position).getRegionId();
    // Define what activity doing after to item click.
                    builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            switch (i) {
                                case 0:
            //PokażPolaActivity create intent to SzczegolyPolaActivity.
                                    startActivity(new Intent(getApplicationContext(), SzczegolyPolaActivity.class)
                                            .putExtra("position", position));
                                    break;
                                case 1:
            //Method call - method describe delete check region from the database.
                                    deleteRegion(regionArrayList.get(position).getRegionId());
                                    finish();
                                    startActivity(getIntent());
                                    break;
                            }
                        }
                    });
                    builder.create().show();
                }
            });
        }
    //method describe delete check region from the database.
        private void deleteRegion(final String id) {
            StringRequest request = new StringRequest(Request.Method.POST, URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Data Deleted")) {
                                Toast.makeText(PokazPolaActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PokazPolaActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PokazPolaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

        private void retriveRegion() {
            StringRequest request = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            regionArrayList.clear();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String sucess = jsonObject.getString("success");
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                if (sucess.equals("1")) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String id = object.getString("id");

                                        region = new RegionModel(id);
                                        regionArrayList.add(region);
                                        adapter.notifyDataSetChanged();

                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    data.put("userId", MainActivity.userId.trim());
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            Intent myIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivityForResult(myIntent, 0);
            return true;
        }
    }
}

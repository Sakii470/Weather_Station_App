package com.example.platform.Activities;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.platform.Adapters.PomiarAdapter;
import com.example.platform.Models.PomiarModel;
import com.example.platform.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DaneActivity extends AppCompatActivity {

    private EditText etData_Od, etData_Do;
    private String data_Od, data_Do;
    private CheckBox checkBoxWilgotnosc, checkBoxTemperatura, checkBoxSwiatlo;
    private TextView zmienna, tvKomunikat;
    TextView tvSrednia;
    String URL = "http://sensor332.atwebpages.com/sensors/retrieveDataSensors.php";
    ListView listView;
    PomiarAdapter adapter;
    PomiarModel pomiarModel;
    List<PomiarModel> pomiar_list = new ArrayList<>();
    float average = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DaneActivity");
        setContentView(R.layout.activity_dane);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findBy();
        checkBoxListener();
        implementationCalendarEditTextListener();
        adapterList();
        boolean isChecked = checkBoxWilgotnosc.isChecked();
        updateWilgotnosc(isChecked);

    }

    private void findBy() {
        etData_Od = findViewById(R.id.etData_Od);
        etData_Do = findViewById(R.id.etData_Do);
        tvSrednia = findViewById(R.id.tvSrednia);
        checkBoxWilgotnosc = findViewById(R.id.checkBoxWilgotnosc);
        checkBoxTemperatura = findViewById(R.id.checkBoxTemperatura);
        checkBoxSwiatlo = findViewById(R.id.checkBoxSwiatlo);
        zmienna = findViewById(R.id.zmienna);
        tvKomunikat = findViewById(R.id.tvKomunikat);
    }

    private void checkBoxListener() {
        checkBoxWilgotnosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateWilgotnosc(isChecked);
                retriveSensorData();
            }
        });

        checkBoxTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateTemperatura(isChecked);
                retriveSensorData();
            }
        });

        checkBoxSwiatlo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateSwiatlo(isChecked);
                retriveSensorData();
            }
        });
    }

    private void implementationCalendarEditTextListener() {
        etData_Od.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DaneActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        etData_Od.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        etData_Do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DaneActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.
                        etData_Do.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    private void adapterList() {
        listView = findViewById(R.id.sensors_list_view);
        adapter = new PomiarAdapter(this, pomiar_list);
        listView.setAdapter(adapter);
    }

    private void updateWilgotnosc(boolean isChecked) {
        if (isChecked) {
            tvSrednia.setText("");
            zmienna.setText("Wilgotnosc");
            checkBoxTemperatura.setChecked(false);
            checkBoxSwiatlo.setChecked(false);
            tvKomunikat.setText("Średnia wilgotność w tym okresie wynosi:");
        }
    }

    private void updateTemperatura(boolean isChecked) {
        if (isChecked) {
            tvSrednia.setText("");
            zmienna.setText("Temp");
            checkBoxWilgotnosc.setChecked(false);
            checkBoxSwiatlo.setChecked(false);
            tvKomunikat.setText("Średnia temperatura w tym okresie wynosi:");
        }
    }

    private void updateSwiatlo(boolean isChecked) {
        if (isChecked) {
            tvSrednia.setText("");
            zmienna.setText("Swiatło");
            checkBoxWilgotnosc.setChecked(false);
            checkBoxTemperatura.setChecked(false);
            tvKomunikat.setText("Średnie nasłonecznienie w tym okresie wynosi:");
        }

    }

    public void btnSzukaj(View view) {
        data_Od = etData_Od.getText().toString().trim();
        data_Do = etData_Do.getText().toString().trim();
        retriveSensorData();
    }

    private void retriveSensorData() {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pomiar_list.clear();
                average = 0;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (sucess.equals("1")) {
                        if (checkBoxWilgotnosc.isChecked()) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String data = object.getString("data");
                                String sensor_name = object.getString("nazwa_sensora");
                                String soil_moisture = object.getString("wilgotnosc");

                                pomiarModel = new PomiarModel(data, sensor_name, soil_moisture);
                                pomiar_list.add(pomiarModel);
                                adapter.notifyDataSetChanged();
                                Log.d(TAG, "Odp: " + response);

                                average += Float.parseFloat(soil_moisture);
                            }
                            average = average / pomiar_list.size();
                            tvSrednia.setText(String.format("%.2f", average) + " %");
                        }
                    }
                    if (checkBoxTemperatura.isChecked()) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String data = object.getString("data");
                            String sensor_name = object.getString("nazwa_sensora");
                            String temperature = object.getString("temperatura");

                            pomiarModel = new PomiarModel(data, sensor_name, temperature);
                            pomiar_list.add(pomiarModel);
                            adapter.notifyDataSetChanged();
                            average += Float.parseFloat(temperature);
                        }
                        average = average / pomiar_list.size();
                        tvSrednia.setText(String.format("%.2f", average) + "\u2103");
                    }
                    if (checkBoxSwiatlo.isChecked()) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String data = object.getString("data");
                            String sensor_name = object.getString("nazwa_sensora");
                            String light = object.getString("naslonecznienie");

                            pomiarModel = new PomiarModel(data, sensor_name, light);
                            pomiar_list.add(pomiarModel);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Odp: " + response);
                            average += Float.parseFloat(light);
                        }
                        average = average / pomiar_list.size();
                        tvSrednia.setText(String.format("%.2f", average) + " %");
                    }
                    if (pomiar_list == null || pomiar_list.isEmpty()) {
                        adapter.notifyDataSetChanged();
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
                data.put("sensorAlfa", SzczegolyPolaActivity.sensorAlfa);
                data.put("sensorBeta", SzczegolyPolaActivity.sensorBeta);
                data.put("data_Od", data_Od);
                data.put("data_Do", data_Do);
                Log.d(TAG, "UserIdko " + MainActivity.userId);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SzczegolyPolaActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
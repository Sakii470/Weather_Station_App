package com.example.platform.Activities;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static java.lang.Float.parseFloat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WykresActivity extends AppCompatActivity {

    private LineChart mChart;
    List<Entry> yValues = new ArrayList<>();
    List<Entry> zValues = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    int[] colorClassArray = new int[]{Color.RED, Color.BLUE};
    String[] legendName = {"Sensor Alfa", "Sensor Beta"};
    private CheckBox checkBoxWilgotnosc, checkBoxTemperatura, checkBoxSwiatlo;
    Description description = new Description();
    TextView tvDane;
    String userId = MainActivity.userId.trim();
    private String URL = "http://sensor332.atwebpages.com/sensors/retriveDataSensorsToChart.php";
    private String URL1 = "http://sensor332.atwebpages.com/sensors/retriveDataSensorsToChartWeeks.php";
    private String URL2 = "http://sensor332.atwebpages.com/sensors/retriveDataSensorsToChartDay.php";
    private String URL3 = "http://sensor332.atwebpages.com/sensors/retriveDataSensorsToChartMinute.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wykres);
        setTitle("WykresActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findBy();
        activeChart();
        StartRetriveData(URL3);
        checkBoxListener();
        boolean isChecked = checkBoxWilgotnosc.isChecked();
        updateWilgotnosc(isChecked);
    }

    private void findBy() {
        tvDane = findViewById(R.id.tvDane);
        checkBoxWilgotnosc = findViewById(R.id.checkBoxWilgotnosc);
        checkBoxTemperatura = findViewById(R.id.checkBoxTemperatura);
        checkBoxSwiatlo = findViewById(R.id.checkBoxSwiatlo);
    }

    private void activeChart() {
        mChart = (LineChart) findViewById(R.id.linechart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setNoDataText("Nie pobrano danych. Zmień zakres.");
        mChart.setDrawGridBackground(true);
        mChart.setBorderColor(Color.RED);
        mChart.setBorderWidth(5);
        description.setText("Pomiar wilgotosci gleby [%]");
        description.setTextColor(Color.BLUE);
        description.setTextSize(20);
        mChart.setDescription(description);
        Legend legend = mChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(15);
        legend.setFormToTextSpace(10);
        LegendEntry[] legendEntries = new LegendEntry[2];
        for (int i = 0; i < legendEntries.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colorClassArray[i];
            entry.label = String.valueOf(legendName[i]);
            legendEntries[i] = entry;
        }
        legend.setCustom(legendEntries);
    }

    private void checkBoxListener() {
        checkBoxWilgotnosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateWilgotnosc(isChecked);
            }
        });

        checkBoxTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateTemperatura(isChecked);
            }
        });

        checkBoxSwiatlo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                updateSwiatlo(isChecked);
            }
        });
    }

    private void updateWilgotnosc(boolean isChecked) {
        if (isChecked) {
            checkBoxTemperatura.setChecked(false);
            checkBoxSwiatlo.setChecked(false);
            description.setText("Pomiar wilgotności gleby w [%]");
            StartRetriveData(URL);
        }
    }

    private void updateTemperatura(boolean isChecked) {
        if (isChecked) {
            checkBoxWilgotnosc.setChecked(false);
            checkBoxSwiatlo.setChecked(false);
            description.setText("Pomiar temperatury w \u2103");
            StartRetriveData(URL);
        }
    }

    private void updateSwiatlo(boolean isChecked) {
        if (isChecked) {
            checkBoxWilgotnosc.setChecked(false);
            checkBoxTemperatura.setChecked(false);
            description.setText("Pomiar nasłonecznienia w [%]");
            StartRetriveData(URL);
        }
    }

    public void StartRetriveData(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        xValues.clear();
                        yValues.clear();
                        zValues.clear();
                        dataSets.clear();
                        tvDane.setText("");

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
                                        String soil_moisure = object.getString("wilgotnosc");

                                        if (sensor_name.equals("ALFA")) {

                                            xValues.add(new String(data));
                                            yValues.add(new BarEntry(i, parseFloat(soil_moisure)));

                                        }
                                        if (sensor_name.equals("BETA")) {

                                            xValues.add(data);
                                            zValues.add(new BarEntry(i, Float.parseFloat(soil_moisure)));
                                        }
                                    }
                                }
                                if (checkBoxTemperatura.isChecked()) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String data = object.getString("data");
                                        String sensor_name = object.getString("nazwa_sensora");
                                        String temperature = object.getString("temperatura");

                                        if (sensor_name.equals("ALFA")) {
                                            xValues.add(new String(data));
                                            yValues.add(new BarEntry(i, parseFloat(temperature)));
                                        }
                                        if (sensor_name.equals("BETA")) {
                                            xValues.add(data);
                                            zValues.add(new BarEntry(i, Float.parseFloat(temperature)));
                                        }
                                    }
                                }
                                if (checkBoxSwiatlo.isChecked()) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String data = object.getString("data");
                                        String sensor_name = object.getString("nazwa_sensora");
                                        String light = object.getString("naslonecznienie");

                                        if (sensor_name.equals("ALFA")) {
                                            xValues.add(new String(data));
                                            yValues.add(new BarEntry(i, parseFloat(light)));
                                        }
                                        if (sensor_name.equals("BETA")) {
                                            xValues.add(data);
                                            zValues.add(new BarEntry(i, Float.parseFloat(light)));
                                        }
                                    }
                                }
                                setDataOnChart(xValues, yValues, zValues);
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
                data.put("userId", userId);
                data.put("sensorAlfa", SzczegolyPolaActivity.sensorAlfa);
                data.put("sensorBeta", SzczegolyPolaActivity.sensorBeta);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setDataOnChart(ArrayList xValues, List<Entry> yValues, List<Entry> zValues) {
        LineDataSet set1 = new LineDataSet(yValues, "Data Set1");
        LineDataSet set2 = new LineDataSet(zValues, "Data Set2");
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        set1.setFillAlpha(110);
        set1.setColor(RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(0.001f);
        set2.setValueTextSize(0.001f);
        set2.setFillAlpha(110);
        set2.setColor(BLUE);
        set2.setLineWidth(3f);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new WykresActivity.MyAxisValueFormatter(xValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setLabelCount(4, true);
        if (yValues != null && yValues.isEmpty() && zValues != null && zValues.isEmpty()) {
            tvDane.setText("Nie ma danych \n do wyświetlenia! \n Zmien zakres!");
        }
    }

    public void btnMiesiace(View view) {
        StartRetriveData(URL);
    }

    public void btnTygodnie(View view) {
        StartRetriveData(URL1);
    }

    public void btnDni(View view) {
        StartRetriveData(URL2);
    }

    public void btnMinuty(View view) {
        StartRetriveData(URL3);
    }

    private class MyAxisValueFormatter extends ValueFormatter {
        private ArrayList<String> mValues;

        public MyAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValues.get((int) value);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), SzczegolyPolaActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
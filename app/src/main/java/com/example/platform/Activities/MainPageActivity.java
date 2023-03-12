package com.example.platform.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.platform.R;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainPageActivity");
        setContentView(R.layout.activity_main_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void btnDodajPole(View view) {
        Intent intent= new Intent(MainPageActivity.this, DodajPoleActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnPokazPola(View view) {
        Intent intent= new Intent(MainPageActivity.this, PokazPolaActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}

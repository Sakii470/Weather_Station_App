package com.example.platform.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

// Activietie enables user authentication

public class MainActivity extends AppCompatActivity {

//variable declaration and initialization
    private EditText etEmail, etPassword;
    private String email, password;
    private TextView tvStatusLogin;
    public static String userId;
    private String URL = "http://sensor332.atwebpages.com/register_login/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MainActivity");
        setContentView(R.layout.activity_main);
        findBy();

    }
    // Method initialize variables.Views are find by Id (XML).
    public void findBy() {
        email = password = "";
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvStatusLogin = findViewById(R.id.tvStatusLogin);
    }
// Method is activated if user click on btnLogin. Method sent entered data(email, password) to server, and give access to system if user data are correct.
    public void btnLogin(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (!email.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
// If response from the server will be userId this will mean that user was found in Database, and will obtain access to system function.
// If response from server will be "failure" this will mean that user wasn't found in Database, and won't obtain access to system function
                    if (response.trim().equals("failure")) {
                        Toast.makeText(MainActivity.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                        tvStatusLogin.setText("Nieprawidłowy email lub hasło! Spróbuj ponownie.");
                    } else if (!(!response.equals(response))) {
                        userId = response;
// If user obtained access, MainActivity create Intent to MainPageActivity. Afterwards user will see MainPageActivity
                        Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
// If will be problem with connection to server user will see information about this.
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
// Method is activated if user click on btnRegister. MainActivitie create Intent to RegisterActivity. Afterwards user will see RegisterActivitie.
    public void btnRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class splashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "not");
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        Intent intent;
        if (loggedIn) {
            intent = new Intent(splashScreenActivity.this, DashboardActivity.class);
            intent.putExtra("email", email);
        } else {
            intent = new Intent(splashScreenActivity.this, LoginActivity.class);
        }
        startActivity(intent);
    }
}

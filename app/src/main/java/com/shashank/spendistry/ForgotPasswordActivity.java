package com.shashank.spendistry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistry.ViewModels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        EditText password = findViewById(R.id.password_forgot);
        EditText confirmPassword = findViewById(R.id.repassword_forgot);
        TextInputLayout passwordLayout = findViewById(R.id.pass_forgot);
        Toolbar toolbar = findViewById(R.id.forgot_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }
        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));
        TextInputLayout confirmPasswordLayout = findViewById(R.id.repass_forgot);
        LinearLayout linearLayout = findViewById(R.id.linearlayout_forgot);
        Button submit = findViewById(R.id.update_password);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    passwordLayout.setError("Password does not match");
                    confirmPasswordLayout.setError("Password does not match");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordLayout.setErrorEnabled(false);
                            confirmPasswordLayout.setErrorEnabled(false);
                        }
                    },2000);
                } else if(password.getText().toString().length() < 6) {
                    passwordLayout.setError("Password must be atleast 6 characters");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordLayout.setErrorEnabled(false);
                        }
                    },2000);
                } else if (password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().isEmpty()) {
                        passwordLayout.setError("Password cannot be empty");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                passwordLayout.setErrorEnabled(false);
                            }
                        },2000);
                    }
                    if (confirmPassword.getText().toString().isEmpty()) {
                        confirmPasswordLayout.setError("Password cannot be empty");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                confirmPasswordLayout.setErrorEnabled(false);
                            }
                        },2000);
                    }
                } else {
                    authViewModel.setNewPassword(ForgotPasswordActivity.this,linearLayout,email,password.getText().toString());

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SharedPreferences sharedPreferences = getSharedPreferences("loggedIn",MODE_PRIVATE);
                if (sharedPreferences.getBoolean("loggedIn",false)) {
                    startActivity(new Intent(ForgotPasswordActivity.this, DashboardActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
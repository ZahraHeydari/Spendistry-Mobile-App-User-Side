package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private TextInputLayout email_login_field, pass_login_field;
    private TextView reg, forgot;
    private LinearLayout linearLayout;

    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.find();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.pass_login);
        loginButton = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        forgot = findViewById(R.id.forgot);
        email_login_field = findViewById(R.id.email_login_field);
        pass_login_field = findViewById(R.id.pass_login_field);
        linearLayout = findViewById(R.id.login_layout);

        AuthViewModel AuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.cardBlue));

        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid(email.getText().toString())) {
                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                    intent.putExtra("email", email.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    email_login_field.setError("Enter Email");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            email_login_field.setErrorEnabled(false);
                        }
                    }, 2500);

                }
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty()) {
                    email_login_field.setError("Email is required");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            email_login_field.setErrorEnabled(false);
                        }
                    }, 2500);
                }
                if (password.getText().toString().isEmpty()) {
                    pass_login_field.setError("Password is required");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pass_login_field.setErrorEnabled(false);
                        }
                    }, 2500);
                }
                if (isEmailValid(email.getText().toString())) {
                    Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(R.layout.loading_layout);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    AuthViewModel.onLogIn(linearLayout, email.getText().toString(), password.getText().toString()).observe(LoginActivity.this, new Observer<String>() {

                        @Override
                        public void onChanged(String token) {
                            if (token != null && !token.equals("Invalid")) {
                                if (!token.equals("")) {
                                    editor.putBoolean("loggedIn", true);
                                    editor.putString("email", email.getText().toString());
                                    editor.putString("token", token);
                                    editor.apply();
                                    //
                                    dialog.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    finish();
                                }
                            } else {
                                dialog.dismiss();
                            }
                        }

                    });
                } else {
                    email_login_field.setError("Email is not valid");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            email_login_field.setErrorEnabled(false);
                        }
                    }, 2500);
                }
            }
        });

    }


}
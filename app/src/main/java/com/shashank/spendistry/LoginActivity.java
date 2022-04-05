package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private TextView reg, forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.pass_login);
        loginButton = findViewById(R.id.login);
        reg = findViewById(R.id.reg);
        forgot = findViewById(R.id.forgot);

        AuthViewModel AuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));

        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, .class);
//                startActivity(intent);
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (isEmailValid(email.getText().toString())) {
                  if (password.getText().toString().length()>5){
                        AuthViewModel.onLogIn(email.getText().toString() ,password.getText().toString()).observe(LoginActivity.this, new Observer<String>() {

                            @Override
                            public void onChanged(String token) {
                                if (token != null)
                                {
                                    if (!token.equals("")){
                                    editor.putBoolean("loggedIn", true);
                                    editor.putString("email", email.getText().toString());
                                    editor.putString("token", token);
                                    editor.apply();
                                    //
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                }
                        }
                            }

                        });
                  }
              } else {
                  Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
              }
            }
        });

    }
    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.find();
    }


}
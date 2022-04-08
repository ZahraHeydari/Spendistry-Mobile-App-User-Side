package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;


import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    private EditText firstName, lastName, mobileNumber, email, password, password2, address;
    private TextInputLayout firstNameLayout, lastNameLayout, mobileNumberLayout, emailLayout,
                            passwordLayout, rePasswordLayout, addressLayout;
    private SharedPreferences sharedPreferences;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobileNumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        password2 = findViewById(R.id.pass2);
        address = findViewById(R.id.address);
        Button send = findViewById(R.id.reg_btn);
        firstNameLayout = findViewById(R.id.firstNameLayout);
        lastNameLayout = findViewById(R.id.lastNameLayout);
        mobileNumberLayout = findViewById(R.id.mobileNumberLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        rePasswordLayout = findViewById(R.id.rePasswordLayout);
        addressLayout = findViewById(R.id.addressLayout);
        ScrollView scrollView = findViewById(R.id.scrollView);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);


        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));

        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_id = email.getText().toString();
                String pass = password.getText().toString();
                String pass2 = password2.getText().toString();
                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                String mob = mobileNumber.getText().toString();
                String add = address.getText().toString();

                if (fname.equals("") || lname.equals("") || mob.equals("") || email_id.equals("") || pass.equals("") || pass2.equals("") || add.equals("")) {
                    if (fname.equals("")) {
                        firstNameLayout.setError("First Name is required");
                        scrollView.scrollTo(0, firstName.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstNameLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    } else if (lname.equals("")) {
                        lastNameLayout.setError("Last Name is required");
                        scrollView.scrollTo(0, lastName.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lastNameLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    } else if (mob.equals("") || mob.length() != 10) {
                        mobileNumberLayout.setError("Mobile Number is required");
                        scrollView.scrollTo(0, mobileNumber.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mobileNumberLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    } else if (isEmailValid(email_id)) {
                        emailLayout.setError("Email is required");
                        scrollView.scrollTo(0, email.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                emailLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    } else if (pass.equals("") || pass.length() < 6) {
                        passwordLayout.setError("Password must be at least 6 characters");
                        scrollView.scrollTo(0, password.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                passwordLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    } else if (pass2.equals("") || pass2.length() < 6) {
                        rePasswordLayout.setError("Password must be at least 6 characters");
                        scrollView.scrollTo(0, password2.getTop());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rePasswordLayout.setErrorEnabled(false);
                            }
                        }, 2000);
                    }
                } else {
                    if (pass.equals(pass2)) {
                        //send otp
                        register(email_id);
                    }
                }

            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (scrollView.getScrollY() == scrollView.getChildAt(0).getHeight() - scrollView.getHeight()) {
                    send.setText("Register");
                } else {
                    send.setText("Next");
                }
            }
        });

        scrollView.setSmoothScrollingEnabled(true);
    }

    public void register(String email) {
        authViewModel.createAccount(new Auth(email, password.getText().toString())).observe(this, new Observer<Auth>() {
                    /**
                     * Called when the data is changed.
                     *
                     * @param auth The new data
                     */
                    @Override
                    public void onChanged(Auth auth) {
//                        int OTP = loginViewModel.sendEmail(email);
                        authViewModel.addUser(new Users(email, firstName.getText().toString(), lastName.getText().toString(),
                                email, mobileNumber.getText().toString(), address.getText().toString())).observe(RegisterActivity.this, new Observer<Users>() {
                            /**
                             * Called when the data is changed.
                             *
                             * @param users The new data
                             */
                            @Override
                            public void onChanged(Users users) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                intent.putExtra("OTP", OTP);
                                startActivity(intent);
                            }
                        });
                    }
                });



    }

    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9_.!#$%&'*+/=?`{|}~^-]+(?:\\.[A-Z0-9_.!#$%&'*+/=?`{|}~^-]+↵\n" +
                ")*@[A-Z0-9-]+(?:\\.[A-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return !matcher.find();
    }

}
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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kevinschildhorn.otpview.OTPView;
import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class RegisterActivity extends AppCompatActivity {
    private EditText firstName, lastName, mobileNumber, email, password, password2, address;
    private TextInputLayout firstNameLayout, lastNameLayout, mobileNumberLayout, emailLayout,
                            passwordLayout, rePasswordLayout, addressLayout;
    private SharedPreferences sharedPreferences;
    private AuthViewModel authViewModel;
    private LinearLayout linearLayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

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
        linearLayout = findViewById(R.id.activity_register);
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

                if (fname.equals("") || lname.equals("") || mob.equals("") || email_id.equals("") || pass.equals("") || pass2.equals("")) {
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
                        scrollView.scrollTo(0, mobileNumber.getBottom());
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
                        if (!send.getText().toString().equals("Next")) {
                            authViewModel.newOTP(linearLayout, email.getText().toString());
                            register(email_id);
                        } else {
                            scrollView.scrollTo(0,scrollView.getBottom());
                        }
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
        Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.otp_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        final OTPView otpField = dialog.findViewById(R.id.otp_field);
        final TextView resendOtp = dialog.findViewById(R.id.resend_dialog);
        dialog.show();

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.newOTP(linearLayout, email);
            }
        });
        otpField.setOnFinishListener(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                authViewModel.verifyOTP(linearLayout, email, Integer.parseInt(s)).observe(RegisterActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("201")) {
                            dialog.dismiss();
                            Dialog dialog1 = new Dialog(RegisterActivity.this);
                            dialog1.setContentView(R.layout.loading_layout);
                            dialog1.setCancelable(false);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog1.show();
                            authViewModel.createAccount(new Auth(email, password.getText().toString())).observe(RegisterActivity.this, new Observer<Auth>() {
                                @Override
                                public void onChanged(Auth auth) {
                                    if(auth.getMessage() == null) {
                                        authViewModel.addUser(new UserDetails(email, firstName.getText().toString(), lastName.getText().toString(),
                                                mobileNumber.getText().toString(), address.getText().toString())).observe(RegisterActivity.this, new Observer<UserDetails>() {
                                            @Override
                                            public void onChanged(UserDetails users) {
                                                dialog1.dismiss();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                            }
                                        });
                                    } else if (auth.getMessage().equals("Email already exists")) {
                                        dialog1.dismiss();
                                        Snackbar snackbar = Snackbar.make(linearLayout, "Email already exists!!", Snackbar.LENGTH_SHORT);
                                        snackbar.setTextColor(Color.WHITE);
                                        snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                                        snackbar.show();
                                    }
                                }
                            });
                        }
                    }
                });
                return null;
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
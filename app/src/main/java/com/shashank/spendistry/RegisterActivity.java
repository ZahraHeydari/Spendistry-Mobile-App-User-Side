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


import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity {
    private Random rnd = new Random();
    private EditText firstName, lastName, mobileNumber, email, password, password2, address;
    private SharedPreferences sharedPreferences;
    private boolean passMatch;
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
                        firstName.setError("First Name is required");
                    } else if (lname.equals("")) {
                        lastName.setError("Last Name is required");
                    } else if (mob.equals("") || mob.length() != 10) {
                        mobileNumber.setError("Mobile Number is required");
                    } else if (isEmailValid(email_id)) {
                        email.setError("Invalid Email");
                    } else if (pass.equals("") || pass.length() < 6) {
                        password.setError("Enter proper password");
                    } else if (pass2.equals("") || pass2.length() < 6) {
                        password2.setError("Enter proper password");
                    } else if (add.equals("")) {
                        address.setError("Address is required");
                    }
                } else {
                    if (pass.equals(pass2)) {
                        register(email_id);
                    }
                }

            }
        });
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
package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.ViewModels.AuthViewModel;

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Intent i = getIntent();
        Users user = i.getParcelableExtra("userData");
        TextView tv = findViewById(R.id.dis);
        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        int otp = sharedPreferences.getInt("otp",0);
        Button otp_btn = findViewById(R.id.otp);
        EditText otp_et = findViewById(R.id.otp_et);
        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (Integer.parseInt(otp_et.getText().toString()) == otp){
                   tv.setText(user.getId()+"\n"+user.getFirstName());
//                   loginViewModel.updateAccount(user.getEmail(), new Auth(true));

               }
            }
        });
    }
}
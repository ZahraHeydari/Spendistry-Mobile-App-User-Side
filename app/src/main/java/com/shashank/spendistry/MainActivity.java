package com.shashank.spendistry;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.ViewModels.AuthViewModel;
import com.shashank.spendistry.ViewModels.DashboardViewModel;

public class MainActivity extends AppCompatActivity {
    private Auth auth;
    private SharedPreferences sharedPreferences;
    private DashboardViewModel dashboardViewModel;
    private ImageView imageView;
    private String encryptedMsg;
    private LinearLayout layout;
    private TextView monthlyExpense, yearlyExpense, name, emailTextView;
    private RoundedImageView profile;
    private ImageButton editProfile;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        Button logout = findViewById(R.id.logout);
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = sharedPreferences.getString("email", "not loggedIn");
        layout = findViewById(R.id.dashboard_layout);
        monthlyExpense = findViewById(R.id.monthly_exp);
        yearlyExpense = findViewById(R.id.yearly_exp);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        editProfile = findViewById(R.id.edit_profile);
        editProfile.setEnabled(false);



        //

        //

        dashboardViewModel.getDashboard(layout,email).observe(this, new Observer<Dashboard>() {
            @Override
            public void onChanged(Dashboard dashboard) {
                QrGenerator(dashboard.getEncryptedQr());
                monthlyExpense.setText( dashboard.getMonthlyTotalAll()+"");
                yearlyExpense.setText(dashboard.getAllTimeTotal()+"");
                name.setText(dashboard.getUserDetails().getFname()+" "+dashboard.getUserDetails().getLname());
                emailTextView.setText(dashboard.getEmail());
                Glide.with(MainActivity.this).load(Constants.API_URL+"userProfile/"+email+".jpeg")
                        .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(true)).into(profile);
                editProfile.setEnabled(true);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("loggedIn", false);
                editor.apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void QrGenerator(String qrCodeData) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(qrCodeData, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.mainBlue) : getResources().getColor(R.color.cardBlue));
                }
            }
            ((ImageView) findViewById(R.id.qr)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
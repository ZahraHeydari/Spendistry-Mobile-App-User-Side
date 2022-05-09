package com.shashank.spendistry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.R;
import com.shashank.spendistry.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistry.ViewModels.BusinessViewModel;

public class BusinessProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_business_profile);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView businessName = findViewById(R.id.business_profile_name);
        TextView businessAddress = findViewById(R.id.business_profile_address);
        TextView businessPhone = findViewById(R.id.business_profile_mobile);
        TextView businessEmail = findViewById(R.id.business_profile_email);
        TextView businessOwner = findViewById(R.id.business_profile_owner);
        TextView businessWebsite = findViewById(R.id.business_profile_website);
        TextView businessGst = findViewById(R.id.business_profile_gst);
        TextView TollFree = findViewById(R.id.business_profile_toll_free);
        TextView map_tv = findViewById(R.id.map_tv);
        ImageView businessImage = findViewById(R.id.business_profile_image);

        Intent intent = getIntent();
        String businessId = intent.getStringExtra("businessId");
        BusinessViewModel businessViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(),businessId)).get(BusinessViewModel.class);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        businessViewModel.getVendor().observe(this, business -> {
            Glide.with(this).load(Constants.API_URL+"vendorProfile/"+businessId+".jpeg")
                    .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .apply(RequestOptions.skipMemoryCacheOf(true)).into(businessImage);
            if (business != null) {
                dialog.cancel();
                businessName.setText("Business Name: "+business.getBusinessName());
                businessAddress.setText("Address: "+business.getAddress());
                businessPhone.setText("Contact us: "+business.getMobileNumber());
                businessEmail.setText("Email: "+business.getEmail());
                businessOwner.setText("Name: "+business.getFirstName() + " " + business.getLastName());
                if (!business.getWebsite().equals("") && business.getWebsite() != null) {
                    businessWebsite.setText("Website: "+business.getWebsite());
                }   else {
                    businessWebsite.setVisibility(TextView.GONE);
                }
                if (business.getGstNumber() != null && !business.getGstNumber().equals("")) {
                    businessGst.setText("GST Number: "+business.getGstNumber());
                }  else {
                    businessGst.setVisibility(TextView.GONE);
                }
                if (business.getTollFreeNumber() != null && !business.getTollFreeNumber().equals("")) {
                    TollFree.setText("Toll Free: "+business.getTollFreeNumber());
                }  else {
                    TollFree.setVisibility(TextView.GONE);
                } if (!business.getLat().equals("") && !business.getLng().equals("")) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.google_map);
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(BusinessProfileActivity.this, R.raw.aubergine));
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(business.getLat()), Double.parseDouble(business.getLng()))).title("Shop Location"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(business.getLat()), Double.parseDouble(business.getLng())), 20));
                            }
                        });
                    }

                } else {
                    map_tv.setVisibility(TextView.VISIBLE);
                }

            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (googleMap != null) {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(BusinessProfileActivity.this, R.raw.aubergine));
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
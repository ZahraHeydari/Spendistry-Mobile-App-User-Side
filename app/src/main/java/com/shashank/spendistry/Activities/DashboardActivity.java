package com.shashank.spendistry.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistry.Adapters.DashboardInvoiceAdapter;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.R;
import com.shashank.spendistry.TouchListener.OnSwipeTouchListener;
import com.shashank.spendistry.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistry.ViewModels.DashboardFragmentViewModel;
import com.shashank.spendistry.databinding.ActivityDashboardBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    DrawerLayout drawer;
    UserDetails userDetails;
    String userId;
    NavigationView navigationView;
    ImageBadgeView imageBadgeView;
    DashboardInvoiceAdapter dashboardInvoiceAdapter;
    ArrayList<BusinessDetail> businessDetails;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.zxing_transparent));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.zxing_transparent));

        setSupportActionBar(binding.appBarDashboard.toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding.appBarDashboard.linearLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        RoundedImageView navImage = headerView.findViewById(R.id.nav_image);
        RoundedImageView qrImageView = headerView.findViewById(R.id.qr_nav);
        RecyclerView recyclerView = binding.appBarDashboard.recyclerView;
        dashboardInvoiceAdapter = new DashboardInvoiceAdapter(new ArrayList<>(), this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dashboardInvoiceAdapter);
        Button shareQr = headerView.findViewById(R.id.share_qr);
        userId = sharedPreferences.getString("email", "");
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);
        SwipeRefreshLayout swipeRefreshLayout = binding.appBarDashboard.swipeRefreshLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarDashboard.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DashboardFragmentViewModel dashboardViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(getApplication(), binding.appBarDashboard.linearLayout, userId)).get(DashboardFragmentViewModel.class);
        dashboardViewModel.getDashboard().observe(this, dashboard -> {
            if (dashboard != null) {
                businessDetails = dashboard.getBusinessDetails();
                userDetails = dashboard.getUserDetails();
                binding.appBarDashboard.monthlyExp.setText(" ₹" + dashboard.getMonthlyTotalAll());
                binding.appBarDashboard.yearlyExp.setText(" ₹" + dashboard.getAllTimeTotal());
                navUsername.setText(dashboard.getUserDetails().getFname() + " " + dashboard.getUserDetails().getLname());
                navEmail.setText(dashboard.getUserDetails().getEmail());
                if (imageBadgeView != null) {
                    imageBadgeView.setBadgeValue(dashboard.getReportCount());
                }
                dashboardInvoiceAdapter.setBusinessDetails(dashboard.getBusinessDetails());
                QRCodeWriter writer = new QRCodeWriter();
                //create bg thread
                new Thread(() -> {
                    BitMatrix bitMatrix = null;
                    try {
                        bitMatrix = writer.encode(dashboard.getEncryptedQr(), BarcodeFormat.QR_CODE, 512, 512);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.mainBlue) : getResources().getColor(R.color.windowBlue));
                        }
                    }
                    runOnUiThread(() -> {
                        // OnPostExecute stuff here
                        qrImageView.setImageBitmap(bmp);
                    });
                }).start();


                dialog.dismiss();

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashboardViewModel.refreshData(binding.appBarDashboard.linearLayout, userId);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_edit:
                    try {
                        if (isConnected()) {
                            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                            intent.putExtra("user", userDetails);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            showSnackBar();
                            closeDrawer();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.nav_all_invoice:
                    try {
                        if (isConnected()) {
                            Intent intent0 = new Intent(DashboardActivity.this, InvoicesActivity.class);
                            intent0.putExtra("activity", "all");
                            startActivity(intent0);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            showSnackBar();
                            closeDrawer();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.reported_nav:
                    try {
                        if (isConnected()) {
                            Intent intent1 = new Intent(DashboardActivity.this, ReportedInvoiceActivity.class);
                            intent1.putExtra("email", userId);
                            startActivity(intent1);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            showSnackBar();
                            closeDrawer();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.returned_nav:
                    try {
                        if (isConnected()) {
                            Intent intent2 = new Intent(DashboardActivity.this, InvoicesActivity.class);
                            intent2.putExtra("activity", "returned");
                            startActivity(intent2);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            showSnackBar();
                            closeDrawer();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.logout_nav:
                    Intent intent3 = new Intent(DashboardActivity.this, LoginActivity.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedIn", false);
                    editor.apply();
                    startActivity(intent3);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    break;
            }
            return true;
        });

        Glide.with(this).load(Constants.API_URL + "userProfile/" + userId + ".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(navImage);

        shareQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrImageView.buildDrawingCache();
                Bitmap qr = qrImageView.getDrawingCache();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, getImageUri(DashboardActivity.this, qr));
                share.putExtra(Intent.EXTRA_TEXT, "Here’s the QR code specially designed for Spendistry and all incoming invoices shall be directed with this QR code.");
                startActivity(Intent.createChooser(share, "Share via"));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Spendistry QR code", null);
        return Uri.parse(path);
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar.make(drawer, "Internet is not available", Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
        snackbar.show();
    }

    public boolean isConnected() throws Exception {
        String command = "";
        command = "ping -c 1 " + Constants.API_URL.replace("https://", "").replace("/", "");
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        FrameLayout badgeLayout = (FrameLayout) menu.findItem(R.id.reports).getActionView();
        imageBadgeView = badgeLayout.findViewById(R.id.badge);
        imageBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isConnected()) {
                        Intent intent1 = new Intent(DashboardActivity.this, ReportedInvoiceActivity.class);
                        intent1.putExtra("email", userId);
                        startActivity(intent1);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        showSnackBar();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                dashboardInvoiceAdapter.setBusinessDetails(businessDetails);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dashboardInvoiceAdapter.searchQuery(s, businessDetails);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        closeDrawer();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return true;
    }
}
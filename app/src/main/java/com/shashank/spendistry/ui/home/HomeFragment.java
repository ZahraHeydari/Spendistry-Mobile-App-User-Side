package com.shashank.spendistry.ui.home;

import static android.content.Context.MODE_PRIVATE;

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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shashank.spendistry.Adapters.DashboardInvoiceAdapter;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Activities.EditProfileActivity;
import com.shashank.spendistry.Activities.InvoicesActivity;
import com.shashank.spendistry.Activities.LoginActivity;
import com.shashank.spendistry.Models.BusinessDetail;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.R;
import com.shashank.spendistry.Activities.ReportedInvoiceActivity;
import com.shashank.spendistry.TouchListener.OnSwipeTouchListener;
import com.shashank.spendistry.ViewModelFactory.ViewModelFactory;
import com.shashank.spendistry.ViewModels.DashboardViewModel;
import com.shashank.spendistry.databinding.FragmentHomeBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class HomeFragment extends Fragment {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String email;
    private ImageBadgeView imageBadgeView;
    private DashboardInvoiceAdapter adapter;
    private DashboardViewModel dashboardViewModel;
    private RecyclerView recyclerView;
    private ArrayList<BusinessDetail> allData;
    private UserDetails userDetails;
    private FragmentHomeBinding binding;
    private LinearLayout linearLayout;

    @Override
    public void onResume() {
        super.onResume();
        if (dashboardViewModel != null) {
            dashboardViewModel.getDashboard();
            closeDrawer();
        }
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
    }

    public boolean isConnected() throws Exception {
        String command="";
        command = "ping -c 1 "+Constants.API_URL.replace("https://","").replace("/","");
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }


    @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        linearLayout = root.findViewById(R.id.linear_layout);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loggedIn", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        dashboardViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelFactory(requireActivity().getApplication(),linearLayout,email)).get(DashboardViewModel.class);

        //get navigation drawer
        navigationView = requireActivity().findViewById(R.id.nav_view);
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        TextView monthlyExp = root.findViewById(R.id.monthly_exp);
        TextView yearlyExp = root.findViewById(R.id.yearly_exp);
        recyclerView = root.findViewById(R.id.recyclerView);
        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        //
        root.setOnTouchListener(new OnSwipeTouchListener(requireContext()){
            public void onSwipeRight() {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_edit:
                    try {
                        if (isConnected()) {
                            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                            intent.putExtra("user", userDetails);
                            startActivity(intent);
                            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                            Intent intent0 = new Intent(getActivity(), InvoicesActivity.class);
                            intent0.putExtra("activity", "all");
                            startActivity(intent0);
                            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                            Intent intent1 = new Intent(requireActivity(), ReportedInvoiceActivity.class);
                            intent1.putExtra("email", email);
                            startActivity(intent1);
                            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                            Intent intent2 = new Intent(requireActivity(), InvoicesActivity.class);
                            intent2.putExtra("activity", "returned");
                            startActivity(intent2);
                            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            showSnackBar();
                            closeDrawer();
                        }
                    } catch (Exception e) {
                e.printStackTrace();
            }

                    break;
                case R.id.logout_nav:
                    Intent intent3 = new Intent(requireActivity(), LoginActivity.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("loggedIn", false);
                    editor.apply();
                    startActivity(intent3);
                    requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    break;
            }
            return true;
        });
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View headerView = navigationView.getHeaderView(0);
        String userId = sharedPreferences.getString("email", "");
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navEmail = headerView.findViewById(R.id.nav_email);
        RoundedImageView navImage = headerView.findViewById(R.id.nav_image);
        RoundedImageView qrImageView = headerView.findViewById(R.id.qr_nav);
        Button shareQr = headerView.findViewById(R.id.share_qr);
        Glide.with(requireActivity()).load(Constants.API_URL+"userProfile/"+email+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(navImage);
        //navbar onclick
        dashboardViewModel.getDashboard().observe(requireActivity(), dashboard -> {
            if (dashboard != null) {
                allData = dashboard.getBusinessDetails();
                userDetails = dashboard.getUserDetails();
                monthlyExp.setText(" ₹" + dashboard.getMonthlyTotalAll());
                yearlyExp.setText(" ₹" + dashboard.getAllTimeTotal());
                navUsername.setText(dashboard.getUserDetails().getFname() + " " + dashboard.getUserDetails().getLname());
                navEmail.setText(dashboard.getUserDetails().getEmail());
                if (imageBadgeView != null) {
                    imageBadgeView.setBadgeValue(dashboard.getReportCount());
                }
                adapter = new DashboardInvoiceAdapter(dashboard.getBusinessDetails(), requireContext(), requireActivity());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

                //
                QRCodeWriter writer = new QRCodeWriter();

                try {
                    BitMatrix bitMatrix = writer.encode(dashboard.getEncryptedQr(), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.mainBlue) : getResources().getColor(R.color.windowBlue));
                        }
                    }
                    qrImageView.setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        shareQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrImageView.buildDrawingCache();
                Bitmap qr = qrImageView.getDrawingCache();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, getImageUri(requireContext(),qr));
                share.putExtra(Intent.EXTRA_TEXT,"Here’s the QR code specially designed for Spendistry and all incoming invoices shall be directed with this QR code.");
                startActivity(Intent.createChooser(share,"Share via"));
                requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        return root;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Spendistry QR code", null);
        return Uri.parse(path);
    }

    private void showSnackBar(){
        Snackbar snackbar = Snackbar.make(linearLayout, "Internet is not available", Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.red));
        snackbar.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        FrameLayout badgeLayout = (FrameLayout) menu.findItem(R.id.reports).getActionView();
        imageBadgeView = badgeLayout.findViewById(R.id.badge);
        imageBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isConnected()){
                        Intent intent1 = new Intent(requireActivity(), ReportedInvoiceActivity.class);
                        intent1.putExtra("email", email);
                        startActivity(intent1);
                        requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } else {
                        showSnackBar();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        android.widget.SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.searchQuery(s, allData);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
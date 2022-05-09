package com.shashank.spendistry.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.R;
import com.shashank.spendistry.ViewModels.AuthViewModel;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private AuthViewModel authViewModel;
    private ImageView profileImage;
    private UserDetails userDetails;
    private String emailStr;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        //
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        //
        Intent intent = getIntent();
        userDetails =  intent.getParcelableExtra("user");
        scrollView = findViewById(R.id.scrollView_profile);
        EditText email = findViewById(R.id.email);
        EditText firstName = findViewById(R.id.first_name_profile);
        EditText lastName = findViewById(R.id.last_name_profile);
        EditText phone = findViewById(R.id.phone_number_profile);
        EditText address = findViewById(R.id.address_profile);
        TextInputLayout emailLayout = findViewById(R.id.email_layout);
        TextInputLayout firstNameLayout = findViewById(R.id.first_name_layout);
        TextInputLayout lastNameLayout = findViewById(R.id.last_name_layout);
        TextInputLayout phoneLayout = findViewById(R.id.phone_number_layout);
        TextInputLayout addressLayout = findViewById(R.id.address_layout);
        profileImage = findViewById(R.id.profile_image);
        registerForContextMenu(profileImage);
        Button update_profile = findViewById(R.id.update_profile);
        //
        email.setText(userDetails.getEmail());
        emailStr = userDetails.getEmail();
        firstName.setText(userDetails.getFname());
        lastName.setText(userDetails.getLname());
        phone.setText(userDetails.getMobileNumber());
        address.setText(userDetails.getAddress());
        Glide.with(this).load(Constants.API_URL+"userProfile/"+userDetails.getEmail()+".jpeg")
                .placeholder(R.drawable.loading).error(R.drawable.no_profile) .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true)).into(profileImage);
        //
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImage.showContextMenu();
            }
        });
        //
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()) {
                    authViewModel.updateUser(scrollView, new UserDetails(userDetails._id,firstName.getText().toString(),lastName.getText().toString(),phone.getText().toString(),address.getText().toString()));
                }
            }
        });

    }

    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.find();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_pass) {
            //open OTP activity
            Intent intent = new Intent(this, OtpActivity.class);
            intent.putExtra("email", emailStr);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        menu.setHeaderTitle(R.string.context_title);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_pic: ;
                Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intent, 23);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar snackbar = Snackbar.make(scrollView, "Please grant permission to access storage", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setAction("Grant", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        Snackbar snackbar = Snackbar.make(scrollView, "Please grant permission to access storage", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setAction("Grant", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });
                        snackbar.setActionTextColor(Color.WHITE);
                        snackbar.show();
                    }
                }).check();

                return true;
            case R.id.remove_pic:

                authViewModel.deleteProfilePic(scrollView, userDetails.get_id());
                profileImage.setImageResource(R.drawable.no_profile);

                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Glide.with(this).load(uri).into(profileImage);
            File file = new File( getRealPathFromURI(uri));
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("userProfile", file.getName(), reqFile);
            authViewModel.setNewProfilePic(scrollView,userDetails.get_id(), body);
        }


    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}
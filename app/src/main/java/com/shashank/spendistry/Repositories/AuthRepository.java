package com.shashank.spendistry.Repositories;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Activities.LoginActivity;
import com.shashank.spendistry.Models.Auth;
import com.shashank.spendistry.Models.OTP;
import com.shashank.spendistry.Models.UserDetails;
import com.shashank.spendistry.Models.Users;
import com.shashank.spendistry.R;
import com.shashank.spendistry.SpendistryApi.SpendistryApi;


import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private Application application;
    private Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    SpendistryApi api = retrofit.create(SpendistryApi.class);




    public AuthRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<Users> fetchUsers(String email){
        MutableLiveData<Users> usersMutableLiveData;
        usersMutableLiveData = new MutableLiveData<>();
        Call<Users> call = api.getUser(email);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                assert response.body() != null;
                usersMutableLiveData.setValue(response.body());
//                Toast.makeText(application, ""+response.body().getPassword(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                Toast.makeText(application, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
        return usersMutableLiveData;
    }

    public void updateUser(ScrollView scrollView, UserDetails userDetails){
        Call<UserDetails> call = api.updateUser(userDetails.get_id(), userDetails);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NonNull Call<UserDetails> call, @NonNull Response<UserDetails> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(scrollView, "Profile updated", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(application.getResources().getColor(R.color.cardBlue));
                snackbar.show();

            }

            @Override
            public void onFailure(@NonNull Call<UserDetails> call, @NonNull Throwable t) {
                Toast.makeText(application, "Something went wrong!! "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
    });
    }


    public void setNewProfilePic(ScrollView scrollView, String email, MultipartBody.Part part) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        try {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            api = new Retrofit.Builder().baseUrl(Constants.API_URL).client(client).build().create(SpendistryApi.class);
            retrofit2.Call<okhttp3.ResponseBody> req = api.setNewProfilePic(email,part);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Do Something
                    if (!response.isSuccessful()) {
                        Snackbar snackbar = Snackbar.make(scrollView, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                        snackbar.show();
                        return;
                    }
                    Snackbar snackbar = Snackbar.make(scrollView, "Profile picture updated", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(application.getResources().getColor(R.color.cardBlue));
                    snackbar.show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Snackbar snackbar = Snackbar.make(scrollView, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                    snackbar.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void deleteProfilePic(ScrollView layout,String id){
        Call<String> call = api.deleteProfilePic(id);
        try {
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            Snackbar snackbar = Snackbar.make(layout, "Profile picture not found", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(layout, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                            snackbar.show();
                        }
                        return;
                    }
                    Snackbar snackbar = Snackbar.make(layout, "Profile picture deleted", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(application.getResources().getColor(R.color.cardBlue));
                    snackbar.show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    t.printStackTrace();
                    Snackbar snackbar = Snackbar.make(layout, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                    snackbar.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<String> onLogIn(LinearLayout linearLayout,String email, String password){
        MutableLiveData<String> usersMutableLiveData;
        usersMutableLiveData = new MutableLiveData<>();
        Auth auth = new Auth(email,password);
        Call<JsonElement> call = api.getAuth(auth);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (!response.isSuccessful()) {

                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().isJsonObject()) {
                    //convert jsonElement to jsonObject
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    //get Auth from jsonObject
                    Gson gson = new Gson();
                    Auth auth = gson.fromJson(jsonObject, Auth.class);
                    if (auth.getMessage() != null) {
                    if (auth.getMessage().equals("Invalid Password") || auth.getMessage().equals("Cannot find user email")) {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Email or Password is incorrect!!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(application.getResources().getColor(R.color.red));
                        snackbar.show();
                        usersMutableLiveData.setValue("Invalid");
                    }
                }
                }else {
                    if (response.headers().get("Auth-Token") != null) {
                        usersMutableLiveData.setValue(response.headers().get("Auth-Token"));
                    }
                }


            }


            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Toast.makeText(application, "Something went wrong!! "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return usersMutableLiveData;
    }

    public void updateAccount(String email, Auth auth){
        Call<Auth> call = api.updateAccount(email,auth);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }


            @Override
            public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {
                Toast.makeText(application, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<UserDetails> createUser(UserDetails users){
        MutableLiveData<UserDetails> usersMutableLiveData = new MutableLiveData<>();
        Call<UserDetails> call = api.createUser(users);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.raw(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Call<Auth> call1 = api.createInvoice(new Auth(users.getEmail()));
                call1.enqueue(new Callback<Auth>() {

                    @Override
                    public void onResponse(Call<Auth> call, Response<Auth> response1) {
                        if (response1.isSuccessful()) {
                            usersMutableLiveData.setValue(response.body());
                        } else {
                            Toast.makeText(application, "notWorking: invoice " + response1.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Auth> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {

            }
        });
        return usersMutableLiveData;
    }

    public MutableLiveData<Auth> createAccount(Auth auth) {
        MutableLiveData<Auth> authMutableLiveData = new MutableLiveData<>();
        Call<Auth> call = api.createAccount( auth);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Auth auth = new Auth();
                    auth.setMessage("Email already exists");
                    authMutableLiveData.setValue(auth);
                    return;
                }
                authMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(application, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return authMutableLiveData;
    }



   public void deleteAccount(String email){
        Call<Auth> call = api.deleteAccount(email);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "notWorking: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

            }
        });
    }

    public void sendOTP(LinearLayout linearLayout,String email){
        OTP otp1 = new OTP(email);
        Call<String> call = api.sendOTP(otp1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                    snackbar.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(linearLayout, "OTP sent!", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.cardBlue));
                snackbar.show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                snackbar.show();
            }
        });
    }

    public void newOTP(LinearLayout linearLayout,String email){
        OTP otp1 = new OTP(email);
        Call<String> call = api.newOTP(otp1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                    snackbar.show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(linearLayout, "OTP sent!", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.cardBlue));
                snackbar.show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                snackbar.show();
            }
        });
    }

    public MutableLiveData<String> verifyOTP(LinearLayout linearLayout,String email,int otp){
        OTP otp1 = new OTP(email,otp);
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        Call<String> call = api.verifyOTP(otp1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Snackbar snackbar;
                    if (response.code() == 401) {
                        snackbar = Snackbar.make(linearLayout, "Incorrect OTP!", Snackbar.LENGTH_SHORT);
                    } else {
                        snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
//                        snackbar = Snackbar.make(linearLayout, response.code(), Snackbar.LENGTH_SHORT);

                    }
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                    snackbar.show();
                    return;
                }
                mutableLiveData.setValue(response.code()+"");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
//                Snackbar snackbar = Snackbar.make(linearLayout, t.getMessage(), Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                snackbar.show();
            }
        });
        return mutableLiveData;
    }

    public void setNewPassword(Context context, LinearLayout linearLayout, String email, String password) {
        Auth auth = new Auth(email, password);
        Call<Auth> call = api.setNewPassword(email, auth);
        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "error in setting new password: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(linearLayout, "Password changed successfully", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(context.getResources().getColor(R.color.mainBlue));
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.startActivity(new Intent(context, LoginActivity.class));
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        //animation
                    }
                }, 1500);


            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(context.getResources().getColor(R.color.red));
                snackbar.show();
            }
        });
    }


}

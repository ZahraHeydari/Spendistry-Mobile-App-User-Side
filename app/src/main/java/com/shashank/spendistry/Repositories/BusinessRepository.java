package com.shashank.spendistry.Repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Vendor;
import com.shashank.spendistry.SpendistryApi.SpendistryApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessRepository {
    private Application application;
    private Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    SpendistryApi api = retrofit.create(SpendistryApi.class);

    public BusinessRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<Vendor> getVendor(String vendorId) {
        MutableLiveData<Vendor> vendor = new MutableLiveData<>();
        api.getVendor(vendorId).enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                vendor.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                vendor.setValue(null);
            }
        });
        return vendor;
    }
}

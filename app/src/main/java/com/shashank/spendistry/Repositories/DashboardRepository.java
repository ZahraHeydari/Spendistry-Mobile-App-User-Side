package com.shashank.spendistry.Repositories;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.R;
import com.shashank.spendistry.SpendistryApi.SpendistryApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardRepository {
    private Application application;
    private Gson gson = new GsonBuilder().setLenient().create();
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    SpendistryApi api = retrofit.create(SpendistryApi.class);

    public DashboardRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<Dashboard> getDashboard(LinearLayout linearLayout,String email) {
        MutableLiveData<Dashboard> dashboard = new MutableLiveData<>();
        Call<Dashboard> call = api.getDashboard(email);
        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (response.isSuccessful()) {
                    dashboard.setValue(response.body());
                    return;
                }
                dashboard.setValue(null);
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).startsWith("Unable to resolve host")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(linearLayout, "Internet is not available", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(ContextCompat.getColor(application,R.color.red));
                            snackbar.show();
                        }
                    },500);

//                    businessDB.dashboardDao().getDashboardData(email).observeForever(dashboard -> {
//                        dashboardData.setValue(dashboard);
//                    });
                }
            }
        });
        return dashboard;
    }
}

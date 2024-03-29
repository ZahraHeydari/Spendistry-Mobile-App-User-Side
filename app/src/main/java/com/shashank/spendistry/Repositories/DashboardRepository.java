package com.shashank.spendistry.Repositories;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Dao.DashboardDao;
import com.shashank.spendistry.Database.SpendistryDatabase;
import com.shashank.spendistry.Models.Dashboard;
import com.shashank.spendistry.R;
import com.shashank.spendistry.SpendistryApi.SpendistryApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class DashboardRepository {
    private final Application application;
    private final Gson gson = new GsonBuilder().setLenient().create();
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    SpendistryApi api = retrofit.create(SpendistryApi.class);
    private final SpendistryDatabase database;

    public DashboardRepository(Application application) {
        this.application = application;
        database = SpendistryDatabase.getInstance(application);
    }



    public void fetchDashboardData(LinearLayout linearLayout, String email){
        Call<Dashboard> call = api.getDashboard(email);
        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (response.isSuccessful()) {
                    new addDashboardData(application, database).execute(response.body());
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                if (Objects.requireNonNull(t.getMessage()).startsWith("Unable to resolve host")) {
                    database.dashboardDao().getDashboard(email).observeForever(new Observer<Dashboard>() {
                        @Override
                        public void onChanged(Dashboard dashboard) {
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(linearLayout, "Internet is not available", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(ContextCompat.getColor(application, R.color.red));
                            snackbar.show();
                        }
                    }, 500);
                }
            }
        });
    }

    public LiveData<Dashboard> getDashboard(String email) {
        return database.dashboardDao().getDashboard(email);
    }

    static class addDashboardData extends AsyncTask<Dashboard, Void, Void> {
        private final DashboardDao dashboardDao;
        private final Application application;

        addDashboardData(Application application, SpendistryDatabase spendistryDatabase) {
            this.application = application;
            dashboardDao = spendistryDatabase.dashboardDao();
        }

        @Override
        protected Void doInBackground(Dashboard... dashboard) {
            dashboardDao.deleteAll();
            dashboardDao.addDashboardData(dashboard[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

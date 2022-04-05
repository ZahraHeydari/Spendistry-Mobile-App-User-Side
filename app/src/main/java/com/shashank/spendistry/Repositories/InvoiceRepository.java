package com.shashank.spendistry.Repositories;

import android.app.Application;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.R;
import com.shashank.spendistry.SpendistryApi.SpendistryApi;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceRepository {
    private Application application;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
    SpendistryApi api = retrofit.create(SpendistryApi.class);
//    private SpendistryBusinessDB businessDB;
//    private MutableLiveData<BusinessInvoices> mutableLiveData;


    public InvoiceRepository(Application application) {
        this.application = application;
//        businessDB = SpendistryBusinessDB.getInstance(application);
    }




    public MutableLiveData<List<Report>> getReportedInvoices(String email) {
        MutableLiveData<List<Report>> mutableLiveData = new MutableLiveData<>();
        Call<List<Report>> call = api.getReportedInvoices(email);
        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public void deleteReportRequest(LinearLayout linearLayout,String reportId){
        Call<Report> call = api.deleteReportRequest(reportId);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar snackbar = Snackbar.make(linearLayout, "Reported Invoice Deleted", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(application.getResources().getColor(R.color.cardBlue));
                snackbar.show();
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {

            }
        });
    }



//    public MutableLiveData<ResponseBody> getPDF(String email, String businessEmail, String invoiceId) {
//        //download pdf file with retrofit 2
//        MutableLiveData<ResponseBody> mutableLiveData = new MutableLiveData<>();
//        Call<ResponseBody> call = api.getPDF(email, businessEmail, invoiceId);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (!response.isSuccessful()) {
//                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//              //set data
//                if (response.body() != null) {
//                    mutableLiveData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//        return mutableLiveData;
//    }



//    static class updateCurrentInvoiceNumber extends AsyncTask<Dashboard, Void, Void> {
//        private Application application;
//        private dashboardDao dashboardDao;
//
//        updateCurrentInvoiceNumber(Application application, SpendistryBusinessDB businessDB) {
//            this.application = application;
//            this.dashboardDao = businessDB.dashboardDao();
//        }
//
//        @Override
//        protected Void doInBackground(Dashboard... dashboards) {
//            Dashboard dashboard = dashboards[0];
//            dashboardDao.updateCurrentInvoiceNumber(dashboard);
//            return null;
//        }
//
//
//    }
//
//    static class addInvoicesToDB extends AsyncTask<BusinessInvoices, Void, Void> {
//        private BusinessInvoicesDao businessInvoicesDao;
//        private Application application;
//
//        addInvoicesToDB(Application application, SpendistryBusinessDB businessDB) {
//            this.application = application;
//            businessInvoicesDao = businessDB.businessInvoicesDao();
//        }
//
//        @Override
//        protected Void doInBackground(BusinessInvoices... invoices) {
//            businessInvoicesDao.deleteAll();
//            businessInvoicesDao.addBusinessInvoices(invoices[0]);
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            Toast.makeText(application, "dashboard added", Toast.LENGTH_SHORT).show();
//        }
//    }


}


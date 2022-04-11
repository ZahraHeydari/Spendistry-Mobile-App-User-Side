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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.Models.Root;
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
    private MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
//    private SpendistryBusinessDB businessDB;
//    private MutableLiveData<BusinessInvoices> mutableLiveData;


    public InvoiceRepository(Application application) {
        this.application = application;
//        businessDB = SpendistryBusinessDB.getInstance(application);
    }

    public void setUserId(String email) {
        stringMutableLiveData.setValue(email);
    }



    public MutableLiveData<List<Report>> getReportedInvoices() {
        MutableLiveData<List<Report>> mutableLiveData = new MutableLiveData<>();
        stringMutableLiveData.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Call<List<Report>> call = api.getReportedInvoices(s);
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
                }
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ResponseBody> getPDF(String email, String businessEmail, String invoiceId) {
        //download pdf file with retrofit 2
        MutableLiveData<ResponseBody> mutableLiveData = new MutableLiveData<>();
        Call<ResponseBody> call = api.getPDF(email.trim(), businessEmail.trim(), invoiceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //set data
                if (response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(application, "why??? "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<Report> reportInvoice(Report report){
        MutableLiveData<Report> mutableLiveData = new MutableLiveData<>();
        Call<Report> call = api.reportInvoice(report);
        call.enqueue(new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {

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

    public MutableLiveData<ArrayList<Invoice>> getBusinessInvoices(String email, String businessEmail) {
//        Toast.makeText(application, "yi", Toast.LENGTH_SHORT).show();
        MutableLiveData<ArrayList<Invoice>> mutableLiveData = new MutableLiveData<>();
        Call<ArrayList<Root>> call = api.getBusinessInvoices(email, businessEmail);
        call.enqueue(new Callback<ArrayList<Root>>() {
            @Override
            public void onResponse(Call<ArrayList<Root>> call, Response<ArrayList<Root>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mutableLiveData.setValue(response.body().get(0).getBusinessName().get(0).getInvoices());
            }

            @Override
            public void onFailure(Call<ArrayList<Root>> call, Throwable t) {
                Toast.makeText(application, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<Invoice>> getAllInvoices(String email) {
        MutableLiveData<ArrayList<Invoice>> mutableLiveData = new MutableLiveData<>();
        Call<Root> call = api.getAllInvoices(email);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Invoice> invoices = new ArrayList<>();
                for (int i = 0; i < Objects.requireNonNull(response.body()).getBusinessName().size(); i++) {
                    invoices.addAll(response.body().getBusinessName().get(i).getInvoices());
                }
                mutableLiveData.setValue(invoices);
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<Invoice>> getReturnedInvoices(String email) {
        MutableLiveData<ArrayList<Invoice>> mutableLiveData = new MutableLiveData<>();
        Call<ArrayList<Invoice>> call = api.getReturnedInvoices(email);
        call.enqueue(new Callback<ArrayList<Invoice>>() {
            @Override
            public void onResponse(Call<ArrayList<Invoice>> call, Response<ArrayList<Invoice>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Invoice>> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<Invoice>> getSingleReportedInvoice(String email,String businessEmail ,String invoiceId) {
        MutableLiveData<ArrayList<Invoice>> mutableLiveData = new MutableLiveData<>();
        Call<Invoice> call = api.getSingleReportedInvoice(email,businessEmail,invoiceId);
        call.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(application, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<Invoice> invoices = new ArrayList<>();
                invoices.add(response.body());
                mutableLiveData.setValue(invoices);
            }
            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }
}


package com.shashank.spendistry.ViewModels;

import android.app.Application;
import android.widget.LinearLayout;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.shashank.spendistry.Constants.Constants;
import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Models.ItemPrices;
import com.shashank.spendistry.Models.Report;
import com.shashank.spendistry.Repositories.DashboardRepository;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class InvoiceViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    InvoiceRepository invoiceRepository;
    MutableLiveData<List<ItemPrices>> data = Constants.data;
    ArrayList<ItemPrices> itemPricesArrayList = Constants.itemPricesArrayList;
    DashboardRepository dashboardRepository;

    public InvoiceViewModel(Application application) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        dashboardRepository = new DashboardRepository(application);
    }

    public MutableLiveData<ResponseBody> getPDF( String email, String businessEmail, String invoiceNumber){

        return invoiceRepository.getPDF(email,businessEmail,invoiceNumber);
    }

    public MutableLiveData<ArrayList<Invoice>> getReturnedInvoices(String email){
        return invoiceRepository.getReturnedInvoices(email);
    }

    public MutableLiveData<Report> reportInvoice(Report report){
        return invoiceRepository.reportInvoice(report);
    }

    public MutableLiveData<ArrayList<Invoice>> getSingleReportedInvoice(String email, String businessEmail, String invoiceID){
        return invoiceRepository.getSingleReportedInvoice(email, businessEmail, invoiceID);
    }
}
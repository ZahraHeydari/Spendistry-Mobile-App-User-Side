package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.ArrayList;

public class SingleReportedViewModel extends AndroidViewModel {
    InvoiceRepository invoiceRepository;
    MutableLiveData<ArrayList<Invoice>> invoiceList;

    public SingleReportedViewModel(@NonNull Application application, String email, String businessEmail, String invoiceID) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        invoiceList = invoiceRepository.getSingleReportedInvoice(email, businessEmail, invoiceID);
    }

    public MutableLiveData<ArrayList<Invoice>> getSingleReportedInvoice(){
        return invoiceList;
    }
}

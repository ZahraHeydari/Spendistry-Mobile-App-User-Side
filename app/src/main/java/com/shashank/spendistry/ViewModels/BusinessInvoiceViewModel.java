package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.ArrayList;

public class BusinessInvoiceViewModel extends AndroidViewModel {
    InvoiceRepository invoiceRepository;
    MutableLiveData<ArrayList<Invoice>> businessInvoiceList;

    public BusinessInvoiceViewModel(@NonNull Application application, String email, String businessEmail) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        businessInvoiceList = invoiceRepository.getBusinessInvoices(email, businessEmail);
    }

    public MutableLiveData<ArrayList<Invoice>> getBusinessInvoices(){
        return businessInvoiceList;
    }
}

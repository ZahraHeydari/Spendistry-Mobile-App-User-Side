package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.ArrayList;

public class AllInvoiceViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Invoice>> allInvoices;
    InvoiceRepository invoiceRepository;

    public AllInvoiceViewModel(@NonNull Application application, String email) {
        super(application);
        invoiceRepository = new InvoiceRepository(application);
        allInvoices = invoiceRepository.getAllInvoices(email);

    }

    public MutableLiveData<ArrayList<Invoice>> getAllInvoices(){
        return allInvoices;
    }
}

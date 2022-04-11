package com.shashank.spendistry.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashank.spendistry.Models.Invoice;
import com.shashank.spendistry.Repositories.InvoiceRepository;

import java.util.ArrayList;

public class ReturnedInvoiceViewModel extends AndroidViewModel {
    InvoiceRepository repository;
    MutableLiveData<ArrayList<Invoice>> returnedInvoiceList;

    public ReturnedInvoiceViewModel(@NonNull Application application, String email) {
        super(application);
        repository = new InvoiceRepository(application);
        returnedInvoiceList = repository.getReturnedInvoices(email);
    }

    public MutableLiveData<ArrayList<Invoice>> getReturnedInvoices(){
        return returnedInvoiceList;
    }
}
